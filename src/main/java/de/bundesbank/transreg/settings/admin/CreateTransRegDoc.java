/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.settings.admin;

import de.bundesbank.transreg.settings.TransRegSettings;
import ec.nbdemetra.ws.WorkspaceFactory;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.nodes.WsNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Tools",
id = "de.bundesbank.transreg.settings.admin.CreateTransRegDoc")
@ActionRegistration(displayName = "#CTL_CreateTransRegDoc")
@ActionReferences({
    @ActionReference(path = TransRegSettingManager.ITEMPATH, position = 1610, separatorBefore = 1300)
})
@Messages("CTL_CreateTransRegDoc=Create Document")
public final class CreateTransRegDoc implements ActionListener {

    private final WsNode context;

    public CreateTransRegDoc(WsNode context) {
        this.context = context;
    }
    
    @Override
    public void actionPerformed(ActionEvent ev) {
        final WorkspaceItem<TransRegSettings> xdoc = context.getWorkspace().searchDocument(context.lookup(), TransRegSettings.class);
         if (xdoc == null||xdoc.getElement() == null) {
            return;
        }
        TransRegSettingManager mgr = (TransRegSettingManager) WorkspaceFactory.getInstance().getManager(xdoc.getFamily());
        if (mgr != null) {
            mgr.createDocument(context.getWorkspace(), xdoc);
        }
    }
}
