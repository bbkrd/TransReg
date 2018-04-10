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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author s4504gn
 */
public class TransRegVarLeaf extends TransRegVarNodeAdapter {

//    private Sheet updatedSheet;

    public TransRegVarLeaf(TransRegVar var) {
        super(Children.LEAF, Lookups.singleton(var));
//        content.add(var);
        setName(var.getName());
//        updatedSheet = createSheet();  // needs to be set here, because lookup is null in createSheet at startup
        var.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setSheet(updatedSheet);
            }
        });
    }

    @Override
    protected final Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = super.createDefaultSetter();
        sheet.put(set);
        return sheet;
    }

//    public TransRegVar getVar() {
//        TransRegVar v = getLookup().lookup(TransRegVar.class);
//        return v;
//    }
}
