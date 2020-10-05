package de.bundesbank.transreg.ui;

import com.google.common.base.Optional;
import ec.nbdemetra.ui.awt.IDialogDescriptorProvider;
import ec.nbdemetra.ui.awt.JComponent2;
import ec.nbdemetra.ui.calendars.CustomDialogDescriptor;
import ec.nbdemetra.ui.nodes.DecoratedNode;
import ec.nbdemetra.ws.WorkspaceFactory;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.nodes.DummyWsNode;
import ec.nbdemetra.ws.nodes.ItemWsNode;
import ec.nbdemetra.ws.ui.SpecSelectionComponent;
import ec.satoolkit.GenericSaProcessingFactory;
import ec.satoolkit.ISaSpecification;
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
public class SettingSelectionComponent extends JComponent2 implements ExplorerManager.Provider, IDialogDescriptorProvider {

    public static final Id SPECS_ID = new LinearId("TransReg", "TransRegSettings");
    public static final String SPECIFICATION_PROPERTY = "specification";
    public static final String ICON_PROPERTY = "icon";

    private final BeanTreeView tree;
    private final ExplorerManager em;
    private final SelectionListener selectionListener;

    private ISaSpecification specification;
    private Image icon;

    public SettingSelectionComponent() {
        this(false);
    }

    public SettingSelectionComponent(boolean showSystemOnly) {
        this.tree = new BeanTreeView();
        this.em = new ExplorerManager();
        this.selectionListener = new SelectionListener();
        this.specification = null;
        this.icon = null;

        tree.setRootVisible(false);
        tree.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        DecoratedNode root = new DecoratedNode(new DummyWsNode(WorkspaceFactory.getInstance().getActiveWorkspace(), SPECS_ID), showSystemOnly ? ItemWsNodeFilter.SYSTEM_ONLY : (o -> true));
        for (DecoratedNode o : root.breadthFirstIterable()) {
            o.setPreferredActionDecorator(DecoratedNode.PreferredAction.DO_NOTHING);
        }

        em.setRootContext(root);

        setLayout(new BorderLayout());
        add(tree, BorderLayout.CENTER);
        setPreferredSize(new Dimension(225, 300));

        em.addVetoableChangeListener(selectionListener);
        addPropertyChangeListener(evt -> {
            String p = evt.getPropertyName();
            if (p.equals(SPECIFICATION_PROPERTY)) {
                onSpecificationChange();
            }
        });
    }

    boolean isCurrentSpecificationNode(Node o) {
        return o instanceof ItemWsNode && ((ItemWsNode) o).getItem().getElement().equals(specification);
    }

    class SelectionListener implements VetoableChangeListener {

        boolean enable = true;

        @Override
        public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
            if (enable && ExplorerManager.PROP_SELECTED_NODES.equals(evt.getPropertyName())) {
                Node[] nodes = (Node[]) evt.getNewValue();
                if (nodes.length > 0 && ((DecoratedNode) nodes[0]).getOriginal() instanceof ItemWsNode) {
                    ItemWsNode node = (ItemWsNode) ((DecoratedNode) nodes[0]).getOriginal();
                    setSpecification((ISaSpecification) node.getItem().getElement());
                    setIcon(node.getIcon(BeanInfo.ICON_COLOR_16x16));
                } else {
                    setSpecification(null);
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

    public ISaSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(ISaSpecification specification) {
        ISaSpecification old = this.specification;
        this.specification = specification;
        firePropertyChange(SPECIFICATION_PROPERTY, old, this.specification);
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

    private static class SpecSelectionDialogDescriptor extends CustomDialogDescriptor<SettingSelectionComponent> {

        SpecSelectionDialogDescriptor(SettingSelectionComponent p, String title) {
            super(p, title, p);
            validate(SpecSelectionConstraints.values());
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String p = evt.getPropertyName();
            if (p.equals(SpecSelectionComponent.SPECIFICATION_PROPERTY)) {
                validate(SpecSelectionConstraints.values());
            }
        }
    }

    private enum SpecSelectionConstraints implements IConstraint<SettingSelectionComponent> {

        SELECTION;

        @Override
        public String check(SettingSelectionComponent t) {
            return t.getSpecification() == null ? "Specification not selected" : null;
        }
    }

    private enum ItemWsNodeFilter implements Predicate<Node> {

        SYSTEM_ONLY;

        @Override
        public boolean test(Node input) {
            return !(input instanceof ItemWsNode)
                    || ((ItemWsNode) input).getItem().getStatus() == WorkspaceItem.Status.System;
        }
    }
}
