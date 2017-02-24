/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        set.put(modelPropertySetter.getAppearanceProperty());

        sheet.put(set);
        
        return sheet;
    }
}
