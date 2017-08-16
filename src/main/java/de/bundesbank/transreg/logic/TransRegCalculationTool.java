/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.logic;

import de.bundesbank.transreg.options.TransRegOptionsPanelController;
import de.bundesbank.transreg.settings.CenteruserSettings;
import de.bundesbank.transreg.settings.GroupsSettings;
import de.bundesbank.transreg.settings.TransRegSettings;
import de.bundesbank.transreg.ui.nodes.NodesLevelEnum;
import static de.bundesbank.transreg.util.CenteruserEnum.*;
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

    private static final double DEFAULT_VALUE = Double.NaN;

    // Pre Test
    public static String testCenteruser(TsData data) {

        // seasonal mean
        double[] seasonalMeans = new double[data.getFrequency().intValue()];
        int[] seasonal_n = new int[data.getFrequency().intValue()];

        // Aufaddieren und Zaehlen der Elemente
        int position = data.getStart().getPosition();
        for (int i = 0; i < data.getLength(); i++) {
            double value = data.get(i);
            if (Double.isFinite(value)) {
                seasonalMeans[position] += value;
                seasonal_n[position]++;
            }

            if (((position + 1) % data.getFrequency().intValue()) == 0) {
                position = 0;
            } else {
                position++;
            }
        }
        double seasonalMean = 0.0;
        //Durchschnitt berechnen
        for (int i = 0; i < seasonal_n.length; i++) {
            seasonalMeans[i] = seasonalMeans[i] / seasonal_n[i];
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
        double mean = block.average();

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
                for (TransRegVar cur : result) {
                    tmp.add(doCenteruser(cur));
                }
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
            TransRegVar cur = new TransRegVar(name + (i + 1), var.getMoniker(), var.getOriginalData());
            cur.setSettings(var.getSettings());
            cur.setLevel(NodesLevelEnum.GROUP);

            // new Group status for variable
            GroupsEnum currentGroup = GroupsEnum.valueOf("Group" + (i + 1));
            cur.setGroupStatus(currentGroup);

            // iterator for GroupsEnum[] from settings, synchronized to the Observations position
            Iterator<TsObservation> iterator = var.getTsData().iterator();
            while (iterator.hasNext()) {
                TsObservation obs = iterator.next();
                double value = DEFAULT_VALUE;
                GroupsEnum group_status_for_current_obs = groups_array[obs.getPeriod().getPosition()];
                if (group_status_for_current_obs.equals(currentGroup)) {
                    value = obs.getValue();
                    cur.getTsData().set(obs.getPeriod(), value);
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
            case Mean:
                TsData selectedData = result.getTsData().select(settings.getSpan());
                double mean = new DescriptiveStatistics(selectedData).getAverage();
                result.setMean(mean);
                newData = result.getTsData().minus(mean);
                break;
            case Seasonal:
                TsData dataMean = result.getTsData().select(settings.getSpan());

                double[] seasonalMean = new double[dataMean.getFrequency().intValue()];
                int[] seasonal_n = new int[dataMean.getFrequency().intValue()];

                // Aufaddieren und Zaehlen der Elemente
                int position = dataMean.getStart().getPosition();
                for (int i = 0; i < dataMean.getLength(); i++) {
                    double value = dataMean.get(i);
                    if (Double.isFinite(value)) {
                        seasonalMean[position] += dataMean.get(i);
                        seasonal_n[position]++;
                    }

                    if (((position + 1) % dataMean.getFrequency().intValue()) == 0) {
                        position = 0;
                    } else {
                        position++;
                    }
                }
                //Durchschnitt berechnen
                for (int i = 0; i < seasonal_n.length; i++) {
                    seasonalMean[i] = seasonalMean[i] / seasonal_n[i];
                }
                result.setMean(seasonalMean);
                // von OriginalZR abziehen
//                newData = var.getTsData();
                position = newData.getStart().getPosition();
                for (int i = 0; i < newData.getLength(); i++) {
                    newData.set(i, (newData.get(i) - seasonalMean[position]));
                    if (((position + 1) % newData.getFrequency().intValue()) == 0) {
                        position = 0;
                    } else {
                        position++;
                    }
                }
                break;
            default:
//                newData = new TsData(var.getOriginalData().getStart(), var.getOriginalData().getValues());
        }
        // NaN's werden durch 0 ersetzt, damit Wert da steht (Anwender wollten 0)
        for (int i = 0; i < newData.getLength(); i++) {
            if (((Double) newData.get(i)).equals(DEFAULT_VALUE)) {
                newData.set(i, 0.0);
            }
        }

        result.setCalculatedData(newData);

        //VaterKind Bez.
        result.setParent(var);
        var.addChild(result);

        return result;
    }
}
