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
package de.bundesbank.transreg.ui.nodes;

import de.bundesbank.transreg.logic.TransRegVar;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

/**
 *
 * @author s4504gn
 */
public class TransRegVarNodeAdapter extends AbstractNode {

    protected Sheet updatedSheet;

    public TransRegVarNodeAdapter(Children children, Lookup lookup) {
        super(children, lookup);
    }

    protected Sheet.Set createDefaultSetter() {
        Sheet.Set set = Sheet.createPropertiesSet();

        TransRegVar model = getLookup().lookup(TransRegVar.class);
        TransRegVarPropertySetter modelPropertySetter = new TransRegVarPropertySetter(model);
        set.put(modelPropertySetter.getLevelProperty());
        set.put(modelPropertySetter.getFreqProperty());
        set.put(modelPropertySetter.getTimespanProperty());
        set.put(modelPropertySetter.getCenteruserTestProperty());
        set.put(modelPropertySetter.getTimestampProperty());
        set.put(modelPropertySetter.getCalculationspanProperty());
        set.put(modelPropertySetter.getCalculatedMeanProperty());

        return set;
    }

}
