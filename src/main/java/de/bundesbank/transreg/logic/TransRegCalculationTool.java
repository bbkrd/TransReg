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
import de.bundesbank.transreg.settings.GroupsSettings;
import de.bundesbank.transreg.settings.TransRegSettings;
import de.bundesbank.transreg.ui.nodes.NodesLevelEnum;
import static de.bundesbank.transreg.util.CenteruserEnum.Global;
import static de.bundesbank.transreg.util.CenteruserEnum.Seasonal;
import de.bundesbank.transreg.util.GroupsEnum;
import ec.tss.tsproviders.utils.MultiLineNameUtil;
import ec.tstoolkit.data.DataBlock;
import ec.tstoolkit.data.DescriptiveStatistics;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsObservation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author s4504gn
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

    public static ArrayList<TransRegVar> calculate(TransRegVar var) {
        LocalDateTime stamp = LocalDateTime.now();
        var.setTimestamp(stamp);
        TransRegSettings settings = var.getSettings();
        ArrayList<TransRegVar> result = new ArrayList<>();

        //1. Groups
        if (settings.getGroups().isEnabled()) {
            ArrayList<TransRegVar> groupVars = doGroups(var);
            result.addAll(groupVars);
        }

        //2. Centeruser
        if (settings.getCenteruser().isEnabled()) {
            if (result.isEmpty()) {
                // if no previuos step
                result.add(doCenteruser(var));
            } else {
                // if previous results
                ArrayList<TransRegVar> tmp = new ArrayList<>();
                result.stream().forEach((cur) -> {
                    tmp.add(doCenteruser(cur));
                });
                result.addAll(tmp);
            }
        }

        result.stream().forEach((t) -> {
            t.setTimestamp(stamp);
        });

        return result;

    }

    private static ArrayList<TransRegVar> doGroups(TransRegVar var) {
        TransRegVar result = var.copy();
        String name = var.getName() + "\n" + "Group";
        name = MultiLineNameUtil.join(name);
        result.setName(name);
//        result.setLevel(NodesLevelEnum.CENTERUSER);
        ArrayList<TransRegVar> results = new ArrayList();

        GroupsSettings settings = var.getSettings().getGroups();
        GroupsEnum[] groups_array = settings.getGroups();

        // Iteration ueber alle moeglichen gruppen, 0-basierend
        for (int i = 0; i <= settings.getMaxGroupNumber(); i++) {
            // copy assigned variable
            TransRegVar cur = new TransRegVar(name + (i + 1), result.getMoniker(), result.getOriginalData());
            cur.setSettings(result.getSettings());
            cur.setLevel(NodesLevelEnum.GROUP);

            // new Group status for variable
            GroupsEnum currentGroup = GroupsEnum.valueOf("Group" + (i + 1));
            cur.setGroupStatus(currentGroup);

            // iterator for GroupsEnum[] from settings, synchronized to the Observations position
            Iterator<TsObservation> iterator = result.getTsData().iterator();
            while (iterator.hasNext()) {
                TsObservation obs = iterator.next();

                double value;
                GroupsEnum groupStatusForCurrentObservation = groups_array[obs.getPeriod().getPosition()];
                if (groupStatusForCurrentObservation.equals(currentGroup)) {
                    value = obs.getValue();
                } else {
                    value = settings.getDefaultValue().getValue();
                }

                // modifiy calculatedData
                cur.getTsData().set(obs.getPeriod(), value);
            }

            // Vater Kind Beziehung
            cur.setParent(var);
            var.addChild(cur);

            results.add(cur);
        }
        return results;
    }

    private static TransRegVar doCenteruser(TransRegVar var) {
        TransRegVar result = var.copy();
        String name = var.getName() + "\n" + "Centred";
        name = MultiLineNameUtil.join(name);
        result.setName(name);
        result.setLevel(NodesLevelEnum.CENTERUSER);

        TsData newData = result.getTsData().clone();
        CenteruserSettings settings = result.getSettings().getCenteruser();
        switch (settings.getMethod()) {
            case Global:
                TsData selectedData = result.getTsData().select(settings.getSpan());
                double mean = new DescriptiveStatistics(selectedData).getAverage();
                result.setMean(mean);
                newData = result.getTsData().minus(mean);
                break;
            case Seasonal:
                TsData dataMean = result.getTsData().select(settings.getSpan());
                double[] seasonalMean = calcSeasonalMeans(dataMean);
                result.setMean(seasonalMean);
                // von OriginalZR abziehen
                Iterator<TsObservation> iterator = var.getTsData().iterator();
                while (iterator.hasNext()) {
                    TsObservation obs = iterator.next();
                    newData.set(obs.getPeriod(), (obs.getValue() - seasonalMean[obs.getPeriod().getPosition()]));
                }
                break;
        }
        // NaN's werden durch 0 ersetzt, damit Wert da steht (Anwender wollten 0)
        newData.setIf(Double::isNaN, () -> 0.0);

        result.setCalculatedData(newData);

        //VaterKind Bez.
        result.setParent(var);
        var.addChild(result);

        return result;
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
}
