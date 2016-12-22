/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.logic;

import de.bundesbank.transreg.settings.TransRegSettings;
import ec.tss.Ts;
import ec.tss.TsFactory;
import ec.tss.TsInformationType;
import ec.tss.TsMoniker;
import ec.tss.TsStatus;
import ec.tstoolkit.data.DataBlock;
import ec.tstoolkit.information.InformationConverter;
import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.timeseries.regression.TsVariable;
import static ec.tstoolkit.timeseries.regression.TsVariables.LINKER;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.utilities.IDynamicObject;

/**
 *
 * @author s4504gn
 */
public class TransRegVar extends TsVariable implements IDynamicObject {

    private TransRegSettings currentSettings; // = new TransRegSettings();
    private TransRegSettings oldSettings; // = new TransRegSettings();
    private TsData calculatedData;
    private final TsMoniker moniker;

    public TransRegVar(TsData d) {
        super(d);
        moniker = null;
        calculatedData = d.clone();
        currentSettings = new TransRegSettings(d.getFrequency().intValue());
        oldSettings = currentSettings.copy();
    }

    public TransRegVar(String s, TsData d) {
        super(s, d);
        moniker = null;
        calculatedData = d.clone();
        currentSettings = new TransRegSettings(d.getFrequency().intValue());
        oldSettings = currentSettings.copy();
    }

    public TransRegVar(String s, TsMoniker m, TsData d) {
        super(s, d);
        calculatedData = d.clone();
        moniker = m;
        currentSettings = new TransRegSettings(d.getFrequency().intValue());
        oldSettings = currentSettings.copy();
    }

    // Fuer Encoding
    private TransRegVar(String s, TsMoniker m, TsData data, TsData calculated, TransRegSettings current, TransRegSettings old) {
        super(s, data);
        moniker = m;
        calculatedData = calculated;
        currentSettings = current;
        oldSettings = old;
    }

    public TsMoniker getMoniker() {
        return moniker;
    }

    @Override
    public TsData getTsData() {
        return calculatedData;
    }

    public TsData getOriginalData() {
        return super.getTsData();
    }

    public TransRegSettings getSettings() {
        return currentSettings;
    }

    public void setSettings(TransRegSettings settings) {
        this.oldSettings = currentSettings.copy();
        this.currentSettings = settings;
    }

    public TransRegSettings getOldSettings() {
        return oldSettings;
    }

    @Override
    public boolean refresh() {

        if (this.getMoniker() != null) {
            Ts s = TsFactory.instance.createTs(null, this.getMoniker(), TsInformationType.Data);
            if (s.hasData() == TsStatus.Undefined) {
                s.load(TsInformationType.Data);
            }

            TsData data = s.getTsData();
            if (data == null) {
                return false;
            }
            setData(data);
            calculate();
            return true;
        }
        return false;
    }

    public void calculate() {
        if (!currentSettings.isDefault()) {
            calculatedData = TransRegCalculationTool.calculateCenteruser(this);
        } else {
            // keine Berechnung
            calculatedData = getOriginalData().clone();
        }
    }

    private static class TransRegVarConverter implements InformationConverter<TransRegVar> {

        @Override
        public TransRegVar decode(InformationSet info) {

            TsData data = info.get(DATA, TsData.class);
            String desc = info.get(DESC, String.class);
            TsMoniker monk = info.get(MONIKER, TsMoniker.class);
            TsData original = info.get(ORIGINAL, TsData.class);
            TransRegSettings cur = info.get(SETTINGS, TransRegSettings.class);
            TransRegSettings old = info.get(OLDSETTINGS, TransRegSettings.class);

            TransRegVar result = new TransRegVar(desc, monk, original, data, cur, old);
            return result;
        }

        @Override
        public InformationSet encode(TransRegVar t, boolean verbose) {

            InformationSet info = new InformationSet();
            info.set(DESC, t.getDescription());
            info.set(MONIKER, t.getMoniker());
            info.set(DATA, t.getTsData());
            info.set(ORIGINAL, t.getOriginalData());
            info.set(SETTINGS, t.getSettings());
            info.set(OLDSETTINGS, t.getOldSettings());
            return info;
        }

        @Override
        public Class<TransRegVar> getInformationType() {
            return TransRegVar.class;
        }

        @Override
        public String getTypeDescription() {
            return TYPE;
        }
        static final String TYPE = "transformed time series", OLDSETTINGS = "old settings", SETTINGS = "settings", MONIKER = "moniker", DATA = "data", ORIGINAL = "original data", DESC = "description";
    };

    private static final InformationConverter<TransRegVar> tsvar = new TransRegVarConverter();

    public static void register() {
        LINKER.register(TransRegVarConverter.TYPE, TransRegVar.class, tsvar);
    }
}
