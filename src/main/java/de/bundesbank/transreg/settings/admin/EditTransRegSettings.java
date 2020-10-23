/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.settings.admin;

import de.bundesbank.transreg.settings.TransRegSettings;
import de.bundesbank.transreg.ui.propertyEditor.TransRegSettingsUI;
import ec.nbdemetra.ui.properties.l2fprod.PropertiesDialog;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.nodes.WsNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;

@ActionID(category = "Tools",
        id = "ec.bundesbank.transreg.settings.admin.EditTransRegSettings")
@ActionRegistration(displayName = "#CTL_EditTransRegSettings")
@ActionReferences({
    @ActionReference(path = TransRegSettingManager.ITEMPATH, position = 1000, separatorAfter = 1090)
})
@NbBundle.Messages("CTL_EditTransRegSettings=Open")
public class EditTransRegSettings implements ActionListener {

    private final WsNode context;

    public EditTransRegSettings(WsNode context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        EditTransRegSettings();
    }

    private void EditTransRegSettings() {
        final WorkspaceItem<TransRegSettings> xdoc = context.getWorkspace().searchDocument(context.lookup(), TransRegSettings.class);
        if (xdoc == null || xdoc.getElement() == null) {
            return;

        }
        
        final TransRegSettingsUI ui = new TransRegSettingsUI(xdoc.getElement().copy(), xdoc.isReadOnly());
        PropertiesDialog propDialog =
                new PropertiesDialog(WindowManager.getDefault().getMainWindow(), true, ui,
                new AbstractAction("OK") {
            @Override
            public void actionPerformed(ActionEvent e) {
                xdoc.setElement(ui.getCore());
            }
        });
        propDialog.setTitle(xdoc.getDisplayName());
        propDialog.setVisible(true);
    }
}
