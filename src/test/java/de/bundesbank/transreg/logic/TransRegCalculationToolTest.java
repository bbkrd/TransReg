/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.logic;

import de.bundesbank.transreg.util.CenteruserEnum;
import ec.tstoolkit.timeseries.Day;
import ec.tstoolkit.timeseries.Month;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.timeseries.simplets.TsPeriod;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
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
    public void testTestCenteruser() {

        Set<String> list = new HashSet();
        
        String s1 = "Banane";
        String s2 = "Apfel";
        
        list.add(s1);
        list.add(s2);
        s1 = s1.toUpperCase();
//        list.add(s1);
        
        System.out.println("Anzahl elemente: "+list.size());
        list.stream().forEach((s) -> {
            System.out.println(s);
        });//        System.out.println("testCenteruser");
//        TsData data = null;
//        String expResult = "";
//        String result = TransRegCalculationTool.testCenteruser(data);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    }

    @Test
    public void testCalculateCenteruser() {

        double[] d1 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        TsPeriod start = new TsPeriod(TsFrequency.Monthly, 1999, 0);

        TsData inputData = new TsData(start, d1, true);

        TsData result;
        TsData expResult;

        // 1. centeruser = mean
        // i) type = all
        TransRegVar v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Mean);
        result = TransRegCalculationTool.calculateCenteruser(v);

        // expected
        double[] r = new double[]{-5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result);

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
        result = TransRegCalculationTool.calculateCenteruser(v);

        r = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result);

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
        result = TransRegCalculationTool.calculateCenteruser(v);

        r = new double[]{-5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result);

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
        result = TransRegCalculationTool.calculateCenteruser(v);

        r = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            -5.5, -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result);

        // 2. centeruser = seasonal 
        // i) type = all 
        v = new TransRegVar(inputData);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);
        result = TransRegCalculationTool.calculateCenteruser(v);

        //expected
        r = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result);

        // ii)type = from
        v = new TransRegVar(inputData2);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);
        v.getSettings().getCenteruser().getSpan().from(new Day(2000, Month.January, 0));
        result = TransRegCalculationTool.calculateCenteruser(v);

        r = new double[]{5.5, 4.5, 3.5, 2.5, 1.5, 0.5, -0.5, -1.5, -2.5, -3.5, -4.5, -5.5,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result);

        // iii) type = to
        v = new TransRegVar(inputData3);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);
        v.getSettings().getCenteruser().getSpan().to(new Day(2003, Month.December, 30));
        result = TransRegCalculationTool.calculateCenteruser(v);

        r = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            5.5, 4.5, 3.5, 2.5, 1.5, 0.5, -0.5, -1.5, -2.5, -3.5, -4.5, -5.5};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result);

        // iv) type = between
        v = new TransRegVar(inputData4);
        v.getSettings().getCenteruser().setMethod(CenteruserEnum.Seasonal);
        v.getSettings().getCenteruser().getSpan().between(new Day(2000, Month.January, 0), new Day(2003, Month.December, 30));
        result = TransRegCalculationTool.calculateCenteruser(v);

        r = new double[]{5.5, 4.5, 3.5, 2.5, 1.5, 0.5, -0.5, -1.5, -2.5, -3.5, -4.5, -5.5,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            5.5, 4.5, 3.5, 2.5, 1.5, 0.5, -0.5, -1.5, -2.5, -3.5, -4.5, -5.5};

        expResult = new TsData(start, r, true);

        assertEquals(expResult, result);

//        System.out.println("calculateCenteruser");
//        TransRegVar var = null;
//        TsData expResult = null;
//        TsData result = TransRegCalculationTool.calculateCenteruser(var);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    }

}
