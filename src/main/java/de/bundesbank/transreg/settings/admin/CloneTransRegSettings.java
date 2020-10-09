/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.settings.admin;

import de.bundesbank.transreg.settings.TransRegSettings;
import ec.nbdemetra.ws.IWorkspaceItemManager;
import ec.nbdemetra.ws.WorkspaceFactory;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.nodes.WsNode;
import ec.tstoolkit.algorithm.IProcSpecification;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Tools",
        id = "de.bundesbank.transreg.settings.admin.CloneTransRegSettings")
@ActionRegistration(displayName = "#CTL_CloneTransRegSettings")
@ActionReferences({
    @ActionReference(path = TransRegSettingManager.ITEMPATH, position = 1700),})
@Messages("CTL_CloneTransRegSettings=Clone")
public final class CloneTransRegSettings implements ActionListener {

    private final WsNode context;

    public CloneTransRegSettings(WsNode context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        WorkspaceItem<TransRegSettings> xdoc
                = context.getWorkspace().searchDocument(context.lookup(), TransRegSettings.class);
        if (xdoc == null) {
            return;
        }
        IWorkspaceItemManager mgr = WorkspaceFactory.getInstance().getManager(xdoc.getFamily());
        WorkspaceItem<TransRegSettings> ndoc = WorkspaceItem.newItem(xdoc.getFamily(), mgr.getNextItemName(null), ((TransRegSettings) xdoc.getElement()).copy());
        context.getWorkspace().add(ndoc);
    }
}
