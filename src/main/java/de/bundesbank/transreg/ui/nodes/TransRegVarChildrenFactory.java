/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui.nodes;

import de.bundesbank.transreg.logic.TransRegVar;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author s4504gn
 */
public class TransRegVarChildrenFactory extends ChildFactory.Detachable<TransRegVar> implements PropertyChangeListener {

    private List<TransRegVar> children;

    public TransRegVarChildrenFactory(List<TransRegVar> children) {
        this.children = children;
    }

    @Override
    protected boolean createKeys(List<TransRegVar> list) {
        return list.addAll(children);
    }

    @Override
    protected Node createNodeForKey(TransRegVar v) {
        v.addPropertyChangeListener(this);
        if (v.hasChildren()) {
            return new TransRegVarNode(v);
        }               
        return new TransRegVarLeaf(v);
    }

    @Override
    protected void removeNotify() {
        for (TransRegVar v : children) {
            v.removePropertyChangeListener(this);
        }
        this.children = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

}
