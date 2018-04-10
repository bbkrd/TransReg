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
