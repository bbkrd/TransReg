/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.admin;

import de.bundesbank.transreg.logic.TransRegVar;
import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.information.InformationSetSerializable;
import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.utilities.DefaultNameValidator;

/**
 *
 * @author s4504gn
 */
public class TransRegDocument extends TsVariables implements InformationSetSerializable {

    public TransRegDocument() {
        super("reg_", new DefaultNameValidator(".+-*/"));
    }

    @Override
    public InformationSet write(boolean verbose) {
        TransRegVar.register();
        return super.write(verbose);
    }

    @Override
    public boolean read(InformationSet info) {
        TransRegVar.register();
        return super.read(info);
    }
}
