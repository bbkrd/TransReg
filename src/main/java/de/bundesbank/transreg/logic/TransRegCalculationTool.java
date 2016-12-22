/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.logic;

import de.bundesbank.transreg.options.TransRegOptionsPanelController;
import de.bundesbank.transreg.settings.CenteruserSettings;
import static de.bundesbank.transreg.util.CenteruserEnum.*;
import ec.tstoolkit.data.DataBlock;
import ec.tstoolkit.data.DescriptiveStatistics;
import ec.tstoolkit.timeseries.simplets.TsData;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author s4504gn
 */
public class TransRegCalculationTool {

    private static final double neutralElement = Double.NaN;

    //TODO: Modifikation
//    public static TsData generateGroupVar(TransRegVar var) {
//
//        GroupsSettings settings = var.getSettings().getGroups();
//
//        boolean[] groupsPeriod = settings.getGroups().getGroupsAsBoolean();
//
//        int length = var.getTsData().getLength();
//
//        TsData newData = var.getTsData();
//        //neutrale Element, welche Groesse???
////            double neutralesElement = Double.NaN;
//
//        int i_groupPeriod = var.getTsData().getStart().getPosition();
//        for (int i = 0; i < length; i++) {
//
//            if (groupsPeriod[i_groupPeriod] == false) {
//                newData.set(i, neutralElement);
//            }
//
//            if (((i_groupPeriod + 1) % var.getTsData().getFrequency().intValue()) == 0) {
//                i_groupPeriod = 0;
//            } else {
//                i_groupPeriod++;
//            }
//        }
//
////            newData.
//            // Ausgabe
////            i_groupPeriod = var.getTsData().getStart().getPosition();
////            for (int i_generatedGroup = 0; i_generatedGroup < length; i_generatedGroup++) {
////                System.out.println((i_generatedGroup + 1) + "  " + groupsPeriod[i_groupPeriod] + " " + var.getTsData().get(i_generatedGroup) + " " + newData.get(i_generatedGroup));
////                if (((i_groupPeriod + 1) % var.getTsData().getFrequency().intValue()) == 0) {
////                    i_groupPeriod = 0;
////                } else {
////                    i_groupPeriod++;
////                }
////            }
//        return newData;
//    }
//
    
    
    // Fuer test
    public static String testCenteruser(TsData data) {

        DataBlock block = new DataBlock(data);
        double mean = block.sum() / block.getLength();

        Preferences node = NbPreferences.forModule(TransRegOptionsPanelController.class);
        double upper = node.getDouble(TransRegOptionsPanelController.TRANSREG_UPPER_LIMIT, Math.pow(10, -4));
        double lower = node.getDouble(TransRegOptionsPanelController.TRANSREG_LOWER_LIMIT, Math.pow(10, -12));
//        double upper = Math.pow(10, -4);
//        double lower = Math.pow(10, -12);

        String rslt;
        if (Math.abs(mean) <= upper) {
            rslt = "Probably centered";
//            vermutlich zentriert
            if (Math.abs(mean) <= lower) {
                // auf jeden fall zentiert
                rslt = "Already centered";
            }

        } else {
            // nicht zentriert
            rslt = "Definitely not centered";
        }
        return rslt;

    }

    public static TsData calculateCenteruser(TransRegVar var) {
        TsData newData; //= null;
        CenteruserSettings settings = var.getSettings().getCenteruser();
        switch (settings.getMethod()) {
            case Mean:
                TsData selectedData = var.getOriginalData().select(settings.getSpan());
                double mean = new DescriptiveStatistics(selectedData.getValues()).getAverage();
                newData = var.getOriginalData().minus(mean);
                System.out.println("Mean: " + mean);
                break;
            case Seasonal:
                TsData dataMean = var.getOriginalData().select(settings.getSpan());

                double[] seasonalMean = new double[dataMean.getFrequency().intValue()];
                int[] seasonal_n = new int[dataMean.getFrequency().intValue()];

                // Aufaddieren und Zählen der Elemente
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
                newData = var.getOriginalData();
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
                newData = new TsData(var.getOriginalData().getStart(), var.getOriginalData().getValues());
        }

        //Ausgabe
//        for (int i = 0; i < calc.getLength(); i++) {
//            System.out.println((i + 1) + " " + var.getOriginalTsData().get(i) + " " + calc.get(i));
//        }
        return newData;
    }

    /* existiert noch nicht als GUI*/
//    public static TsData generateHorizontalVar(TransRegVar var) {
//        
//        // mit neutralem Element = 0.0 
//        
//        TsData newData = new TsData(var.getTsData().getDomain(), neutralElement);
//        TsData selected = var.getTsData().select(var.getTsPeriodSelectorForHorizontal());
//        
//        for(TsObservation obs: selected){
//            newData.set(obs.getPeriod(), obs.getValue());
//        }
//        
//        return newData;
//        
//        
//        //nur selektierter Vektor ohne neutrales Element
////        return var.getOriginalTsData().select(var.getTsPeriodSelectorForHorizontal());
//    }
}
