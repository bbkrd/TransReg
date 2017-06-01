/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        set.put(modelPropertySetter.getActiveProperty());
        set.put(modelPropertySetter.getLevelProperty());
        set.put(modelPropertySetter.getFreqProperty());
        set.put(modelPropertySetter.getTimespanProperty());
//        set.put(modelPropertySetter.getDataProperty());
        set.put(modelPropertySetter.getCenteruserTestProperty());
        set.put(modelPropertySetter.getTimestampProperty());
        set.put(modelPropertySetter.getCalculationspanProperty());
        set.put(modelPropertySetter.getCalculatedMeanProperty());

        return set;
    }

}
