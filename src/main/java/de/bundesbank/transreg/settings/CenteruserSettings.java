/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.settings;

import de.bundesbank.transreg.util.CenteruserEnum;
import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.information.InformationSetSerializable;
import ec.tstoolkit.timeseries.PeriodSelectorType;
import ec.tstoolkit.timeseries.TsPeriodSelector;

/**
 *
 * @author s4504gn
 */
public class CenteruserSettings implements InformationSetSerializable {

    private TsPeriodSelector span;
    private CenteruserEnum method;

    public CenteruserSettings() {
        //Defaults:
        span = new TsPeriodSelector();
        method = CenteruserEnum.Seasonal;
    }

    public CenteruserSettings(CenteruserEnum method, TsPeriodSelector span) {
        this.span = span;
        this.method = method;
    }

    public TsPeriodSelector getSpan() {
        return span;
    }

    public void setSpan(TsPeriodSelector sel) {
        this.span = sel;
    }

    public CenteruserEnum getMethod() {
        return method;
    }

    public void setMethod(CenteruserEnum method) {
        this.method = method;
    }

    public boolean isDefault() {
        if(!method.equals(CenteruserEnum.Seasonal)){
            return false;
        }
        return span.getType().equals(PeriodSelectorType.All);
    }

    public CenteruserSettings copy() {

        CenteruserSettings copy = new CenteruserSettings();
        copy.setMethod(method);
        copy.setSpan(span.clone());

        return copy;
    }

    public String getInfo() {
        return "Centeruser method: " + method.toString();
    }

    @Override
    public InformationSet write(boolean verbose) {
        InformationSet info = new InformationSet();
        info.add(METHOD, method.toString());
        //TODO: Span
        info.add(SPAN, span);
        
        return info;
    }

    @Override
    public boolean read(InformationSet info) {
        method = CenteruserEnum.valueOf(info.get(METHOD, String.class));   
        span = info.get(SPAN, TsPeriodSelector.class);
        return true;
    }

    private static String METHOD = "method", SPAN = "span";

    public boolean isEnabled() {
        return !method.equals(CenteruserEnum.None);
    }
}
