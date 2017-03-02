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

    private static final double defaultValue = Double.NaN;

    // Fuer test
    public static String testCenteruser(TsData data) {

        DataBlock block = new DataBlock(data);
        double mean = block.sum() / block.getLength();

        Preferences node = NbPreferences.forModule(TransRegOptionsPanelController.class);
        double upper = node.getDouble(TransRegOptionsPanelController.TRANSREG_UPPER_LIMIT, 1E-4);
        double lower = node.getDouble(TransRegOptionsPanelController.TRANSREG_LOWER_LIMIT, 1E-12);

        String rslt;
        if (Math.abs(mean) <= upper) {
            rslt = "Probably centred";
//            vermutlich zentriert
            if (Math.abs(mean) <= lower) {
                // auf jeden fall zentiert
                rslt = "Centred";
            }

        } else {
            // nicht zentriert
            rslt = "Not centred";
        }
        return rslt;

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
        ArrayList<TransRegVar> result = new ArrayList();
        String name = var.getName() + "_group";

        GroupsSettings settings = var.getSettings().getGroups();
        GroupsEnum[] groups_array = settings.getGroups();
        int start = var.getTsData().getStart().getPosition();
        int freq = var.getTsData().getFrequency().intValue(); // 12 oder 4

        // Iteration ueber alle moeglichen gruppen
        for (int i = 0; i <= settings.getMaxGroupNumber(); i++) {
            // copy assigned variable
            TransRegVar cur = new TransRegVar(name + (i + 1), var.getMoniker(), var.getOriginalData());
            cur.setSettings(var.getSettings());
            cur.setLevel(NodesLevelEnum.GROUP);

            // new Group status for variable
            GroupsEnum currentGroup = GroupsEnum.valueOf("Group" + (i + 1));
            cur.setGroupStatus(currentGroup);

            // iterator for GroupsEnum[] from settings, synchronized to the Observations position
            int i_groups_array = start;
            Iterator<TsObservation> iterator = var.getTsData().iterator();
            while (iterator.hasNext()) {
                TsObservation obs = iterator.next();
                double value = defaultValue;
                GroupsEnum group_status_for_current_obs = groups_array[i_groups_array];
                if (group_status_for_current_obs.equals(currentGroup)) {
                    value = obs.getValue();
                    cur.getTsData().set(obs.getPeriod(), obs.getValue());
                }
                // modifiy calculatedData 
                cur.getTsData().set(obs.getPeriod(), value);
                // increase iterator, but groupsEnum[] depends on frequency
                i_groups_array++;
                if (i_groups_array > (freq - 1)) {
                    i_groups_array = 0;
                }
            }

            // Vater Kind Beziehung
            cur.setParent(var);
            var.addChild(cur);

            result.add(cur);
        }
        return result;
    }

    private static TransRegVar doCenteruser(TransRegVar var) {
        TransRegVar result;
        String name = var.getName() + "_centred";
        result = new TransRegVar(name, var.getMoniker(), var.getOriginalData());
        result.setLevel(NodesLevelEnum.CENTERUSER);

        TsData newData = var.getTsData();
        CenteruserSettings settings = var.getSettings().getCenteruser();
        switch (settings.getMethod()) {
            case Mean:
                TsData selectedData = var.getTsData().select(settings.getSpan());
                double mean = new DescriptiveStatistics(selectedData.getValues()).getAverage();
                newData = var.getTsData().minus(mean);
                break;
            case Seasonal:
                TsData dataMean = result.getTsData().select(settings.getSpan());

                double[] seasonalMean = new double[dataMean.getFrequency().intValue()];
                int[] seasonal_n = new int[dataMean.getFrequency().intValue()];

                // Aufaddieren und ZÃƒÆ’Ã‚Â¤hlen der Elemente
                int position = dataMean.getStart().getPosition();
                for (int i = 0; i < dataMean.getLength(); i++) {
                    seasonalMean[position] += dataMean.get(i);
                    seasonal_n[position]++;

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
        result.setCalculatedData(newData);

        //VaterKind Bez.
        result.setParent(var);
        var.addChild(result);

        return result;
    }
}
