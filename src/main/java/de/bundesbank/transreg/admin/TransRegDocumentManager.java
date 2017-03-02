/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.admin;

import de.bundesbank.transreg.options.SaveMethodEnum;
import de.bundesbank.transreg.options.TransRegOptionsPanelController;
import de.bundesbank.transreg.ui.TransRegTopComponent;
import ec.nbdemetra.ws.AbstractWorkspaceItemManager;
import ec.nbdemetra.ws.IWorkspaceItemManager;
import ec.nbdemetra.ws.Workspace;
import ec.nbdemetra.ws.WorkspaceFactory;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.tstoolkit.algorithm.ProcessingContext;
import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.utilities.Id;
import ec.tstoolkit.utilities.LinearId;
import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author s4504gn
 */
@ServiceProvider(service = IWorkspaceItemManager.class, position = 8910)
public class TransRegDocumentManager extends AbstractWorkspaceItemManager<TransRegDocument> {

    @Override
    public WorkspaceItem<TransRegDocument> create(Workspace ws) {
        WorkspaceItem<TransRegDocument> nvars = super.create(ws);
        ProcessingContext.getActiveContext().getTsVariableManagers().set(nvars.getDisplayName(), nvars.getElement());
        return nvars;
    }

    public static final LinearId ID = new LinearId("Utilities", "TransReg");
    public static final String PATH = "TransReg";
    public static final String ITEMPATH = "TransReg.item";
    public static final String CONTEXTPATH = "TransReg.context";

    @Override
    protected String getItemPrefix() {
        return "TransReg";
    }

    @Override
    protected TransRegDocument createNewObject() {
        return new TransRegDocument();
    }

    @Override
    public Status getStatus() {
        return IWorkspaceItemManager.Status.Experimental;
    }

    @Override
    public ItemType getItemType() {
        return IWorkspaceItemManager.ItemType.Doc;
    }

    @Override
    public Class<TransRegDocument> getItemClass() {
        return TransRegDocument.class;
    }

    @Override
    public Id getId() {
        return ID;
    }

    @Override
    public String getActionsPath() {
        return PATH;
    }

    @Override
    public Action getPreferredItemAction(final Id child) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WorkspaceItem<TransRegDocument> doc = (WorkspaceItem<TransRegDocument>) WorkspaceFactory.getInstance().getActiveWorkspace().searchDocument(child);
                if (doc != null) {
                    openDocument(doc);
                }
            }
        };
    }

    public void openDocument(WorkspaceItem<TransRegDocument> doc) {
        if (doc.isOpen()) {
            doc.getView().requestActive();
        } else {
            TransRegTopComponent view = new TransRegTopComponent(doc);
            view.open();
            view.requestActive();
        }
    }
    
    @Override
    public boolean isAutoLoad() {
        return true;
    }
}
