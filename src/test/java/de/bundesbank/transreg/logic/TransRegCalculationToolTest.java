/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.logic;

import de.bundesbank.transreg.ui.nodes.NodesLevelEnum;
import de.bundesbank.transreg.util.CenteruserEnum;
import de.bundesbank.transreg.util.DefaultValueEnum;
import de.bundesbank.transreg.util.Epoch;
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
 * @author Nina Gonschorreck
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

        double[] r1 = new double[]{
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, Double.NaN,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, Double.NaN,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, Double.NaN,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, Double.NaN,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, Double.NaN,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, Double.NaN
        };

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

    @Test
    public void testMissingValues_mean() {

        double[] d1 = new double[]{
            1, Double.NaN, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
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

    @Test
    public void test_EpochNaN_Seasonal() {

        double[] d = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            3, 3, 3, 3,
            4, 4, 4, 4,
            5, 5, 5, 5
        };

        TsPeriod start = new TsPeriod(TsFrequency.Quarterly, 1999, 0);
        TsData inputData = new TsData(start, d, true);

        ArrayList<TransRegVar> result;
        TsData expResult1;

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);

        v.getSettings().getEpoch().setEnabled(true);
        v.getSettings().getEpoch().setDefaultValue(DefaultValueEnum.NaN);

        Epoch e1 = new Epoch(new Day(1999, Month.January, 0), new Day(2000, Month.December, 30));
        Epoch e2 = new Epoch(new Day(2002, Month.January, 0), new Day(2003, Month.December, 30));
        v.getSettings().getEpoch().setEpochs(new Epoch[]{e1, e2});

        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.EPOCH);

        double[] d1 = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            4, 4, 4, 4,
            5, 5, 5, 5
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d1,
                true
        );

        assertEquals(expResult1, result.get(0).getTsData());

        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        double[] d2 = new double[]{
            -2, -2, -2, -2,
            -1, -1, -1, -1,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            1, 1, 1, 1,
            2, 2, 2, 2,};

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d2,
                true
        );

        assertEquals(expResult1, result.get(0).getTsData());
    }

    @Test
    public void test_EpochNaN_GroupsNaN_Seasonal() {

        double[] d = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            3, 3, 3, 3,
            4, 4, 4, 4,
            5, 5, 5, 5
        };

        TsPeriod start = new TsPeriod(TsFrequency.Quarterly, 1999, 0);
        TsData inputData = new TsData(start, d, true);

        ArrayList<TransRegVar> result;
        TsData expResult1;

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);

        v.getSettings().getEpoch().setEnabled(true);
        v.getSettings().getEpoch().setDefaultValue(DefaultValueEnum.NaN);
        Epoch e1 = new Epoch(new Day(1999, Month.January, 0), new Day(2000, Month.December, 30));
        Epoch e2 = new Epoch(new Day(2002, Month.January, 0), new Day(2003, Month.December, 30));
        v.getSettings().getEpoch().setEpochs(new Epoch[]{e1, e2});

        v.getSettings().getGroups().setEnabled(true);
        v.getSettings().getGroups().setDefaultValue(DefaultValueEnum.NaN);
        Group g1 = new Group(1);
        Group g2 = new Group(2);
        v.getSettings().getGroups().setGroups(new Group[]{g1, g2, g2, g2});

        // Test Epoch 
        //**********************************************************************
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.EPOCH);
        double[] d1 = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            4, 4, 4, 4,
            5, 5, 5, 5
        };
        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d1,
                true
        );

        assertEquals(expResult1, result.get(0).getTsData());

        // Test Group
        //**********************************************************************
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.GROUP);

        double[] d_g1 = new double[]{
            1, Double.NaN, Double.NaN, Double.NaN,
            2, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            4, Double.NaN, Double.NaN, Double.NaN,
            5, Double.NaN, Double.NaN, Double.NaN
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d_g1,
                true
        );
        assertEquals(expResult1, result.get(0).getTsData());

        double[] d_g2 = new double[]{
            Double.NaN, 1, 1, 1,
            Double.NaN, 2, 2, 2,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, 4, 4, 4,
            Double.NaN, 5, 5, 5};

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d_g2,
                true
        );

        assertEquals(expResult1, result.get(1).getTsData());

        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);
        d_g1 = new double[]{
            -2, Double.NaN, Double.NaN, Double.NaN,
            -1, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            1, Double.NaN, Double.NaN, Double.NaN,
            2, Double.NaN, Double.NaN, Double.NaN
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d_g1,
                true
        );
        assertEquals(expResult1, result.get(0).getTsData());

        d_g2 = new double[]{
            Double.NaN, -2, -2, -2,
            Double.NaN, -1, -1, -1,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, 1, 1, 1,
            Double.NaN, 2, 2, 2};

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d_g2,
                true
        );

        assertEquals(expResult1, result.get(1).getTsData());
    }

    @Test
    public void testEpochZero_Seasonal() {

        double[] d = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            3, 3, 3, 3,
            4, 4, 4, 4,
            5, 5, 5, 5
        };

        TsPeriod start = new TsPeriod(TsFrequency.Quarterly, 1999, 0);
        TsData inputData = new TsData(start, d, true);

        ArrayList<TransRegVar> result;
        TsData expResult1;

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);

        v.getSettings().getEpoch().setEnabled(true);
        v.getSettings().getEpoch().setDefaultValue(DefaultValueEnum.ZERO);

        Epoch e1 = new Epoch(new Day(1999, Month.January, 0), new Day(2000, Month.December, 30));
        Epoch e2 = new Epoch(new Day(2002, Month.January, 0), new Day(2003, Month.December, 30));
        v.getSettings().getEpoch().setEpochs(new Epoch[]{e1, e2});

        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        double[] d2 = new double[]{
            -2, -2, -2, -2,
            -1, -1, -1, -1,
            0, 0, 0, 0,
            1, 1, 1, 1,
            2, 2, 2, 2,};

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d2,
                true
        );

        assertEquals(expResult1, result.get(0).getTsData());
    }

    @Test
    public void test_EpochZero_GroupsNaN_Seasonal() {

        double[] d = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            3, 3, 3, 3,
            4, 4, 4, 4,
            5, 5, 5, 5
        };

        TsPeriod start = new TsPeriod(TsFrequency.Quarterly, 1999, 0);
        TsData inputData = new TsData(start, d, true);

        ArrayList<TransRegVar> result;
        TsData expResult1;

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);

        v.getSettings().getEpoch().setEnabled(true);
        v.getSettings().getEpoch().setDefaultValue(DefaultValueEnum.ZERO);
        Epoch e1 = new Epoch(new Day(1999, Month.January, 0), new Day(2000, Month.December, 30));
        Epoch e2 = new Epoch(new Day(2002, Month.January, 0), new Day(2003, Month.December, 30));
        v.getSettings().getEpoch().setEpochs(new Epoch[]{e1, e2});

        v.getSettings().getGroups().setEnabled(true);
        v.getSettings().getGroups().setDefaultValue(DefaultValueEnum.NaN);
        Group g1 = new Group(1);
        Group g2 = new Group(2);
        v.getSettings().getGroups().setGroups(new Group[]{g1, g2, g2, g2});

        // Test Epoch 
        //**********************************************************************
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.EPOCH);
        double[] d1 = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            4, 4, 4, 4,
            5, 5, 5, 5
        };
        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d1,
                true
        );

//        assertEquals(expResult1, result.get(0).getTsData());
        // Test Group
        //**********************************************************************
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.GROUP);

        double[] d_g1 = new double[]{
            1, Double.NaN, Double.NaN, Double.NaN,
            2, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            4, Double.NaN, Double.NaN, Double.NaN,
            5, Double.NaN, Double.NaN, Double.NaN
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d_g1,
                true
        );
//        assertEquals(expResult1, result.get(0).getTsData());

        double[] d_g2 = new double[]{
            Double.NaN, 1, 1, 1,
            Double.NaN, 2, 2, 2,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, 4, 4, 4,
            Double.NaN, 5, 5, 5};

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d_g2,
                true
        );

//        assertEquals(expResult1, result.get(1).getTsData());
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);
        d_g1 = new double[]{
            -2, Double.NaN, Double.NaN, Double.NaN,
            -1, Double.NaN, Double.NaN, Double.NaN,
            0, Double.NaN, Double.NaN, Double.NaN,
            1, Double.NaN, Double.NaN, Double.NaN,
            2, Double.NaN, Double.NaN, Double.NaN
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d_g1,
                true
        );

        assertEquals(expResult1, result.get(0).getTsData());

        d_g2 = new double[]{
            Double.NaN, -2, -2, -2,
            Double.NaN, -1, -1, -1,
            Double.NaN, 0, 0, 0,
            Double.NaN, 1, 1, 1,
            Double.NaN, 2, 2, 2};

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d_g2,
                true
        );

        assertEquals(expResult1, result.get(1).getTsData());
    }

    @Test
    public void test_Eistageregressor() {

        double[] d = {
            1.42, 8.94, 0, 0, 0, 0, 0, 0, 0, 0, 0.03, 3.31,
            3.16, 0.69, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3.79,
            2.22, 4.27, 1, 0, 0, 0, 0, 0, 0, 0, 5.6, 0.93,
            0.58, 3.26, 0.03, 0, 0, 0, 0, 0, 0, 0, 0, 0.55,
            4.1, 0.03, 0.04, 0, 0, 0, 0, 0, 0, 0, 0.41, 8.02,
            11.95, 7.81, 1.75, 0, 0, 0, 0, 0, 0, 0, 0.08, 8.59,
            9.8, 0.27, 0, 0, 0, 0, 0, 0, 0, 0, 0.13, 2.29,
            2.65, 0.87, 0, 0, 0, 0, 0, 0, 0, 0, 2.66, 6.18,
            1.76, 2.8, 0, 0, 0, 0, 0, 0, 0, 0, 0.93, 2.21,
            2.64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2.18,
            2.77, 0.71, 0.14, 0, 0, 0, 0, 0, 0, 0, 0.11, 3.22,
            6.43, 0, 0, 0, 0, 0, 0, 0, 0, 0.01, 0, 5.96,
            6.1, 5.32, 0.07, 0, 0, 0, 0, 0, 0, 0.03, 0.03, 1.03,
            4.61, 0.53, 0.49, 0, 0, 0, 0, 0, 0, 0, 0.01, 2.98, 3.08, 4.91, 2.1, 0, 0, 0, 0, 0, 0, 0, 0.43, 4.13, 7.3, 2.83, 1.18, 0, 0, 0, 0, 0, 0, 0, 0, 0.58, 2.2, 0.05, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3.86, 1.25, 0.25, 0, 0, 0, 0, 0, 0, 0, 0, 0.19, 1.94, 8.34, 1.37, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4.29, 12.71, 6.34, 1.07, 0, 0, 0, 0, 0, 0, 0, 2.09, 13.9, 3.97, 3.85, 0.04, 0, 0, 0, 0, 0, 0, 0, 0.39, 0, 2.16, 8.57, 0, 0, 0, 0, 0, 0, 0, 0, 0.11, 3.13, 8.42, 3.57, 3.37, 0.04, 0, 0, 0, 0, 0, 0, 0.32, 0.22, 3.05, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1.91, 0.64, 2.11, 0, 0, 0, 0, 0, 0, 0, 0, 0.01, 0, 4.83, 0.07, 0.04, 0, 0, 0, 0, 0, 0, 0, 0, 1.57, 7.03, 1.57, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.01, 4.53, 1.55, 0, 0, 0, 0, 0, 0, 0, 0.11, 1, 3.75, 0.22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.33, 0.51, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.01, 0.16, 4.32, 2.53, 0.43, 0, 0, 0, 0, 0, 0, 0, 0.46, 2.94, 4.32, 2.53, 0.43, 0, 0, 0, 0, 0, 0, 0, 0.46, 2.94, 4.32, 2.53, 0.43, 0, 0, 0, 0, 0, 0, 0, 0.46, 2.94, 4.32, 2.53, 0.43, 0, 0, 0, 0, 0, 0, 0, 0.46, 2.94};

        double[] r = {-2.895294118, 6.414705882, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.425588235, 0.368235294, -1.155294118, -1.835294118, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.455588235, 0.848235294, -2.095294118, 1.744705882, 0.570882353, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, 5.144411765, -2.011764706, -3.735294118, 0.734705882, -0.399117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.455588235, -2.391764706, -0.215294118, -2.495294118, -0.389117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.045588235, 5.078235294, 7.634705882, 5.284705882, 1.320882353, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.375588235, 5.648235294, 5.484705882, -2.255294118, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.325588235, -0.651764706, -1.665294118, -1.655294118, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, 2.204411765, 3.238235294, -2.555294118, 0.274705882, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, 0.474411765, -0.731764706, -1.675294118, -2.525294118, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.455588235, -0.761764706, -1.545294118, -1.815294118, -0.289117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.345588235, 0.278235294, 2.114705882, -2.525294118, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, 0.008823529, -0.455588235, 3.018235294, 1.784705882, 2.794705882, -0.359117647, -0.001176471, 0, 0, 0, 0, 0, 0.028823529, -0.425588235, -1.911764706, 0.294705882, -1.995294118, 0.060882353, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.445588235, 0.038235294, -1.235294118, 2.384705882, 1.670882353, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.025588235, 1.188235294, 2.984705882, 0.304705882, 0.750882353, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.455588235, -2.361764706, -2.115294118, -2.475294118, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.455588235, 0.918235294, -3.065294118, -2.275294118, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.265588235, -1.001764706, 4.024705882, -1.155294118, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.455588235, 1.348235294, 8.394705882, 3.814705882, 0.640882353, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, 1.634411765, 10.95823529, -0.345294118, 1.324705882, -0.389117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.065588235, -2.941764706, -2.155294118, 6.044705882, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.345588235, 0.188235294, 4.104705882, 1.044705882, 2.940882353, 0.038823529, 0, 0, 0, 0, 0, -0.001176471, -0.135588235, -2.721764706, -1.265294118, -2.525294118, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.455588235, -1.031764706, -3.675294118, -0.415294118, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.445588235, -2.941764706, 0.514705882, -2.455294118, -0.389117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.455588235, -1.371764706, 2.714705882, -0.955294118, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.455588235, -2.941764706, -4.305294118, 2.004705882, 1.120882353, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.345588235, -1.941764706, -0.565294118, -2.305294118, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.455588235, -2.611764706, -3.805294118, -2.525294118, -0.429117647, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, -0.445588235, -2.781764706, 0.004705882, 0.004705882, 0.000882353, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, 0.004411765, -0.001764706, 0.004705882, 0.004705882, 0.000882353, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, 0.004411765, -0.001764706, 0.004705882, 0.004705882, 0.000882353, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, 0.004411765, -0.001764706, 0.004705882, 0.004705882, 0.000882353, -0.001176471, 0, 0, 0, 0, 0, -0.001176471, 0.004411765, -0.001764706};

        TsPeriod start = new TsPeriod(TsFrequency.Monthly, 1991, 0);
        TsData inputData = new TsData(start, d, true);
        TsData expResult = new TsData(start, r, true);

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);
        ArrayList<TransRegVar> result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        double epsilon = 0.001;
        TsData diff = result.get(0).getTsData().minus(expResult).abs();

        int length = diff.getLength();
        boolean correct = true;
        for (int i = 0; i < length; i++) {
            if (diff.get(i) >= epsilon) {
                System.out.println(diff.get(i) + " " + i);
                correct = false;
            }
        }
        assertTrue(correct);
    }

    @Test
    public void test__GroupsZero_Seasonal() {

        double[] d = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            3, 3, 3, 3,
            4, 4, 4, 4,
            5, 5, 5, 5
        };

        TsPeriod start = new TsPeriod(TsFrequency.Quarterly, 1999, 0);
        TsData inputData = new TsData(start, d, true);

        ArrayList<TransRegVar> result;
        TsData expResult1;

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);

        v.getSettings().getGroups().setEnabled(true);
        v.getSettings().getGroups().setDefaultValue(DefaultValueEnum.ZERO);
        Group g1 = new Group(1);
        Group g2 = new Group(2);
        v.getSettings().getGroups().setGroups(new Group[]{g1, g2, g2, g2});

        // Test Group
        //**********************************************************************
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.GROUP);

        double[] d_g1 = new double[]{
            1, Double.NaN, Double.NaN, Double.NaN,
            2, Double.NaN, Double.NaN, Double.NaN,
            3, Double.NaN, Double.NaN, Double.NaN,
            4, Double.NaN, Double.NaN, Double.NaN,
            5, Double.NaN, Double.NaN, Double.NaN
        };

        double[] d_g2 = new double[]{
            Double.NaN, 1, 1, 1,
            Double.NaN, 2, 2, 2,
            Double.NaN, 3, 3, 3,
            Double.NaN, 4, 4, 4,
            Double.NaN, 5, 5, 5};

        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);
        d_g1 = new double[]{
            -2, 0, 0, 0,
            -1, 0, 0, 0,
            0, 0, 0, 0,
            1, 0, 0, 0,
            2, 0, 0, 0
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d_g1,
                true
        );

        System.out.println(result.get(0).getTsData());
        assertEquals(expResult1, result.get(0).getTsData());

        d_g2 = new double[]{
            0, -2, -2, -2,
            0, -1, -1, -1,
            0, 0, 0, 0,
            0, 1, 1, 1,
            0, 2, 2, 2};

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d_g2,
                true
        );

        assertEquals(expResult1, result.get(1).getTsData());
    }

    @Test
    public void test_EpochZero_GroupsZero_Seasonal() {

        double[] d = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            3, 3, 3, 3,
            4, 4, 4, 4,
            5, 5, 5, 5
        };

        TsPeriod start = new TsPeriod(TsFrequency.Quarterly, 1999, 0);
        TsData inputData = new TsData(start, d, true);

        ArrayList<TransRegVar> result;
        TsData expResult1;

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);

        v.getSettings().getEpoch().setEnabled(true);
        v.getSettings().getEpoch().setDefaultValue(DefaultValueEnum.ZERO);
        Epoch e1 = new Epoch(new Day(1999, Month.January, 0), new Day(2000, Month.December, 30));
        Epoch e2 = new Epoch(new Day(2002, Month.January, 0), new Day(2003, Month.December, 30));
        v.getSettings().getEpoch().setEpochs(new Epoch[]{e1, e2});

        v.getSettings().getGroups().setEnabled(true);
        v.getSettings().getGroups().setDefaultValue(DefaultValueEnum.ZERO);
        Group g1 = new Group(1);
        Group g2 = new Group(2);
        v.getSettings().getGroups().setGroups(new Group[]{g1, g2, g2, g2});

        // Test Epoch 
        //**********************************************************************
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.EPOCH);
        double[] d1 = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            4, 4, 4, 4,
            5, 5, 5, 5
        };
        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d1,
                true
        );

//        assertEquals(expResult1, result.get(0).getTsData());
        // Test Group
        //**********************************************************************
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.GROUP);

        double[] d_g1 = new double[]{
            1, Double.NaN, Double.NaN, Double.NaN,
            2, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            4, Double.NaN, Double.NaN, Double.NaN,
            5, Double.NaN, Double.NaN, Double.NaN
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d_g1,
                true
        );
//        assertEquals(expResult1, result.get(0).getTsData());

        double[] d_g2 = new double[]{
            Double.NaN, 1, 1, 1,
            Double.NaN, 2, 2, 2,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, 4, 4, 4,
            Double.NaN, 5, 5, 5};

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d_g2,
                true
        );

//        assertEquals(expResult1, result.get(1).getTsData());
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);
        d_g1 = new double[]{
            -2, 0, 0, 0,
            -1, 0, 0, 0,
            0, 0, 0, 0,
            1, 0, 0, 0,
            2, 0, 0, 0
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d_g1,
                true
        );

        assertEquals(expResult1, result.get(0).getTsData());

        d_g2 = new double[]{
            0, -2, -2, -2,
            0, -1, -1, -1,
            0, 0, 0, 0,
            0, 1, 1, 1,
            0, 2, 2, 2};

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d_g2,
                true
        );

        assertEquals(expResult1, result.get(1).getTsData());
    }

    @Test
    public void test_2integratedEpochsNaN_Seasonal() {

        double[] d = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            3, 3, 3, 3,
            4, 4, 4, 4,
            5, 5, 5, 5
        };

        TsPeriod start = new TsPeriod(TsFrequency.Quarterly, 1999, 0);
        TsData inputData = new TsData(start, d, true);

        ArrayList<TransRegVar> result;
        TsData expResult1;

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);

        v.getSettings().getEpoch().setEnabled(true);
        v.getSettings().getEpoch().setDefaultValue(DefaultValueEnum.NaN);

        Epoch e1 = new Epoch(new Day(1999, Month.January, 0), new Day(2001, Month.December, 30));
        Epoch e2 = new Epoch(new Day(2001, Month.April, 0), new Day(2001, Month.December, 30));
        v.getSettings().getEpoch().setEpochs(new Epoch[]{e1, e2});

        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.EPOCH);

        double[] d1 = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            3, 3, 3, 3,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d1,
                true
        );

        assertEquals(expResult1, result.get(0).getTsData());

        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        double[] d2 = new double[]{
            -1, -1, -1, -1,
            0, 0, 0, 0,
            1, 1, 1, 1,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d2,
                true
        );

        assertEquals(expResult1, result.get(0).getTsData());
    }

    @Test
    public void test_2overlappingEpochsNaN_Seasonal() {

        double[] d = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            3, 3, 3, 3,
            4, 4, 4, 4,
            5, 5, 5, 5
        };

        TsPeriod start = new TsPeriod(TsFrequency.Quarterly, 1999, 0);
        TsData inputData = new TsData(start, d, true);

        ArrayList<TransRegVar> result;
        TsData expResult1;

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);

        v.getSettings().getEpoch().setEnabled(true);
        v.getSettings().getEpoch().setDefaultValue(DefaultValueEnum.NaN);

        Epoch e1 = new Epoch(new Day(1999, Month.January, 0), new Day(2000, Month.December, 30));
        Epoch e2 = new Epoch(new Day(2000, Month.July, 0), new Day(2001, Month.December, 30));
        v.getSettings().getEpoch().setEpochs(new Epoch[]{e1, e2});

        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.EPOCH);

        double[] d1 = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            3, 3, 3, 3,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d1,
                true
        );

        assertEquals(expResult1, result.get(0).getTsData());

        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        double[] d2 = new double[]{
            -1, -1, -1, -1,
            0, 0, 0, 0,
            1, 1, 1, 1,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN,
            Double.NaN, Double.NaN, Double.NaN, Double.NaN
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d2,
                true
        );

        assertEquals(expResult1, result.get(0).getTsData());
    }

    @Test
    public void testForecast_GlobalMean() {
        double[] d = new double[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            3, 3, 3, 3,
            4, 4, 4, 4,
            5, 5, 5, 5
        };

        TsPeriod start = new TsPeriod(TsFrequency.Quarterly, 1999, 0);
        TsData inputData = new TsData(start, d, true);

        ArrayList<TransRegVar> result;
        TsData expResult1;

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Global);
        v.getSettings().getCenteruser().setExtendingPeriods(-2);

        HashMap<NodesLevelEnum, ArrayList<TransRegVar>> tmp = TransRegCalculationTool.calculate(v);
        result = tmp.get(NodesLevelEnum.CENTERUSER);

        double[] d2 = new double[]{
            -2, -2, -2, -2,
            -1, -1, -1, -1,
            0, 0, 0, 0,
            1, 1, 1, 1,
            2, 2, 2, 2,
            0, 0, 0, 0,
            0, 0, 0, 0
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Quarterly, 1999, 0),
                d2,
                true
        );

        assertEquals(expResult1, result.get(0).getTsData());

    }

    @Test
    public void test_RegimesMittenImMonat() {

        double[] d = new double[]{
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
            5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5
        };

        TsPeriod start = new TsPeriod(TsFrequency.Monthly, 1999, 0);
        TsData inputData = new TsData(start, d, true);

        ArrayList<TransRegVar> result;
        TsData expResult1;

        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);

        v.getSettings().getEpoch().setEnabled(true);
        v.getSettings().getEpoch().setDefaultValue(DefaultValueEnum.ZERO);

        Epoch e1 = new Epoch(new Day(1999, Month.January, 0), new Day(2001, Month.November, 0));
        Epoch e2 = new Epoch(new Day(2001, Month.November, 29), new Day(2002, Month.December, 30));
        v.getSettings().getEpoch().setEpochs(new Epoch[]{e1, e2});

        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.EPOCH);

        double[] d1 = new double[]{
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 3,
            4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Monthly, 1999, 0),
                d1,
                true
        );

        assertEquals(expResult1, result.get(0).getTsData());
        result = TransRegCalculationTool.calculate(v).get(NodesLevelEnum.CENTERUSER);

        
        double[] d2 = new double[]{
            -1.5, -1.5, -1.5, -1.5, -1.5, -1.5, -1.5, -1.5, -1.5, -1.5, -4/3., -1.5,
            -0.5, -0.5, -0.5, -0.5, -0.5, -0.5, -0.5, -0.5, -0.5, -0.5, -1/3., -0.5,
            0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.0, 0.5,
            1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 5/3., 1.5,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };

        expResult1 = new TsData(
                new TsPeriod(TsFrequency.Monthly, 1999, 0),
                d2,
                true
        );

        assertEquals(expResult1.round(6), result.get(0).getTsData().round(6));
        
    }
}
