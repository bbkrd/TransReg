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
import ec.nbdemetra.ui.notification.MessageType;
import ec.nbdemetra.ui.notification.NotifyUtil;
import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.timeseries.regression.ITsVariable;
import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.utilities.DefaultNameValidator;
import ec.tstoolkit.utilities.IDynamicObject;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 *
 * @author Nina Gonschorreck
 */
public class TransRegDocument extends TsVariables implements IDynamicObject {

    private TransRegSettings specification = TransRegSettings.DEFAULT;
    private boolean dirty = false;

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

    public void setSpecification(TransRegSettings settings) {
        specification = settings.copy();
    }

    public TransRegSettings getSpecification() {
        return specification;
    }

    @Override
    public boolean refresh() {
        Collection<ITsVariable> vars = variables();

        String notUpdated = vars.stream()
                .filter(var -> (var instanceof TransRegVar))
                .filter(var -> !((TransRegVar) var).refresh())
                .map(var -> var.getName())
                .collect(Collectors.joining("; "));

        if (!notUpdated.isEmpty()) {
            NotifyUtil.show("Warning",
                    "Could not update the following variables: " + notUpdated,
                    MessageType.WARNING);
        }

        dirty = true;
        return true;
    }

    @Override
    public void resetDirty() {
        super.resetDirty();
        dirty = false;
    }

    @Override
    public boolean isDirty() {
        return super.isDirty() || dirty;
    }

}
