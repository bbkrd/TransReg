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
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author s4504gn
 */
public class TransRegVarNode extends TransRegVarNodeAdapter {

//    protected Sheet updatedSheet;
    public TransRegVarNode(TransRegVar var) {
        this(var, new InstanceContent());

    }

    private TransRegVarNode(TransRegVar var, InstanceContent content) {
        super(Children.create(
                new TransRegVarChildrenFactory(var.getChildren()), false),
                new AbstractLookup(content));
        content.add(var);
        setName(var.getName());
        updatedSheet = createSheet();  // needs to be set here, because lookup is null in createSheet at startup
        var.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setSheet(updatedSheet);
            }
        });
    }

    @Override
    protected final Sheet createSheet() {

        TransRegVar model = getLookup().lookup(TransRegVar.class);
        TransRegVarPropertySetter modelPropertySetter = new TransRegVarPropertySetter(model);

        Sheet sheet = super.createSheet();
        Sheet.Set set = super.createDefaultSetter();
        set.put(modelPropertySetter.getAppearanceProperty());
//        set.put(modelPropertySetter.getAppearanceProperty());

        sheet.put(set);
        
        return sheet;
    }
}
