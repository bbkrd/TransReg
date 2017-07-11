/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui;

import de.bundesbank.transreg.admin.TransRegDocument;
import de.bundesbank.transreg.logic.TransRegVar;
import de.bundesbank.transreg.ui.nodes.NodesLevelEnum;
import de.bundesbank.transreg.ui.nodes.TransRegVarChildrenFactory;
import de.bundesbank.transreg.util.GroupsEnum;
import ec.nbdemetra.ui.DemetraUI;
import ec.nbdemetra.ui.NbComponents;
import ec.nbdemetra.ui.awt.ActionMaps;
import ec.nbdemetra.ui.awt.InputMaps;
import ec.nbdemetra.ui.awt.KeyStrokes;
import ec.nbdemetra.ui.tsaction.ITsAction;
import ec.tss.Ts;
import ec.tss.TsCollection;
import ec.tss.TsFactory;
import ec.tss.tsproviders.utils.MultiLineNameUtil;
import ec.tstoolkit.timeseries.regression.ITsVariable;
import ec.tstoolkit.timeseries.regression.TsVariables;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsDomain;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.timeseries.simplets.TsPeriod;
import ec.ui.interfaces.ITsActionAble;
import ec.util.chart.swing.Charts;
import ec.util.various.swing.JCommand;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.dnd.DnDConstants;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author s4504gn
 */
public class TransRegVarOutlineView extends JComponent implements ITsActionAble, ExplorerManager.Provider {

    private static final int //ID_COLUMN_ACTIVE = -1, 
            ID_COLUMN_LEVEL = 1,
            ID_COLUMN_FREQUENCY = 2,
            ID_COLUMN_TIMESPAN = 3,
            ID_COLUMN_CENTERUSER = 4,
            ID_COLUMN_TIMESTAMP = 5,
            ID_COLUMN_CALC_SPAN = 6,
            ID_COLUMN_CALC_MEAN = 7;
//            ID_COLUMN_DATA = 4;

    public static String CHANGE_SELECTED_VAR = "change_selected_var";

    private TransRegDocument vars;
    private List<TransRegVar> myModels = new ArrayList<>();

    private OutlineView outlineview;
    private ExplorerManager em = new ExplorerManager();

    private PropertyChangeSupport pcs;
    private ITsAction tsAction;

    public TransRegVarOutlineView(TransRegDocument doc) {

        pcs = new PropertyChangeSupport(this);
        vars = doc;
        outlineview = buildView();

        registerActions();
//        registerInputs();
        enableOpenOnDoubleClick();
        enablePopupMenu();

        if (!(vars == null || vars.variables() == null || vars.variables().isEmpty())) {
            createTreeFromDoc();
        }

//        createDummyTree();
        setLayout(new BorderLayout());
        add(NbComponents.newJScrollPane(outlineview), BorderLayout.CENTER);
    }

    private void createTreeFromDoc() {
        myModels.clear();
        for (ITsVariable var : vars.variables()) {
            if (var instanceof TransRegVar) {
                // only original regressor should add to the list of TransRegVars
                TransRegVar v = (TransRegVar) var;
                if (v.isRoot()) {
                    myModels.add(v);
                }
            }
        }
        setNodes();
    }

    private OutlineView buildView() {
        OutlineView ov = new OutlineView();

        ov.getOutline().setRootVisible(false);
        ov.setDropTarget(true);
        ov.setAllowedDropActions(DnDConstants.ACTION_COPY_OR_MOVE);
//        ov.setTransferHandler(new TransRegTransferHandler());

        // set columns
        ov.setPropertyColumns(
                //TransRegVar.PROP_X, "Active",
                TransRegVar.PROP_LEVEL, "Level",
                TransRegVar.PROP_FREQUENCY, "Frequency",
                TransRegVar.PROP_TIMESPAN, "Period",
                TransRegVar.PROP_CENTERUSER, "Pre test",
                TransRegVar.PROP_TIMESTAMP, "Timestamp",
                TransRegVar.PROP_CALC_SPAN, "Mean Calculation Span",
                TransRegVar.PROP_CALC_MEAN, "Calculated Mean"
        //                TransRegVar.PROP_DATA, "TsData"
        );

        TableColumnModel tableColumn = ov.getOutline().getColumnModel();
        tableColumn.getColumn(ID_COLUMN_LEVEL).setCellRenderer(new DefaultTableCellRenderer());
        tableColumn.getColumn(ID_COLUMN_FREQUENCY).setCellRenderer(new DefaultTableCellRenderer());
        tableColumn.getColumn(ID_COLUMN_TIMESPAN).setCellRenderer(new DefaultTableCellRenderer());
        tableColumn.getColumn(ID_COLUMN_CENTERUSER).setCellRenderer(new DefaultTableCellRenderer());
        tableColumn.getColumn(ID_COLUMN_TIMESTAMP).setCellRenderer(new DefaultTableCellRenderer());
        tableColumn.getColumn(ID_COLUMN_CALC_SPAN).setCellRenderer(new DefaultTableCellRenderer());
        tableColumn.getColumn(ID_COLUMN_CALC_MEAN).setCellRenderer(new DefaultTableCellRenderer());
//        tableColumn.getColumn(ID_COLUMN_DATA).setCellRenderer(new TsSparklineCellRenderer());
        // Idee fuer TsData: 
//        ov.getOutline().addColumn(<TableColumn>);

        ov.setTreeSortable(false);
        ov.getOutline().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ov.getOutline().setRowSorter(null);

        ov.getOutline().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    firePropertyChange(CHANGE_SELECTED_VAR, null, getSelectedVariable());
                }
            }
        });
        return ov;
    }

    private void setNodes() {
        Children children = Children.create(new TransRegVarChildrenFactory(myModels), false);
        em.setRootContext(new AbstractNode(children));
    }

    public OutlineView getOutlineview() {
        return outlineview;
    }

    public TransRegDocument getVars() {
        return vars;
    }

    public TransRegVar getSelectedVariable() {
        return getSelectedVariable(this);
    }

    private static TransRegVar getSelectedVariable(TransRegVarOutlineView ov) {
        int selectetedRow = ov.getOutlineview().getOutline().getSelectedRow();
        if (selectetedRow >= 0) {
            String name = ov.getOutlineview().getOutline().getModel().getValueAt(selectetedRow, 0).toString();
            ITsVariable var = ov.getVars().get(name);
            if (var instanceof TransRegVar) {
                return ((TransRegVar) var);
            }
        }
        return null;
    }

    private static Ts toTs(TransRegVar variable) {
        return TsFactory.instance.createTs(variable.getName(), null, variable.getTsData());
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }

    public void refresh() {
        createTreeFromDoc();
    }

    //<editor-fold defaultstate="collapsed" desc="TransferHandler">
    public void appendTsVariables(TsCollection coll) {
        for (Ts s : coll) {
            TransRegVar v;
            String name = MultiLineNameUtil.join(s.getName());
            if (s.getMoniker().isAnonymous()) {
                v = new TransRegVar(name, s.getTsData());
            } else {
                v = new TransRegVar(name, s.getMoniker(), s.getTsData());
            }
            // in myModel
            String nextName = getNextName(name, vars);
            v.setName(nextName);
            myModels.add(v);
            vars.set(nextName, v);

        }
        setNodes();
    }

    private static String getNextName(String name, TransRegDocument vars) {
        int counter = 0;
        String nextName = name;
        while (vars.get(nextName) != null) {
            nextName = name + "\n" + ++counter;
            nextName = MultiLineNameUtil.join(nextName);
        }
        return nextName;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Actions, Menus">
    @Override
    public ITsAction getTsAction() {
        return tsAction;
    }

    @Override
    public void setTsAction(ITsAction tsAction) {
        ITsAction old = this.tsAction;
        this.tsAction = tsAction;
        firePropertyChange(TS_ACTION_PROPERTY, old, this.tsAction);
    }

    public static final String DELETE_ACTION = "delete";
    public static final String ROOT_ACTION = "root";
//    public static final String CLEAR_ACTION = "clear";
    public static final String OPEN_ACTION = "open";
    public static final String RENAME_ACTION = "rename";

    private void registerActions() {
        ActionMap am = getActionMap();
        am.put(OPEN_ACTION, OpenCommand.INSTANCE.toAction(this));
        am.put(RENAME_ACTION, RenameCommand.INSTANCE.toAction(this));
        am.put(DELETE_ACTION, DeleteCommand.INSTANCE.toAction(this));
        am.put(ROOT_ACTION, SetAsRootCommand.INSTANCE.toAction(this));
//        am.put(CLEAR_ACTION, ClearCommand.INSTANCE.toAction(this));
        ActionMaps.copyEntries(am, false, outlineview.getActionMap());
    }

    private void registerInputs() {
        InputMap im = getInputMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), OPEN_ACTION);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), DELETE_ACTION);
//        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK), CLEAR_ACTION);
        InputMaps.copyEntries(im, false, outlineview.getInputMap());
    }

    private void enableOpenOnDoubleClick() {
        outlineview.getOutline().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!Charts.isPopup(e) && Charts.isDoubleClick(e)) {
                    ActionMaps.performAction(getActionMap(), OPEN_ACTION, e);
                }
            }
        });
    }

    private void enablePopupMenu() {
        outlineview.getOutline().setComponentPopupMenu(buildPopupMenu());
    }

    private JMenu buildOpenWithMenu() {
        JMenu result = new JMenu(OpenWithCommand.INSTANCE.toAction(this));

        for (ITsAction o : DemetraUI.getDefault().getTsActions()) {
            JMenuItem item = new JMenuItem(new OpenWithItemCommand(o).toAction(this));
            item.setName(o.getName());
            item.setText(o.getDisplayName());
            result.add(item);
        }

        return result;
    }

    protected JPopupMenu buildPopupMenu() {
        ActionMap actionMap = getActionMap();

        JMenu result = new JMenu();
        JMenuItem item;

        item = new JMenuItem(actionMap.get(OPEN_ACTION));
        item.setText("Open");
        item.setAccelerator(KeyStrokes.OPEN.get(0));
        item.setFont(item.getFont().deriveFont(Font.BOLD));
        result.add(item);

        item = buildOpenWithMenu();
        item.setText("Open with");
        result.add(item);

        item = new JMenuItem(actionMap.get(RENAME_ACTION));
        item.setText("Rename");
        result.add(item);

        result.addSeparator();

        item = new JMenuItem(actionMap.get(DELETE_ACTION));
        item.setText("Remove");
        item.setAccelerator(KeyStrokes.DELETE.get(0));
        result.add(item);

        item = new JMenuItem(actionMap.get(ROOT_ACTION));
        item.setText("Set as root");
        result.add(item);

        return result.getPopupMenu();
    }

    private static final class OpenCommand extends JCommand<TransRegVarOutlineView> {

        public static final OpenCommand INSTANCE = new OpenCommand();

        @Override
        public void execute(TransRegVarOutlineView c) throws Exception {
            TransRegVar variable = getSelectedVariable(c);
            ITsAction tsAction = c.tsAction != null ? c.tsAction : DemetraUI.getDefault().getTsAction();
            tsAction.open(toTs(variable));
        }

        @Override
        public boolean isEnabled(TransRegVarOutlineView c) {
            return getSelectedVariable(c) != null;
        }

        @Override
        public JCommand.ActionAdapter toAction(TransRegVarOutlineView c) {
            return super.toAction(c).withWeakListSelectionListener(c.outlineview.getOutline().getSelectionModel());
        }
    }

    private static final class OpenWithCommand extends JCommand<TransRegVarOutlineView> {

        public static final OpenWithCommand INSTANCE = new OpenWithCommand();

        @Override
        public void execute(TransRegVarOutlineView c) throws Exception {
            // do nothing
        }

        @Override
        public boolean isEnabled(TransRegVarOutlineView c) {
            return c.outlineview.getOutline().getSelectedRowCount() == 1;
        }

        @Override
        public JCommand.ActionAdapter toAction(TransRegVarOutlineView c) {
            return super.toAction(c).withWeakListSelectionListener(c.outlineview.getOutline().getSelectionModel());
        }
    }

    private static final class OpenWithItemCommand extends JCommand<TransRegVarOutlineView> {

        private final ITsAction tsAction;

        OpenWithItemCommand(@Nonnull ITsAction tsAction) {
            this.tsAction = tsAction;
        }

        @Override
        public void execute(TransRegVarOutlineView c) throws Exception {
            tsAction.open(toTs(getSelectedVariable(c)));
        }
    }

    private static final class DeleteCommand extends JCommand<TransRegVarOutlineView> {

        public static final DeleteCommand INSTANCE = new DeleteCommand();

        @Override
        public void execute(TransRegVarOutlineView c) throws java.lang.Exception {
            int[] sel = c.outlineview.getOutline().getSelectedRows();
            if (sel.length == 0) {
                return;
            }
            NotifyDescriptor nd = new NotifyDescriptor.Confirmation("Are you sure you want to delete the selected items?", NotifyDescriptor.OK_CANCEL_OPTION);
            if (DialogDisplayer.getDefault().notify(nd) != NotifyDescriptor.OK_OPTION) {
                return;
            }
            TransRegVar var = getSelectedVariable(c);
            if (var != null) {
                /*
                 * delete a variable (var) and its children 
                 */

                // recursive methode
                deleteWithChildren(var, c);

                if (!var.isRoot()) {
                    var.getParent().removeChild(var);
                }

                // general todo to delete a variable
                c.vars.remove(var);
                if (c.myModels.contains(var)) {
                    c.myModels.remove(var);
                }
                TransRegVar.variables.remove(var.getID());
                c.firePropertyChange(CHANGE_SELECTED_VAR, var, null);
                c.setNodes();
            }
        }

        private static void deleteWithChildren(TransRegVar var, TransRegVarOutlineView c) {
            if (var.hasChildren()) {
                for (TransRegVar t : var.getChildren()) {
                    deleteWithChildren(t, c);
                    c.vars.remove(t);
                    TransRegVar.variables.remove(t.getID());
                }
            }

        }

        @Override
        public boolean isEnabled(TransRegVarOutlineView c) {
            return c.outlineview.getOutline().getSelectedRowCount() > 0;
        }

        @Override
        public JCommand.ActionAdapter toAction(TransRegVarOutlineView c) {
            return super.toAction(c).withWeakListSelectionListener(c.outlineview.getOutline().getSelectionModel());
        }
    }

    private static final class ClearCommand extends JCommand<TransRegVarOutlineView> {

        public static final ClearCommand INSTANCE = new ClearCommand();

        @Override
        public void execute(TransRegVarOutlineView c) throws java.lang.Exception {
            NotifyDescriptor nd = new NotifyDescriptor.Confirmation("Are you sure you want to clear the list?", NotifyDescriptor.OK_CANCEL_OPTION);
            if (DialogDisplayer.getDefault().notify(nd) != NotifyDescriptor.OK_OPTION) {
                return;
            }
            TransRegVar.variables.clear();
            c.vars.clear();
            c.myModels.clear();
            // TODO: gui neu zeichnen
            c.remove(c.outlineview);
            c.outlineview = c.buildView();
            c.add(NbComponents.newJScrollPane(c.outlineview), BorderLayout.CENTER);
//            c.validate();
//            c.setNodes();
        }
    }

    private static final class RenameCommand extends JCommand<TransRegVarOutlineView> {

        public static final RenameCommand INSTANCE = new RenameCommand();

        @Override
        public void execute(TransRegVarOutlineView c) throws java.lang.Exception {
            int[] sel = c.outlineview.getOutline().getSelectedRows();
            if (sel.length != 1) {
                return;
            }
            TransRegVar renamedVar = getSelectedVariable(c);
            String oldName = c.vars.get(renamedVar);
            String newName;
            VarName nd = new VarName(c.vars, "New name:", "Please enter the new name", oldName);
            if (DialogDisplayer.getDefault().notify(nd) != NotifyDescriptor.OK_OPTION) {
                return;
            }
            newName = nd.getInputText();
            if (newName.equals(oldName)) {
                return;
            }
            // Rename for Nodes
            if (c.myModels.contains(renamedVar)) {
                c.myModels.remove(renamedVar);
                renamedVar.rename(newName);
                c.myModels.add(renamedVar);
            } else {
                renamedVar.rename(newName);
            }
            // rename for WS
            c.vars.rename(oldName, newName);
            c.setNodes();
        }

        @Override
        public boolean isEnabled(TransRegVarOutlineView c) {
            return c.outlineview.getOutline().getSelectedRowCount() == 1;
        }

        @Override
        public ActionAdapter toAction(TransRegVarOutlineView c) {
            return super.toAction(c).withWeakListSelectionListener(c.outlineview.getOutline().getSelectionModel());
        }
    }

    private static final class SetAsRootCommand extends JCommand<TransRegVarOutlineView> {

        public static final SetAsRootCommand INSTANCE = new SetAsRootCommand();

        @Override
        public void execute(TransRegVarOutlineView c) throws java.lang.Exception {
            int[] sel = c.outlineview.getOutline().getSelectedRows();
            if (sel.length != 1) {
                return;
            }
            TransRegVar var = getSelectedVariable(c);

            if (var.isRoot()) {
                return;
            }

            var.getParent().removeChild(var);
            var.removeParent();
            c.myModels.add(var);
            c.setNodes();
        }

        @Override
        public boolean isEnabled(TransRegVarOutlineView c) {
            return c.outlineview.getOutline().getSelectedRowCount() == 1;
        }

        @Override
        public JCommand.ActionAdapter toAction(TransRegVarOutlineView c) {
            return super.toAction(c).withWeakListSelectionListener(c.outlineview.getOutline().getSelectionModel());
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VarName">
    private static final class VarName extends NotifyDescriptor.InputLine {

        VarName(final TsVariables vars, String title, String text, final String oldname) {
            super(title, text, NotifyDescriptor.QUESTION_MESSAGE, NotifyDescriptor.OK_CANCEL_OPTION);

            setInputText(oldname);
            textField.addKeyListener(new KeyListener() {
                // To handle VK_ENTER !!!
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && !textField.getInputVerifier().verify(textField)) {
                        e.consume();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });
            textField.setInputVerifier(new InputVerifier() {
                @Override
                public boolean verify(JComponent input) {
                    JTextField txt = (JTextField) input;
                    String name = txt.getText();
                    if (name.equals(oldname)) {
                        return true;
                    }
                    if (vars.contains(name)) {
                        NotifyDescriptor nd = new NotifyDescriptor.Message(name + " is in use. You should choose another name!");
                        DialogDisplayer.getDefault().notify(nd);
                        return false;
                    }
                    if (!vars.getNameValidator().accept(name)) {
                        NotifyDescriptor nd = new NotifyDescriptor.Message(vars.getNameValidator().getLastError());
                        DialogDisplayer.getDefault().notify(nd);
                        return false;
                    }
                    return true;
                }
            });
        }
    }
//</editor-fold>

    private void createDummyTree() {
        TsData data = new TsData(new TsDomain(new TsPeriod(TsFrequency.Monthly), 120));
        TransRegVar t1 = new TransRegVar("Reg1", data);
        vars.set(t1.getName(), t1);
        TransRegVar t1_g1 = new TransRegVar("Reg1_g1", data);
        t1_g1.setLevel(NodesLevelEnum.GROUP);
        vars.set(t1_g1.getName(), t1_g1);
//        TransRegVar t1_g1_a = new TransRegVar("t1_g1_a", data);
//        t1_g1_a.setLevel(NodesLevelEnum.ACTIVE);
//        vars.set(t1_g1_a.getName(), t1_g1_a);
        TransRegVar t1_g1_center = new TransRegVar("Reg1_g1_centered", data);
        t1_g1_center.setLevel(NodesLevelEnum.CENTERUSER);
        vars.set(t1_g1_center.getName(), t1_g1_center);

        TransRegVar t1_g2 = new TransRegVar("Reg1_g2", data);
        t1_g2.setGroupStatus(GroupsEnum.Group2);
//        t1_g2.getSettings().getGroups().setMyGroup(GroupsEnum.Group2);
        t1_g2.setLevel(NodesLevelEnum.GROUP);
        vars.set(t1_g2.getName(), t1_g2);

        t1_g1.addChild(t1_g1_center);
//        t1_g1.addChild(t1_g1_a);
        t1.addChild(t1_g1);
        t1.addChild(t1_g2);
        myModels.add(t1);

        TransRegVar t2 = new TransRegVar("Reg2", data);
        vars.set(t2.getName(), t2);
        TransRegVar t2_g1 = new TransRegVar("Reg2_g1", data);
        t2_g1.setLevel(NodesLevelEnum.GROUP);
        vars.set(t2_g1.getName(), t2_g1);
//        TransRegVar t1_g1_a = new TransRegVar("t1_g1_a", data);
//        t1_g1_a.setLevel(NodesLevelEnum.ACTIVE);
//        vars.set(t1_g1_a.getName(), t1_g1_a);
        TransRegVar t2_g1_center = new TransRegVar("Reg2_g1_centered", data);
        t2_g1_center.setLevel(NodesLevelEnum.CENTERUSER);
        vars.set(t2_g1_center.getName(), t2_g1_center);

        TransRegVar t2_g2 = new TransRegVar("Reg2_g2", data);
        t2_g2.setGroupStatus(GroupsEnum.Group2);
//        t2_g2.getSettings().getGroups().setMyGroup(GroupsEnum.Group2);
        t2_g2.setLevel(NodesLevelEnum.GROUP);
        vars.set(t2_g2.getName(), t2_g2);

        TransRegVar t2_g2_center = new TransRegVar("Reg2_g2_centered", data);
        t2_g2_center.setLevel(NodesLevelEnum.CENTERUSER);
        vars.set(t2_g2_center.getName(), t2_g2_center);

        t2_g1.addChild(t2_g1_center);
        t2_g2.addChild(t2_g2_center);
//        t1_g1.addChild(t1_g1_a);
        t2.addChild(t2_g1);
        t2.addChild(t2_g2);
        myModels.add(t2);

        setNodes();
    }
}
