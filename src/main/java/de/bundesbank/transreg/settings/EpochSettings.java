package de.bundesbank.transreg.settings;

import de.bundesbank.transreg.util.Epoch;
import de.bundesbank.transreg.util.DefaultValueEnum;
import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.information.InformationSetSerializable;
import ec.tstoolkit.timeseries.Day;
import javax.swing.JOptionPane;

/**
 *
 * @author Nina Gonschorreck
 */
public class EpochSettings implements InformationSetSerializable {
    
    private boolean enabled;
    private Epoch[] activeEpochs;
    private DefaultValueEnum defaultValue = DefaultValueEnum.ZERO;
    
    public EpochSettings() {
        this.enabled = false;
//        defaultValue = DefaultValueEnum.ZERO;
        activeEpochs = new Epoch[1];
        activeEpochs[0] = new Epoch();
    }
    
    public EpochSettings(Day start, Day end) {
        this.enabled = false;
//        defaultValue = DefaultValueEnum.ZERO;
        activeEpochs = new Epoch[1];
        activeEpochs[0] = new Epoch(start, end);
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public Epoch[] getEpochs() {
        return activeEpochs;
    }
    
    public void setEpochs(Epoch[] epochs) {
        if (checkEpochs(epochs)) {
            this.activeEpochs = epochs;
        }else{
             JOptionPane.showMessageDialog(null, "Overlapping regimes.", "Error", JOptionPane.WARNING_MESSAGE); 
        }
    }
    
    private boolean checkEpochs(Epoch[] epochs) {
        boolean isOkay = true;
        for (int i = 0; i < epochs.length - 1; i++) {
            Epoch epoch_1 = epochs[i];
            for (int j = 1; j < epochs.length; j++) {
                Epoch epoch_2 = epochs[j];
                if (epoch_1.getEnd().isAfter(epoch_2.getStart())) {
                    isOkay = false;
                }
            }
        }
        return isOkay;
    }
    
    public DefaultValueEnum getDefaultValue() {
        return defaultValue;
    }
    
    public void setDefaultValue(DefaultValueEnum defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public boolean isDefault() {
        return !isEnabled(); //|| activeEpochs.isEmpty();
    }
    
    public EpochSettings copy() {
        EpochSettings copy = new EpochSettings();
        copy.setEnabled(enabled);
        if (activeEpochs != null) {//&& !activeEpochs.isEmpty()){
            copy.setEpochs(activeEpochs);
        }
        copy.setDefaultValue(defaultValue);
        
        return copy;
    }
    
    private static String ENABLED = "enabled",
            DEFAULTVALUE = "defaultvalue",
            EPOCHS = "epochs",
            EPOCHLENGTH = "epochlength";
    
    @Override
    public InformationSet write(boolean verbose) {
        InformationSet info = new InformationSet();
        if (activeEpochs != null && activeEpochs.length != 0) {
            info.add(EPOCHLENGTH, activeEpochs.length);
            for (int i = 0; i < activeEpochs.length; i++) {
                info.add(EPOCHS + i, activeEpochs[i].toString());
            }
        }
        info.add(ENABLED, enabled);
        info.add(DEFAULTVALUE, defaultValue);
        
        return info;
    }
    
    @Override
    public boolean read(InformationSet info) {
        enabled = info.get(ENABLED, Boolean.class);
        defaultValue = DefaultValueEnum.fromString(info.get(DEFAULTVALUE, String.class));
        Integer length = info.get(EPOCHLENGTH, Integer.class);
        if (length != null && length != 0) {
            activeEpochs = new Epoch[length];
            for (int i = 0; i < length; i++) {
                String epoch = info.get(EPOCHS + i, String.class);
                activeEpochs[i] = Epoch.fromString(epoch);
            }
        }
        return true;
    }
}
