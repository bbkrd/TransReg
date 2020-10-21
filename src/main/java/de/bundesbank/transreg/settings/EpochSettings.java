package de.bundesbank.transreg.settings;

import de.bundesbank.transreg.util.Epoch;
import de.bundesbank.transreg.util.DefaultValueEnum;
import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.information.InformationSetSerializable;
import ec.tstoolkit.timeseries.Day;
import java.util.ArrayList;

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
        defaultValue = DefaultValueEnum.ZERO;
        activeEpochs = new Epoch[1];
        activeEpochs[0] = new Epoch();
    }

    public EpochSettings(Day start, Day end) {
        this.enabled = false;
        defaultValue = DefaultValueEnum.ZERO;
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
        this.activeEpochs = epochs;
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

    private static String ENABLED = "enabled", DEFAULTVALUE = "defaultvalue", EPOCHS = "epochs", EPOCHLENGTH = "epochlength";

    @Override
    public InformationSet write(boolean verbose) {
        InformationSet info = new InformationSet();
        info.add(EPOCHLENGTH, activeEpochs.length);
        if (activeEpochs.length != 0) {
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
                activeEpochs[i] = info.get(EPOCHS + i, Epoch.class);
            }
        }
        return true;
    }
}
