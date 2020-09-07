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

    private static String ENABLED = "enabled";

    @Override
    public InformationSet write(boolean verbose) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates. //ToDo Nina - 01: Warum sind read und write nicht ausprogrammiert
    }

    @Override
    public boolean read(InformationSet info) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
