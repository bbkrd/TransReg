/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.logic;

import de.bundesbank.transreg.util.CenteruserEnum;
import de.bundesbank.transreg.util.GroupsEnum;
import ec.tstoolkit.timeseries.Day;
import ec.tstoolkit.timeseries.Month;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.timeseries.simplets.TsPeriod;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

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
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Mean);
        result = TransRegCalculationTool.calculate(v);

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
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Mean);
        v.getSettings().getCenteruser().getSpan().from(new Day(2000, Month.January, 0));
        result = TransRegCalculationTool.calculate(v);

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
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Mean);
        v.getSettings().getCenteruser().getSpan().to(new Day(2003, Month.December, 30));
        result = TransRegCalculationTool.calculate(v);

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
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Mean);
        v.getSettings().getCenteruser().getSpan().between(new Day(2000, Month.January, 0), new Day(2003, Month.December, 30));
        result = TransRegCalculationTool.calculate(v);

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
        result = TransRegCalculationTool.calculate(v);

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
        result = TransRegCalculationTool.calculate(v);

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
        result = TransRegCalculationTool.calculate(v);

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
        result = TransRegCalculationTool.calculate(v);

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
        GroupsEnum[] groups = new GroupsEnum[]{GroupsEnum.Group1, GroupsEnum.Group1, GroupsEnum.Group1,
            GroupsEnum.Group1, GroupsEnum.Group1, GroupsEnum.Group1,
            GroupsEnum.Group1, GroupsEnum.Group1, GroupsEnum.Group1,
            GroupsEnum.Group1, GroupsEnum.Group1, GroupsEnum.Group2};

        var.getSettings().getGroups().setGroups(groups);
        var.getSettings().getGroups().setEnabled(true);

        result = TransRegCalculationTool.calculate(var);

        assertEquals(expResult_JanNov, result.get(0).getTsData());
        assertEquals(expResult_Dec, result.get(1).getTsData());

        //ii) 1 Group
        TsData expResult = new TsData(start, d1, true);

        // Settings
        groups = new GroupsEnum[]{GroupsEnum.Group1, GroupsEnum.Group1, GroupsEnum.Group1,
            GroupsEnum.Group1, GroupsEnum.Group1, GroupsEnum.Group1,
            GroupsEnum.Group1, GroupsEnum.Group1, GroupsEnum.Group1,
            GroupsEnum.Group1, GroupsEnum.Group1, GroupsEnum.Group1};

        var.getSettings().getGroups().setGroups(groups);
        var.getSettings().getGroups().setEnabled(true);

        result = TransRegCalculationTool.calculate(var);

        assertEquals(expResult, result.get(0).getTsData());

        //iii) max groups
        // TODO
    }

    @Test
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

}
