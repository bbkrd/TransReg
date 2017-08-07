/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.actions;

import de.bundesbank.transreg.admin.TransRegDocument;
import de.bundesbank.transreg.admin.TransRegDocumentManager;
import de.bundesbank.transreg.logic.TransRegVar;
import de.bundesbank.transreg.ui.TransRegTopComponent;
import ec.nbdemetra.ui.nodes.SingleNodeAction;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.nodes.ItemWsNode;
import ec.tstoolkit.timeseries.regression.ITsVariable;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "TransReg",
        id = "de.bundesbank.transreg.actions.RefreshAction"
)
@ActionRegistration(
        displayName = "#CTL_RefreshAction", lazy = false
)
@ActionReferences({
    @ActionReference(path = TransRegDocumentManager.ITEMPATH, position = 1700, separatorBefore = 1699)
})
@Messages("CTL_RefreshAction=Refresh")
public final class RefreshAction extends SingleNodeAction<ItemWsNode> {

    public RefreshAction() {
        super(ItemWsNode.class);
    }

    @Override
    protected void performAction(ItemWsNode context) {
        WorkspaceItem<TransRegDocument> cur = (WorkspaceItem<TransRegDocument>) context.getItem();
        if (cur != null && !cur.isReadOnly()) {
            Collection<ITsVariable> vars = cur.getElement().variables();

            ArrayList<ITsVariable> delete = new ArrayList<>();
            vars.stream().filter((var) -> (var instanceof TransRegVar)).forEach((var) -> {
                boolean refreshed = ((TransRegVar) var).refresh();
                if (!refreshed) {
                    delete.add(var);
                }
            });

            for(ITsVariable var : delete){
                vars.remove(var);
                JOptionPane.showMessageDialog(null, 
                        "Variable "+((TransRegVar)var).getName()+ " is deleted.", 
                        "Warning", 
                        JOptionPane.WARNING_MESSAGE);
            }
            
            ((TransRegTopComponent)cur.getView()).refresh();
        }
    }

    @Override
    protected boolean enable(ItemWsNode context) {
        WorkspaceItem<?> cur = context.getItem();
        return cur != null && !cur.isReadOnly();
    }

    @Override
    public String getName() {
        return Bundle.CTL_RefreshAction();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }
}
