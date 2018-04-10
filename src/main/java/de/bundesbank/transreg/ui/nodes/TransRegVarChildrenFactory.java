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
        if(children.isEmpty()){
            return true;
        }
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
