/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.actions;

import de.bundesbank.transreg.admin.TransRegDocumentManager;
import ec.nbdemetra.ui.nodes.SingleNodeAction;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.nodes.ItemWsNode;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "TransReg",
        id = "de.bundesbank.transreg.actions.OpenAction"
)
@ActionRegistration(
        displayName = "#CTL_OpenAction", lazy = false
)
@ActionReferences({
    @ActionReference(path = TransRegDocumentManager.ITEMPATH, position = 1700, separatorBefore = 1699)
})
@Messages("CTL_OpenAction=Open")
public final class OpenAction extends SingleNodeAction<ItemWsNode>  {

    public OpenAction() {
        super(ItemWsNode.class);
    }
    
     @Override
    protected boolean enable(ItemWsNode context) {
        WorkspaceItem<?> cur = context.getItem();
        return cur != null;
    }
    
     @Override
    protected void performAction(ItemWsNode context) {
    }
    
    @Override
    public String getName() {
        return Bundle.CTL_OpenAction();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }
}
