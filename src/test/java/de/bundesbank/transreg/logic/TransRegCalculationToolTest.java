/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.logic;

import de.bundesbank.transreg.ui.nodes.NodesLevelEnum;
import de.bundesbank.transreg.util.CenteruserEnum;
import de.bundesbank.transreg.util.DefaultValueEnum;
import de.bundesbank.transreg.util.Group;
import ec.tstoolkit.timeseries.Day;
import ec.tstoolkit.timeseries.Month;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.timeseries.simplets.TsPeriod;
import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author s4504gn
 */
public class TransRegCalculationToolTest {

    public TransRegCalculationToolTest() {
    }

    @Test
    public void testDoCenteruser() {

        double[] d1 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        TsPeriod start = new TsPeriod(TsFrequency.Monthly, 1999, 0);
        TsData inputData = new TsData(start, d1, true);

        ArrayList<TransRegVar> result;
        TsData expResult;

        /*
         ************************
         * 1. centeruser = mean *
         ************************
         */
        // i) type = all
        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Global);
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        // expected
        double[] r = new double[]{-5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result.get(0).getTsData());

        // ii)type = from
        double[] d2 = new double[]{6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        TsData inputData2 = new TsData(start, d2, true);
        v = new TransRegVar(inputData2);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Global);
        v.getSettings().getCenteruser().getSpan().from(new Day(2000, Month.January, 0));
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        r = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result.get(0).getTsData());

        // iii) type = to
        double[] d3 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5};

        TsData inputData3 = new TsData(start, d3, true);
        v = new TransRegVar(inputData3);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Global);
        v.getSettings().getCenteruser().getSpan().to(new Day(2003, Month.December, 30));
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        r = new double[]{-5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result.get(0).getTsData());

        // iv) type = between
        double[] d4 = new double[]{6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5, 6.5};

        TsData inputData4 = new TsData(start, d4, true);
        v = new TransRegVar(inputData4);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Global);
        v.getSettings().getCenteruser().getSpan().between(new Day(2000, Month.January, 0), new Day(2003, Month.December, 30));
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        r = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result.get(0).getTsData());

        /*
         ***************************
         * 2. centeruser = seasonal*
         ***************************
         */
        // i) type = all
        v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        //expected
        r = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result.get(0).getTsData());

        // ii)type = from
        v = new TransRegVar(inputData2);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);
        v.getSettings().getCenteruser().getSpan().from(new Day(2000, Month.January, 0));
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        r = new double[]{5.5, 4.5, 3.5, 2.5, 1.5, 0.5, -0.5, -1.5, -2.5, -3.5, -4.5, -5.5,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result.get(0).getTsData());

        // iii) type = to
        v = new TransRegVar(inputData3);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);
        v.getSettings().getCenteruser().getSpan().to(new Day(2003, Month.December, 30));
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        r = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            5.5, 4.5, 3.5, 2.5, 1.5, 0.5, -0.5, -1.5, -2.5, -3.5, -4.5, -5.5};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result.get(0).getTsData());

        // iv) type = between
        v = new TransRegVar(inputData4);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);
        v.getSettings().getCenteruser().getSpan().between(new Day(2000, Month.January, 0), new Day(2003, Month.December, 30));
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        r = new double[]{5.5, 4.5, 3.5, 2.5, 1.5, 0.5, -0.5, -1.5, -2.5, -3.5, -4.5, -5.5,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            5.5, 4.5, 3.5, 2.5, 1.5, 0.5, -0.5, -1.5, -2.5, -3.5, -4.5, -5.5};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result.get(0).getTsData());
    }

    @Test
    public void testDoGroups() {

        double[] d1 = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        TsPeriod start = new TsPeriod(TsFrequency.Monthly, 1999, 0);
        TsData inputData = new TsData(start, d1, true);
        TransRegVar var = new TransRegVar(inputData);
        var.getSettings().getCenteruser().setMethod(CenteruserEnum.None);
        var.getSettings().getGroups().setDefaultValue(DefaultValueEnum.NaN);

        ArrayList<TransRegVar> result;

        // i) 2 groups: JanNov + Dec
        TsData expResult_JanNov;
        TsData expResult_Dec;

        double[] r1 = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, Double.NaN,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, Double.NaN,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, Double.NaN,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, Double.NaN,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, Double.NaN,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, Double.NaN};

        expResult_JanNov = new TsData(start, r1, true);

        double[] r2 = new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 1,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 1,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 1,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 1,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 1,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 1
        };

        expResult_Dec = new TsData(start, r2, true);

        // Settings
        Group g = new Group(1);
        Group[] groups = new Group[]{g, g, g, g, g, g, g, g, g, g, g, new Group(2)};

        var.getSettings().getGroups().setGroups(groups);
        var.getSettings().getGroups().setEnabled(true);

        result = TransRegCalculationTool.calculate(var).get(NodesLevelEnum.GROUP);

        assertEquals(expResult_JanNov, result.get(0).getTsData());
        assertEquals(expResult_Dec, result.get(1).getTsData());

        //ii) 1 Group
        TsData expResult = new TsData(start, d1, true);

        // Settings
        groups = new Group[]{g, g, g, g, g, g, g, g, g, g, g, g};

        var.getSettings().getGroups().setGroups(groups);
        var.getSettings().getGroups().setEnabled(true);

        result = TransRegCalculationTool.calculate(var).get(NodesLevelEnum.GROUP);

        assertEquals(expResult, result.get(0).getTsData());

    }

    //@Test
    //ToDo Nina - 07: was soll hier getestet werden? Wie ist die Abdeckung? Epoch
    public void testCalculate() {
        // kombiniert Gruppen mit centeruser
        double[] d1 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        TsPeriod start = new TsPeriod(TsFrequency.Monthly, 1999, 0);
        TsData inputData = new TsData(start, d1, true);

        ArrayList<TransRegVar> result;

        // expected
        ArrayList<TsData> expResult = new ArrayList<>();

        // Groups 1
        double[] r1 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, Double.NaN,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, Double.NaN,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, Double.NaN,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, Double.NaN,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, Double.NaN,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, Double.NaN};
        expResult.add(new TsData(start, r1, true));

        // Group2
        double[] r2 = new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 12,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 12,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 12,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 12,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 12,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 12};

        expResult.add(new TsData(start, r2, true));
        // group1 + centeruser = sesonal oder mean?

        // group2 + centeruser = sesonal
    }

    @Test
    public void testMissingValues_mean() {

        double[] d1 = new double[]{1, Double.NaN, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            Double.NaN, 2};

        TsPeriod start = new TsPeriod(TsFrequency.Monthly, 1999, 0);
        TsData inputData = new TsData(start, d1, true);

        ArrayList<TransRegVar> result;
        TsData expResult;

        /*
         ************************
         * 1. centeruser = mean *
         ************************
         */
        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Global);
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        // expected
        double[] r = new double[]{-5.5, Double.NaN, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            Double.NaN, -4.5};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result.get(0).getTsData());
    }

    @Test
    public void testMissingValues_seasonal() {
        /*
         ***************************
         * 2. centeruser = seasonal*
         ***************************
         */

        double[] d1 = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, Double.NaN,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
            Double.NaN, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
        TsPeriod start = new TsPeriod(TsFrequency.Monthly, 1999, 0);
        TsData inputData = new TsData(start, d1, true);

        ArrayList<TransRegVar> result;
        TsData expResult;

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        //expected
        double[] r = new double[]{-1.5, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, Double.NaN,
            -0.5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1.5,
            0.5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.5,
            1.5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.5,
            Double.NaN, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1.5};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result.get(0).getTsData());
    }

    @Test
    public void testExtending_GlobalMean() {

        double[] d = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        TsPeriod start = new TsPeriod(TsFrequency.Monthly, 1999, 0);
        TsData inputData = new TsData(start, d, true);

        TsData expResult;

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Global);
        v.getSettings().getCenteruser().setExtendingPeriods(12);

        HashMap<NodesLevelEnum, ArrayList<TransRegVar>> tmp = TransRegCalculationTool.calculate(v);
        ArrayList<TransRegVar> result = tmp.get(NodesLevelEnum.CENTERUSER);

        double[] r = new double[]{-5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

        expResult = new TsData(start, r, true);

        System.out.println(expResult.toString());
        System.out.println(result.get(0).getTsData());
        assertEquals(expResult.getLength(), result.get(0).getTsData().getLength());
        assertEquals(expResult, result.get(0).getTsData());
    }

//    //@Test
//    public void testExtending_basic() {
//
//        double[] d = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
//            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
//            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
//            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
//            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
//
//        TsPeriod start = new TsPeriod(TsFrequency.Monthly, 1999, 0);
//        TsData inputData = new TsData(start, d, true);
//
//        ArrayList<TransRegVar> result;
//        TsData expResult;
//
//        TransRegVar v = new TransRegVar(inputData);
//
//        v.getSettings().getCenteruser().setMethod(CenteruserEnum.None);
//        v.getSettings().getCenteruser().setExtendingPeriods(12);
//
//        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.EXTENDING);
//
//        double[] r = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
//            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
//            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
//            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
//            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
//            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
//
//        expResult = new TsData(start, r, true);
//
//        assertEquals(expResult.getLength(), result.get(0).getTsData().getLength());
//        assertEquals(expResult, result.get(0).getTsData());
//    }
    @Test
    public void test_lead() {

        double[] d = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        TsPeriod start = new TsPeriod(TsFrequency.Monthly, 1999, 0);
        TsData inputData = new TsData(start, d, true);

        ArrayList<TransRegVar> result;
        TsData expResult;

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.None);
        v.getSettings().getLeadLag().setnPeriods(12);

        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.LEADLAG);

        expResult = new TsData(
                new TsPeriod(TsFrequency.Monthly, 2000, 0), d, true
        );

        assertEquals(expResult, result.get(0).getTsData());
    }

    @Test
    public void test_lag() {

        double[] d = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        TsPeriod start = new TsPeriod(TsFrequency.Monthly, 1999, 0);
        TsData inputData = new TsData(start, d, true);

        ArrayList<TransRegVar> result;
        TsData expResult;

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.None);
        v.getSettings().getLeadLag().setnPeriods(-12);

        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.LEADLAG);

        expResult = new TsData(
                new TsPeriod(TsFrequency.Monthly, 1998, 0),
                d,
                true
        );

        assertEquals(expResult, result.get(0).getTsData());
    }

    @Test
    public void testDoGroupsWithExtending() {

        double[] d1 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        TsPeriod start = new TsPeriod(TsFrequency.Monthly, 1999, 0);
        TsData inputData = new TsData(start, d1, true);
        TransRegVar var = new TransRegVar(inputData);
        var.getSettings().getCenteruser().setMethod(CenteruserEnum.Global);
        var.getSettings().getCenteruser().setExtendingPeriods(12);
        var.getSettings().getGroups().setDefaultValue(DefaultValueEnum.NaN);
        Group g = new Group(1);
        Group[] groups = new Group[]{g, g, g, g, g, g, g, g, g, g, g, new Group(2)};

        var.getSettings().getGroups().setGroups(groups);
        var.getSettings().getGroups().setEnabled(true);

        double[] result_group1 = TransRegCalculationTool.calculate(var).get(NodesLevelEnum.CENTERUSER).get(0).getTsData().internalStorage();
        double[] result_group2 = TransRegCalculationTool.calculate(var).get(NodesLevelEnum.CENTERUSER).get(0).getTsData().internalStorage();

    }

    public void testEpoch() {

    }
}
