/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.settings.admin;

import static de.bundesbank.transreg.actions.RenameAction.NAME_MESSAGE;
import static de.bundesbank.transreg.actions.RenameAction.RENAME_TITLE;
import de.bundesbank.transreg.settings.TransRegSettings;
import ec.nbdemetra.ws.IWorkspaceItemManager;
import ec.nbdemetra.ws.WorkspaceFactory;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.nodes.WsNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Tools",
        id = "de.bundesbank.transreg.settings.admin.RenameTransRegSettings")
@ActionRegistration(displayName = "#CTL_RenameTransRegSettings")
@ActionReferences({
    @ActionReference(path = TransRegSettingManager.ITEMPATH, position = 1700),})
@Messages("CTL_RenameTransRegSettings=Rename")
public final class RenameTransRegSettings implements ActionListener {

    private final WsNode context;

    public RenameTransRegSettings(WsNode context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        WorkspaceItem<TransRegSettings> cur
                = context.getWorkspace().searchDocument(context.lookup(), TransRegSettings.class);

        if (cur != null && !cur.isReadOnly()) {
            // create the input dialog
            String oldName = cur.getDisplayName(), newName;
            TransRegName nd = new TransRegName(NAME_MESSAGE, RENAME_TITLE, oldName);

            if (DialogDisplayer.getDefault().notify(nd) != NotifyDescriptor.OK_OPTION) {
                return;
            }
            newName = nd.getInputText();
            if (!newName.equals(oldName)) {              
                context.getWorkspace().rename(cur, newName);
                cur.getElement().setSpecificationName(newName);
            }
        }
    }
}

class TransRegName extends NotifyDescriptor.InputLine {

    TransRegName(String title, String text, String oldName) {
        super(title, text);
        setInputText(oldName);
    }
}
