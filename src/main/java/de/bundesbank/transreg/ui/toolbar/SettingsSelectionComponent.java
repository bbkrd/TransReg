/*
 * Copyright 2013 National Bank of Belgium
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved 
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and 
 * limitations under the Licence.
 */
package de.bundesbank.transreg.ui.toolbar;

import com.google.common.base.Optional;
import de.bundesbank.transreg.settings.TransRegSettings;
import ec.nbdemetra.ui.awt.IDialogDescriptorProvider;
import ec.nbdemetra.ui.awt.JComponent2;
import ec.nbdemetra.ui.calendars.CustomDialogDescriptor;
import ec.nbdemetra.ui.nodes.DecoratedNode;
import ec.nbdemetra.ws.WorkspaceFactory;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.nodes.DummyWsNode;
import ec.nbdemetra.ws.nodes.ItemWsNode;
import ec.satoolkit.GenericSaProcessingFactory;
import ec.tss.tsproviders.utils.IConstraint;
import ec.tstoolkit.utilities.Id;
import ec.tstoolkit.utilities.LinearId;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.function.Predicate;
import javax.swing.tree.TreeSelectionModel;
import org.openide.DialogDescriptor;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.Node;

/**
 *
 * @author Nina Gonschorreck
 */
public class SettingsSelectionComponent extends JComponent2 implements ExplorerManager.Provider, IDialogDescriptorProvider {

    public static final Id ID = new LinearId("Utilities", "TransReg");
    public static final String SETTING_PROPERTY = "settings";
    public static final String ICON_PROPERTY = "icon";

    private final BeanTreeView tree;
    private final ExplorerManager em;
    private final SelectionListener selectionListener;

    private TransRegSettings settings;
    private Image icon;

    public SettingsSelectionComponent() {
        this(false);
    }

    public SettingsSelectionComponent(boolean showSystemOnly) {
        this.tree = new BeanTreeView();
        this.em = new ExplorerManager();
        this.selectionListener = new SelectionListener();
        this.settings = null;
        this.icon = null;

        tree.setRootVisible(false);
        tree.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

//        DecoratedNode root = new DecoratedNode(new DummyWsNode(WorkspaceFactory.getInstance().getActiveWorkspace(), ID), showSystemOnly ? ItemWsNodeFilter.SYSTEM_ONLY : (o -> true));
//        for (DecoratedNode o : root.breadthFirstIterable()) {
//            o.setPreferredActionDecorator(DecoratedNode.PreferredAction.DO_NOTHING);
//        }
//
//        em.setRootContext(root); 

        setLayout(new BorderLayout());
        add(tree, BorderLayout.CENTER);
        setPreferredSize(new Dimension(225, 300));

        em.addVetoableChangeListener(selectionListener);
        addPropertyChangeListener(evt -> {
            String p = evt.getPropertyName();
            if (p.equals(SETTING_PROPERTY)) {
                onSpecificationChange();
            }
        });
    }

    boolean isCurrentSpecificationNode(Node o) {
        // TODO
        return false;
//        return o instanceof ItemWsNode && ((ItemWsNode) o).getItem().getElement().equals(specification);
    }

    class SelectionListener implements VetoableChangeListener {

        boolean enable = true;

        @Override
        public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
            if (enable && ExplorerManager.PROP_SELECTED_NODES.equals(evt.getPropertyName())) {
                Node[] nodes = (Node[]) evt.getNewValue();
                if (nodes.length > 0 && ((DecoratedNode) nodes[0]).getOriginal() instanceof ItemWsNode) {
                    ItemWsNode node = (ItemWsNode) ((DecoratedNode) nodes[0]).getOriginal();
                    setSettings((TransRegSettings) node.getItem().getElement());
                    setIcon(node.getIcon(BeanInfo.ICON_COLOR_16x16));
                } else {
                    setSettings(null);
                    setIcon(null);
                }
            }
        }
    }

    protected void onSpecificationChange() {
        selectionListener.enable = false;
//        for (Node o : (Node[]) em.getSelectedNodes()) {
//            ((DecoratedNode) o).setHtmlDecorator(null);
//        }
        DecoratedNode root = (DecoratedNode) em.getRootContext();
        Optional<DecoratedNode> node = root.breadthFirstIterable().firstMatch(o -> isCurrentSpecificationNode(o.getOriginal()));
        if (node.isPresent()) {
//            node.get().setHtmlDecorator(DecoratedNode.Html.BOLD);
            try {
                em.setSelectedNodes(new Node[]{node.get()});
            } catch (PropertyVetoException ex) {
                // do nothing?
            }
        }
        selectionListener.enable = true;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }

    public TransRegSettings getSettings() {
        return settings;
    }

    public void setSettings(TransRegSettings settings) {
        TransRegSettings old = this.settings;
        this.settings = settings;
        firePropertyChange(SETTING_PROPERTY, old, this.settings);
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        Image old = this.icon;
        this.icon = icon;
        firePropertyChange(ICON_PROPERTY, old, this.icon);
    }
    //</editor-fold>

    @Override
    public DialogDescriptor createDialogDescriptor(String title) {
        return new SpecSelectionDialogDescriptor(this, title);
    }

    private static class SpecSelectionDialogDescriptor extends CustomDialogDescriptor<SettingsSelectionComponent> {

        SpecSelectionDialogDescriptor(SettingsSelectionComponent p, String title) {
            super(p, title, p);
            validate(SpecSelectionConstraints.values());
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String p = evt.getPropertyName();
            if (p.equals(SettingsSelectionComponent.SETTING_PROPERTY)) {
                validate(SpecSelectionConstraints.values());
            }
        }
    }

    private enum SpecSelectionConstraints implements IConstraint<SettingsSelectionComponent> {

        SELECTION;

        @Override
        public String check(SettingsSelectionComponent t) {
            return t.getSettings()== null ? "Specification not selected" : null;
        }
    }

//    private enum ItemWsNodeFilter implements Predicate<Node> {
//
//        SYSTEM_ONLY;
//
//        @Override
//        public boolean test(Node input) {
//            return !(input instanceof ItemWsNode)
//                    || ((ItemWsNode) input).getItem().getStatus() == WorkspaceItem.Status.System;
//        }
//    }
}
