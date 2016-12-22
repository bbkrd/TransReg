/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.settings;

import de.bundesbank.transreg.util.CenteruserEnum;
import ec.tstoolkit.timeseries.PeriodSelectorType;
import ec.tstoolkit.timeseries.TsPeriodSelector;

/**
 *
 * @author s4504gn
 */
public class CenteruserSettings {

    TsPeriodSelector span;
    CenteruserEnum method;

    public CenteruserSettings() {
        //Defaults:
        span = new TsPeriodSelector();
        method = CenteruserEnum.None;
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
        if (method.equals(CenteruserEnum.None) && span.getType().equals(PeriodSelectorType.All)) {
            return true;
        }
        return false;
    }
    
    public CenteruserSettings copy(){
        
        CenteruserSettings copy = new CenteruserSettings();
        copy.setMethod(method);
        copy.setSpan(span.clone());
        
        return copy;
    }
}
