package de.bundesbank.transreg.settings;

import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.information.InformationSetSerializable;

/**
 *
 * @author Nina Gonschorreck
 */
public class LeadLagSettings implements InformationSetSerializable {

    private int nPeriods;

    /**
     * Defaults
     */
    public LeadLagSettings() {
        nPeriods = 0;
    }

    public int getnPeriods() {
        return nPeriods;
    }

    public void setnPeriods(int nPeriods) {
        this.nPeriods = nPeriods;
    }

    public boolean isDefault() {
        return nPeriods == 0;
    }

    public LeadLagSettings copy() {
        LeadLagSettings copy = new LeadLagSettings();
        copy.setnPeriods(nPeriods);

        return copy;
    }

    private static final String NPERIODS = "nperiods";

    @Override
    public InformationSet write(boolean verbose) {
        InformationSet info = new InformationSet();
        info.add(NPERIODS, nPeriods);

        return info;
    }

    @Override
    public boolean read(InformationSet info) {
        Integer i = info.get(NPERIODS, Integer.class);
        if (i != null) {
            nPeriods = i;
        }

        return true;
    }

}
