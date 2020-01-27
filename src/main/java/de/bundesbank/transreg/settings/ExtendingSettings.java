package de.bundesbank.transreg.settings;

import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.information.InformationSetSerializable;

/**
 *
 * @author Nina Gonschorreck
 */


public class ExtendingSettings implements InformationSetSerializable{

    private boolean enabled;
    private int nExtending;
 
    public ExtendingSettings(){
        //Defaults
        this.enabled = false;
        this.nExtending = 0;
    }

    public ExtendingSettings(boolean enabled, int end) {
        this.enabled = enabled;
        this.nExtending = end;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getEnd() {
        return nExtending;
    }

    public void setEnd(int end) {
        this.nExtending = end;
    }
    
    public boolean isDefault(){
        return !isEnabled()&&nExtending==0;
    }
    
    public ExtendingSettings copy(){
        ExtendingSettings copy = new ExtendingSettings();
        copy.setEnabled(enabled);
        copy.setEnd(nExtending);
        
        return copy;
    }
    
    @Override
    public InformationSet write(boolean verbose) {
        InformationSet info = new InformationSet();
        info.add(ENABLE, enabled);
        info.add(END, nExtending);
        
        return info;
    }

    @Override
    public boolean read(InformationSet info) {
        enabled = info.get(ENABLE, Boolean.class);
        nExtending = info.get(END, Integer.class);
        
        return true;
    }
    
    private static String ENABLE = "enable", END = "end"; 
    
}
