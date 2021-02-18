package de.bundesbank.transreg.settings.admin;

import de.bundesbank.transreg.admin.TransRegDocument;
import de.bundesbank.transreg.admin.TransRegDocumentManager;
import de.bundesbank.transreg.settings.TransRegSettings;
import de.bundesbank.transreg.ui.TransRegTopComponent;
import ec.nbdemetra.ws.AbstractWorkspaceItemManager;
import ec.nbdemetra.ws.IWorkspaceItemManager;
import ec.nbdemetra.ws.Workspace;
import ec.nbdemetra.ws.WorkspaceFactory;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.nodes.ItemWsNode;
import ec.tstoolkit.utilities.Id;
import ec.tstoolkit.utilities.LinearId;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Nina Gonschorreck
 */

@ServiceProvider(service = IWorkspaceItemManager.class,
position = 1020)
public class TransRegSettingManager extends AbstractWorkspaceItemManager<TransRegSettings>{
    
    public static final LinearId ID = new LinearId("TransReg", "specifications"); 
    public static final String PATH = "transreg.spec";
    public static final String ITEMPATH = "transreg.spec.item";

    @Override
    protected String getItemPrefix() {
        return "TransRegSpecification";
    }

    @Override
    public Id getId() {
        return ID;
    }

    @Override
    protected TransRegSettings createNewObject() {
        return TransRegSettings.DEFAULT.copy();
    }

    @Override
    public ItemType getItemType() {
        return ItemType.Spec;
    }

    @Override
    public String getActionsPath() {
        return PATH;
    }

    @Override
    public Status getStatus() {
        return Status.Certified;
    }

    @Override
    public Action getPreferredItemAction(Id child) {
        ItemWsNode tmp = new ItemWsNode(WorkspaceFactory.getInstance().getActiveWorkspace(), child);
        final EditTransRegSettings obj = new EditTransRegSettings(tmp);
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                obj.actionPerformed(e);
            }
        };
    }

    @Override
    public List<WorkspaceItem<TransRegSettings>> getDefaultItems() {
        List<WorkspaceItem<TransRegSettings>> result = new ArrayList<>();
        result.add(WorkspaceItem.system(ID, "Default", TransRegSettings.DEFAULT));
        return result;
    }

    public void createDocument(final Workspace ws, final WorkspaceItem<TransRegSettings> xdoc) {
        TransRegDocumentManager dmgr = (TransRegDocumentManager) WorkspaceFactory.getInstance().getManager(TransRegDocumentManager.ID);
        WorkspaceItem<TransRegDocument> doc = (WorkspaceItem<TransRegDocument>) dmgr.create(ws);
        doc.getElement().setSpecification(xdoc.getElement()); //<-Todo
        doc.setComments(xdoc.getComments());
        TransRegTopComponent view = new TransRegTopComponent(doc);
        view.open();
        view.requestActive();
    }

    @Override
    public Class<TransRegSettings> getItemClass() {
        return TransRegSettings.class;
    }

    @Override
    public Icon getManagerIcon() {
        return ImageUtilities.loadImageIcon("ec/nbdemetra/sa/blog_16x16.png", false);
    }
}
