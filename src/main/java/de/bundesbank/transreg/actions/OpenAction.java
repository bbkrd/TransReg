/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.actions;

import de.bundesbank.transreg.admin.TransRegDocument;
import de.bundesbank.transreg.admin.TransRegDocumentManager;
import ec.nbdemetra.ws.WorkspaceFactory;
import ec.nbdemetra.ws.WorkspaceItem;
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
        id = "de.bundesbank.transreg.actions.OpenAction"
)
@ActionRegistration(
        displayName = "#CTL_OpenAction")
@ActionReferences({
    @ActionReference(path = TransRegDocumentManager.ITEMPATH, position = 1700, separatorBefore = 1699)
})
@Messages("CTL_OpenAction=Open")
public final class OpenAction implements ActionListener {

    private final WsNode context;
    
    public OpenAction(WsNode context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        WorkspaceItem<TransRegDocument> doc = context.getWorkspace().searchDocument(context.lookup(), TransRegDocument.class);
        TransRegDocumentManager mgr = WorkspaceFactory.getInstance().getManager(TransRegDocumentManager.class);
        mgr.openDocument(doc);
    }
}
