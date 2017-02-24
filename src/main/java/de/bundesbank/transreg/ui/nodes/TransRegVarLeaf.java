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
