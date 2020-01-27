package de.bundesbank.transreg.settings;

import de.bundesbank.transreg.util.LeadLagEnum;
import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.information.InformationSetSerializable;

/**
 *
 * @author Nina Gonschorreck
 */


public class LeadLagSettings implements InformationSetSerializable{

    private boolean enabled;
    private int nPeriods;
    private LeadLagEnum method;
    
    /**
     * Defaults
    */
    public LeadLagSettings(){
        enabled = false;
        nPeriods=0;
        method = LeadLagEnum.Lead;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getnPeriods() {
        return nPeriods;
    }

    public void setnPeriods(int nPeriods) {
        this.nPeriods = nPeriods;
    }

    public LeadLagEnum getMethod() {
        return method;
    }

    public void setMethod(LeadLagEnum method) {
        this.method = method;
    }
    
    public boolean isDefault(){
        return !this.enabled;
    }
    
    public LeadLagSettings copy(){
        LeadLagSettings copy = new LeadLagSettings();
        copy.setEnabled(enabled);
        copy.setMethod(method);
        copy.setnPeriods(nPeriods);
        
        return copy;
    }
    
    private static String ENABLED = "enabled", METHOD = "method", NPERIODS = "nperiods";
    
    @Override
    public InformationSet write(boolean verbose) {
        InformationSet info = new InformationSet();
        info.add(ENABLED, enabled);
        info.add(METHOD, method.toString());
        info.add(NPERIODS, nPeriods);
        
        return info;
    }

    @Override
    public boolean read(InformationSet info) {
        enabled = info.get(ENABLED, Boolean.class);
        method = LeadLagEnum.valueOf(info.get(METHOD, String.class));
        nPeriods = info.get(NPERIODS, Integer.class);
        
        return true;
    }
    
}
