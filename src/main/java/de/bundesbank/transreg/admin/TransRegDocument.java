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
package de.bundesbank.transreg.admin;

import de.bundesbank.transreg.logic.TransRegVar;
import de.bundesbank.transreg.settings.TransRegSettings;
import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.utilities.DefaultNameValidator;

/**
 *
 * @author Nina Gonschorreck
 */
public class TransRegDocument extends TsVariables{

    private TransRegSettings specification = TransRegSettings.DEFAULT;
    
    public TransRegDocument() {
        super("reg_", new DefaultNameValidator(""));
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
    
    public void setSpecification(TransRegSettings settings){
        specification = settings.copy();
    }

    public TransRegSettings getSpecification() {
        return specification;
    }
    
    
}
