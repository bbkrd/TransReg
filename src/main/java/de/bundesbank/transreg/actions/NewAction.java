/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.actions;

import de.bundesbank.transreg.admin.TransRegDocumentManager;
import ec.nbdemetra.ws.IWorkspaceItemManager;
import ec.nbdemetra.ws.Workspace;
import ec.nbdemetra.ws.WorkspaceFactory;
import ec.nbdemetra.ws.nodes.WsNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "TransReg",
        id = "de.bundesbank.transreg.actions.NewAction"
)
@ActionRegistration(
        displayName = "#CTL_NewAction"
)
@ActionReferences({
    @ActionReference(path = TransRegDocumentManager.PATH, position = 1000)
})
@Messages("CTL_NewAction=New")
public final class NewAction implements ActionListener {

    private final WsNode context;

    public NewAction(WsNode context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        IWorkspaceItemManager mgr = WorkspaceFactory.getInstance().getManager(context.lookup());
        if (mgr != null) {
            Workspace ws = context.getWorkspace();
            mgr.create(ws);
        }
    }
}
