/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.admin;

import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.utilities.DefaultNameValidator;

/**
 *
 * @author s4504gn
 */
public class TransRegDocument extends TsVariables{

    
    public TransRegDocument(){
        super("t", new DefaultNameValidator(".+-*/"));
//        regressors = new TsVariables("t", new DefaultNameValidator(",=+-"));
    }
        
    // TODO: um aus WS zu laden und zu specichern, read und write

    
}
