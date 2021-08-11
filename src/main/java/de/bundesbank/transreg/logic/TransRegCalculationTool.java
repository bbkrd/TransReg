/*
 * Copyright 2018 Deutsche Bundesbank
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package de.bundesbank.transreg.logic;

import de.bundesbank.transreg.options.TransRegOptionsPanelController;
import de.bundesbank.transreg.settings.CenteruserSettings;
import de.bundesbank.transreg.settings.EpochSettings;
import de.bundesbank.transreg.settings.GroupsSettings;
import de.bundesbank.transreg.settings.LeadLagSettings;
import de.bundesbank.transreg.settings.TransRegSettings;
import de.bundesbank.transreg.ui.nodes.NodesLevelEnum;
import static de.bundesbank.transreg.util.CenteruserEnum.Global;
import static de.bundesbank.transreg.util.CenteruserEnum.Seasonal;
import de.bundesbank.transreg.util.DefaultValueEnum;
import de.bundesbank.transreg.util.Epoch;
import de.bundesbank.transreg.util.Group;
import ec.tss.tsproviders.utils.MultiLineNameUtil;
import ec.tstoolkit.data.DataBlock;
import ec.tstoolkit.data.DescriptiveStatistics;
import ec.tstoolkit.timeseries.Day;
import ec.tstoolkit.timeseries.TsPeriodSelector;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsObservation;
import ec.tstoolkit.timeseries.simplets.TsPeriod;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author Nina Gonschorreck
 */
public class TransRegCalculationTool {

    // Pre Test
    public static String testCenteruser(TsData data) {
        // seasonal mean
        double[] seasonalMeans = calcSeasonalMeans(data);

        double seasonalMean = 0.0;
        for (int i = 0; i < seasonalMeans.length; i++) {
            //euklidische Norm berechenn (1)
            seasonalMean += (seasonalMeans[i] * seasonalMeans[i]);
        }
        // euklidische Norm (2)
        seasonalMean = Math.sqrt(seasonalMean);

        Preferences node = NbPreferences.forModule(TransRegOptionsPanelController.class);
        double upper = node.getDouble(TransRegOptionsPanelController.TRANSREG_UPPER_LIMIT, 1E-4);
        double lower = node.getDouble(TransRegOptionsPanelController.TRANSREG_LOWER_LIMIT, 1E-12);

        if (seasonalMean <= lower) {
            return "Centred (seasonal means)";
        }
        if (seasonalMean <= upper) {
            return "Probaly centred (seasonal means)";
        }

        // standard mean
        DataBlock block = new DataBlock(data);
        double mean = Math.abs(block.average());

        if (mean <= lower) {
            return "Centred (global mean)";
        }
        if (mean <= upper) {
            return "Probaly centred (global mean)";
        }

        return "Not centred";
    }

    public static HashMap<NodesLevelEnum, ArrayList<TransRegVar>> calculate(TransRegVar original) {

        LocalDateTime stamp = LocalDateTime.now();
        TransRegSettings settings = original.getSettings();

        HashMap<NodesLevelEnum, ArrayList<TransRegVar>> results = new HashMap<>();
        ArrayList<TransRegVar> tmp = new ArrayList<>();
        tmp.add(original);
        results.put(NodesLevelEnum.ORIGINAL, tmp);

        TransRegVar beforeVar = original; //.copy();

        //<editor-fold defaultstate="collapsed" desc="LeadLag">
        if (!settings.getLeadLag().isDefault()) {

            TransRegVar t = beforeVar.copy();
            t.setCalculatedData(doLeadLag(t.getTsData(), t.getSettings().getLeadLag()));

            String name = t.getName() + "\n LeadLag";
            name = MultiLineNameUtil.join(name);
            t.setName(name);
            t.setLevel(NodesLevelEnum.LEADLAG);
            t.setTimestamp(stamp);

            //VaterKind Bez.
            t.setParent(beforeVar);
            beforeVar.addChild(t);

            ArrayList<TransRegVar> ex = new ArrayList<>();
            ex.add(t);
            results.put(NodesLevelEnum.LEADLAG, ex);

            beforeVar = t;//.copy();
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Epoch">
        if (settings.getEpoch().isEnabled()) {
            ArrayList<TransRegVar> ex = new ArrayList<>();
            TsData epochs = doEpoch(beforeVar.getTsData(), beforeVar.getSettings().getEpoch());
            String name = beforeVar.getName() + "\n Regime";

            TransRegVar var = beforeVar.copy();
            var.setCalculatedData(epochs);
            var.setName(MultiLineNameUtil.join(name));
            var.setGroupStatus(1);
            var.setLevel(NodesLevelEnum.EPOCH);
            var.setTimestamp(stamp);

            //VaterKind Bez.
            var.setParent(beforeVar);
            beforeVar.addChild(var);

            ex.add(var);
            results.put(NodesLevelEnum.EPOCH, ex);

            beforeVar = var;
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Groups">
        if (settings.getGroups().isEnabled()) {
            ArrayList<TransRegVar> groupVars = new ArrayList<>();
            ArrayList<TsData> groups = doGroups(beforeVar.getTsData(), beforeVar.getSettings().getGroups());
            String name = beforeVar.getName() + "\n Group";

            for (int i = 0; i < groups.size(); i++) {
                TransRegVar var = beforeVar.copy();
                var.setCalculatedData(groups.get(i));
                var.setName(MultiLineNameUtil.join(name + (i + 1)));
                var.setGroupStatus(i + 1);
                var.setLevel(NodesLevelEnum.GROUP);
                var.setTimestamp(stamp);

                //VaterKind Bez.
                var.setParent(beforeVar);
                beforeVar.addChild(var);

                groupVars.add(var);
            }
            results.put(NodesLevelEnum.GROUP, groupVars);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Centeruser">
        ArrayList<TransRegVar> tmps = new ArrayList<>();
        boolean extendingNode = false;
        if (settings.getCenteruser().isEnabled()) {

            if (!settings.getGroups().isEnabled()) {
                //<editor-fold defaultstate="collapsed" desc="simple way">
                TsData tmpData = beforeVar.getTsData();
                if (settings.getCenteruser().isExtending()) {
                    tmpData = extend(tmpData,
                            settings.getCenteruser().getExtendingPeriods(),
                            calcSeasonalMeans(beforeVar.getTsData()));
                    extendingNode = true;
                }
                tmpData = doCenteruser(tmpData, settings.getCenteruser());

                if (extendingNode) {
                    // auf 0.0 zurücksetzen
                    int periods = settings.getCenteruser().getExtendingPeriods();
                    tmpData = extendingDataToValue(tmpData, periods, 0.0);
                }

                String name = beforeVar.getName() + "\n" + "Centred";
                name = MultiLineNameUtil.join(name);

                TransRegVar centeruser = beforeVar.copy();
                centeruser.setCalculatedData(tmpData);
                centeruser.setName(name);
                centeruser.setSettings(settings);
                centeruser.setLevel(NodesLevelEnum.CENTERUSER);
                centeruser.setTimestamp(stamp);

                //VaterKind Bez.
                centeruser.setParent(beforeVar);
                beforeVar.addChild(centeruser);

                tmps.add(centeruser);
                //</editor-fold>
            } else {
                //<editor-fold defaultstate="collapsed" desc="with groups">
                ArrayList<TsData> dataList = new ArrayList<>();
                ArrayList<TransRegVar> beforeVars = results.get(NodesLevelEnum.GROUP);
                beforeVars.stream().forEach((cur) -> {
                    dataList.add(cur.getTsData());
                });

                boolean extending = false;
                if (settings.getCenteruser().isExtending()) {
                    extending = true;
                    int extend = settings.getCenteruser().getExtendingPeriods();
                    double[] seasonalMeans = calcSeasonalMeans(beforeVar.getOriginalData());
                    for (int i = 0; i < dataList.size(); i++) {
                        dataList.set(i, extend(dataList.get(i), extend, seasonalMeans));
                    }
                }

                for (int i = 0; i < dataList.size(); i++) {
                    dataList.set(i, doCenteruser(dataList.get(i), settings.getCenteruser()));
                }

                if (extending) {
                    int extend = settings.getCenteruser().getExtendingPeriods();
                    for (int i = 0; i < dataList.size(); i++) {
                        dataList.set(i, extendingDataToValue(dataList.get(i), extend, 0.0));
                    }
                }

                //neue Variablen
                for (int i = 0; i < beforeVars.size(); i++) {
                    TransRegVar var = beforeVars.get(i).copy();
                    var.setCalculatedData(dataList.get(i));
                    String name = var.getName() + "\n" + "Centred";
                    name = MultiLineNameUtil.join(name);
                    var.setName(name);
                    var.setLevel(NodesLevelEnum.CENTERUSER);
                    var.setSettings(settings);
                    var.setTimestamp(stamp);

                    //VaterKind Bez.
                    var.setParent(beforeVars.get(i));
                    beforeVars.get(i).addChild(var);
                    tmps.add(var);
                }
//                results.replace(NodesLevelEnum.GROUP, beforeVars);
//                results.remove(NodesLevelEnum.GROUP);
//                results.put(NodesLevelEnum.GROUP, beforeVars);
                //</editor-fold>
            }
            results.put(NodesLevelEnum.CENTERUSER, tmps);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Korrektur von DefaultVauleEnum.Zero">
        //<editor-fold defaultstate="collapsed" desc="Epoch">
        if (settings.getEpoch().isEnabled() && DefaultValueEnum.ZERO.equals(settings.getEpoch().getDefaultValue())) {

            // 1. Epoch
            TransRegVar epoch = results.get(NodesLevelEnum.EPOCH).get(0);

            TsPeriod start = epoch.getTsData().getStart();
            for (int i = 0; i < epoch.getTsData().getLength(); i++) {
                double value = epoch.getTsData().get(i);
                if (Double.isNaN(value)) {
                    TsPeriod current = start.plus(i);
                    boolean activePeriod = false;
                    for (Epoch p : settings.getEpoch().getEpochs()) {
                        /*if (current.contains(p.getStart()) || current.contains(p.getEnd())) {
                            activePeriod = true;
                        }*/
                        if (current.isAfter(p.getStart()) && current.isBefore(p.getEnd())) {
                            activePeriod = true;
                        }
                    }
                    if (!activePeriod) {
                        epoch.getTsData().set(i, 0.0);
                    }
                }
            }

            // 2. Groups
            if (settings.getGroups().isEnabled()) {
                ArrayList<TransRegVar> groups = results.get(NodesLevelEnum.GROUP);

                for (TransRegVar group : groups) {
                    start = group.getTsData().getStart();

                    int groupStatus = group.getGroupStatus();
                    Group[] groupsTemp = group.getSettings().getGroups().getGroups();
                    int[] groupsByPeriod = new int[groupsTemp.length];
                    for (int i = 0; i < groupsTemp.length; i++) {
                        groupsByPeriod[i] = groupsTemp[i].getNumber();
                    }

                    for (int i = 0; i < group.getTsData().getLength(); i++) {
                        double value = group.getTsData().get(i);
                        if (Double.isNaN(value)) {
                            TsPeriod current = start.plus(i);
                            boolean activePeriod = false;
                            for (Epoch p : settings.getEpoch().getEpochs()) {
                                /*if (current.contains(p.getStart()) || current.contains(p.getEnd())) {
                                    activePeriod = true;
                                }*/
                                if (current.isAfter(p.getStart()) && current.isBefore(p.getEnd())) {
                                    activePeriod = true;
                                }
                            }
                            int currentPeriod = (current.getPosition()) % current.getFrequency().intValue();
                            if (!activePeriod && (groupStatus == groupsByPeriod[currentPeriod])) {
                                group.getTsData().set(i, 0.0);
                            }
                        }
                    }
                }
            }

            // 3. Centeruser
            if (settings.getCenteruser().isEnabled()) {
                ArrayList<TransRegVar> centerusers = results.get(NodesLevelEnum.CENTERUSER);
                for (TransRegVar centeruser : centerusers) {

                    int groupStatus = 1;
                    int[] groupsByPeriod = new int[centeruser.getFrequency().intValue()];
                    
                    if (centeruser.getSettings().getGroups().isEnabled()) {
                        groupStatus = centeruser.getGroupStatus();
                        Group[] groupsTemp = centeruser.getSettings().getGroups().getGroups();
//                    groupsByPeriod = new int[groupsTemp.length];
                        for (int i = 0; i < groupsTemp.length; i++) {
                            groupsByPeriod[i] = groupsTemp[i].getNumber();
                        }
                    }else{
                        for (int i = 0; i < groupsByPeriod.length; i++) {
                            groupsByPeriod[i] = 1;
                        }
                    }
                    
                    start = centeruser.getTsData().getStart();
                    for (int i = 0; i < centeruser.getTsData().getLength(); i++) {
                        double value = centeruser.getTsData().get(i);
                        if (Double.isNaN(value)) {
                            TsPeriod current = start.plus(i);
                            boolean activePeriod = false;
                            for (Epoch p : settings.getEpoch().getEpochs()) {
                                /*if (current.contains(p.getStart()) || current.contains(p.getEnd())) {
                                    activePeriod = true;
                                }*/
                                if (current.isAfter(p.getStart()) && current.isBefore(p.getEnd())) {
                                    activePeriod = true;
                                }
                            }
                            int currentPeriod = (current.getPosition()) % current.getFrequency().intValue();
                            if (!activePeriod && (groupStatus == groupsByPeriod[currentPeriod])) {
                                centeruser.getTsData().set(i, 0.0);
                            }
                        }
                    }
                }
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Groups">
        if (settings.getGroups().isEnabled() && DefaultValueEnum.ZERO.equals(settings.getGroups().getDefaultValue())) {

            // 1. Groups
            if (settings.getGroups().isEnabled()) {
                ArrayList<TransRegVar> groups = results.get(NodesLevelEnum.GROUP);

                for (TransRegVar group : groups) {
                    TsPeriod start = group.getTsData().getStart();

                    int groupStatus = group.getGroupStatus();
                    Group[] groupsTemp = group.getSettings().getGroups().getGroups();
                    int[] groupsByPeriod = new int[groupsTemp.length];
                    for (int i = 0; i < groupsTemp.length; i++) {
                        groupsByPeriod[i] = groupsTemp[i].getNumber();
                    }

                    for (int i = 0; i < group.getTsData().getLength(); i++) {
                        double value = group.getTsData().get(i);
                        if (Double.isNaN(value)) {
                            TsPeriod current = start.plus(i);

                            int currentPeriod = (current.getPosition()) % current.getFrequency().intValue();
                            if (groupStatus != groupsByPeriod[currentPeriod]) {
                                group.getTsData().set(i, 0.0);
                            }
                        }
                    }
                }
            }

            // 2. Centeruser
            if (settings.getCenteruser().isEnabled()) {
                ArrayList<TransRegVar> centerusers = results.get(NodesLevelEnum.CENTERUSER);
                for (TransRegVar centeruser : centerusers) {

                    int groupStatus = centeruser.getGroupStatus();
                    Group[] groupsTemp = centeruser.getSettings().getGroups().getGroups();
                    int[] groupsByPeriod = new int[groupsTemp.length];
                    for (int i = 0; i < groupsTemp.length; i++) {
                        groupsByPeriod[i] = groupsTemp[i].getNumber();
                    }

                    TsPeriod start = centeruser.getTsData().getStart();
                    for (int i = 0; i < centeruser.getTsData().getLength(); i++) {
                        double value = centeruser.getTsData().get(i);
                        if (Double.isNaN(value)) {
                            TsPeriod current = start.plus(i);

                            int currentPeriod = (current.getPosition()) % current.getFrequency().intValue();
                            if (groupStatus != groupsByPeriod[currentPeriod]) {
                                centeruser.getTsData().set(i, 0.0);
                            }
                        }
                    }
                }
            }
        }
        //</editor-fold>
        //</editor-fold>
        return results;
    }

    private static ArrayList<TsData> doGroups(TsData data, GroupsSettings settings) {

        ArrayList<TsData> result = new ArrayList<>();
        Group[] groups_array = settings.getGroups();

        // Iteration ueber alle moeglichen Gruppen, 0-basierend
        for (Group group : settings.getGivenGroups()) {
            TsData cur = data.clone();

            // new Group status for variable
            int currentGroup = group.getNumber(); //i + 1;

            // iterator for GroupsEnum[] from settings, synchronized to the Observations position
            Iterator<TsObservation> iterator = data.iterator();
            while (iterator.hasNext()) {
                TsObservation obs = iterator.next();
                double value; // Double wegen NaN?
                Group groupStatusForCurrentObservation = groups_array[obs.getPeriod().getPosition()];
                if (groupStatusForCurrentObservation.getNumber() == currentGroup) {
                    value = obs.getValue();
                } else {
                    value = settings.getDefaultValue().getValue();
                }

                cur.set(obs.getPeriod(), value);
            }
            result.add(cur);
        }
        return result;
    }

    private static TsData doCenteruser(TsData data, CenteruserSettings settings) {

        TsData newData = data.clone();
        switch (settings.getMethod()) {
            case Global:
                TsData selectedData = data.select(settings.getSpan());
                double mean = new DescriptiveStatistics(selectedData).getAverage();
//                result.setMean(mean);
                newData = data.minus(mean);
                break;
            case Seasonal:
                TsData dataMean = data.select(settings.getSpan());
                double[] seasonalMean = calcSeasonalMeans(dataMean);

                // von OriginalZR abziehen
                Iterator<TsObservation> iterator = data.iterator();
                while (iterator.hasNext()) {
                    TsObservation obs = iterator.next();
                    newData.set(obs.getPeriod(), (obs.getValue() - seasonalMean[obs.getPeriod().getPosition()]));
                }
                break;
        }
        /*        
        // NaN's werden durch 0 ersetzt, damit Wert da steht (Anwender wollten 0)
        newData.setIf(Double::isNaN, () -> 0.0); 
         */

        return newData;
    }

    private static TsData extend(TsData data, int extend, double[] seasonalMean) {

        if (extend < 0) {
            extend = extend * (-1) * data.getStart().getFrequency().intValue();
        }
        int period = data.getLastPeriod().getPosition(); // 0-based
        int freq = data.getFrequency().intValue();
        int lastIndexOriginalData = data.getLength();

        data = data.extend(0, extend);

        for (int index = lastIndexOriginalData; index < lastIndexOriginalData + extend; index++) {
            period++;
            if (period >= freq) {
                period = 0;
            }
            data.set(index, seasonalMean[period]);
        }
        return data;
    }

    private static TsData extendingDataToValue(TsData data, int extend, double value) {

        if (extend < 0) {
            extend = extend * (-1) * data.getStart().getFrequency().intValue();
        }
        for (int i = data.getLength() - 1; i >= (data.getLength() - extend); i--) {
            data.set(i, value);
        }
        return data;
    }

    private static TsData doLeadLag(TsData data, LeadLagSettings settings) {

        int periods = settings.getnPeriods();

        /**
         * Von Anwendern so gewuenscht
         *
         * periods > 0 : Shift in die vergangeheit, lag periods < 0 : Shift in
         * die Zukunft, lead
         *
         * meine Logik
         *
         * if (periods > 0) { data = data.lead(periods); } else { data =
         * data.lag(-periods); }
         *
         */
        return data.lead(periods);
    }

    private static double[] calcSeasonalMeans(TsData data) {

        double[] means = new double[data.getFrequency().intValue()];
        int[] n = new int[data.getFrequency().intValue()];

        Iterator<TsObservation> iterator = data.iterator();
        while (iterator.hasNext()) {
            TsObservation obs = iterator.next();
            double value = obs.getValue();
            if (Double.isFinite(value)) {
                means[obs.getPeriod().getPosition()] += value;
                n[obs.getPeriod().getPosition()]++;
            }
        }
        //Durchschnitt berechnen
        for (int i = 0; i < n.length; i++) {
            means[i] = means[i] / n[i];
        }
        return means;
    }

    private static TsData doEpoch(TsData data, EpochSettings settings) {
        TsData result = data.clone();
        for (int i = 0; i < result.getLength(); i++) {
            result.set(i, settings.getDefaultValue().getValue());
        }

        for (Epoch epoch : settings.getEpochs()) {
            Day start = epoch.getStart();
            Day end = epoch.getEnd();

            TsPeriodSelector selector = new TsPeriodSelector();
            selector.between(start, end);
            TsData tmp = data.select(selector);
            Iterator<TsObservation> iter = tmp.iterator();
            while (iter.hasNext()) {
                TsObservation next = iter.next();
                result.set(next.getPeriod(), next.getValue());
            }
        }
        return result;
    }
}
