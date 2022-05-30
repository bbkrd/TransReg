/*
 * Copyright 2018 Deutsche Bundesbank
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package de.bundesbank.transreg.actions;

import de.bundesbank.transreg.admin.TransRegDocument;
import de.bundesbank.transreg.admin.TransRegDocumentManager;
import de.bundesbank.transreg.ui.TransRegTopComponent;
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
            cur.getElement().refresh();
            if (cur.isOpen()) {
                ((TransRegTopComponent) cur.getView()).refresh();
            }
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
