/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui;

import de.bundesbank.transreg.admin.TransRegDocument;
import de.bundesbank.transreg.logic.TransRegCalculationTool;
import de.bundesbank.transreg.logic.TransRegVar;
import ec.nbdemetra.ui.DemetraUI;
import ec.nbdemetra.ui.NbComponents;
import ec.nbdemetra.ui.awt.ActionMaps;
import ec.nbdemetra.ui.awt.InputMaps;
import ec.nbdemetra.ui.awt.KeyStrokes;
import ec.nbdemetra.ui.awt.ListTableModel;
import ec.nbdemetra.ui.tsaction.ITsAction;
import ec.tss.Ts;
import ec.tss.TsCollection;
import ec.tss.TsFactory;
import ec.tss.TsInformationType;
import ec.tss.datatransfer.TssTransferSupport;
import ec.tstoolkit.data.DataBlock;
import ec.tstoolkit.timeseries.regression.ITsVariable;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsDomain;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.timeseries.simplets.TsPeriod;
import ec.ui.chart.TsSparklineCellRenderer;
import ec.ui.interfaces.ITsActionAble;
import ec.ui.list.TsFrequencyTableCellRenderer;
import ec.ui.list.TsPeriodTableCellRenderer;
import ec.util.chart.swing.Charts;
import ec.util.grid.swing.XTable;
import ec.util.various.swing.JCommand;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.COPY;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author s4504gn
 */
public class TransRegVarList extends JComponent implements ITsActionAble {

    public static final String SELECTION_CHANGE = "change";

    public static final String DELETE_ACTION = "delete";
    public static final String CLEAR_ACTION = "clear";
    public static final String OPEN_ACTION = "open";

    private ITsAction tsAction;
    private TransRegDocument vars;
    private final XTable table;
    private TransRegTableModel model;

    public TransRegVarList(TransRegDocument vars) {

        this.vars = vars;
        model = new TransRegTableModel();
        table = buildTable();

        registerActions();
        registerInputs();
        enableOpenOnDoubleClick();
        enablePopupMenu();

        setLayout(new BorderLayout());
        add(NbComponents.newJScrollPane(table), BorderLayout.CENTER);
    }

    public TransRegVar getSelectedVariable() {
        return getSelectedVariable(this);
    }

    private void registerActions() {
        ActionMap am = getActionMap();
        am.put(OPEN_ACTION, OpenCommand.INSTANCE.toAction(this));
        am.put(DELETE_ACTION, DeleteCommand.INSTANCE.toAction(this));
        am.put(CLEAR_ACTION, ClearCommand.INSTANCE.toAction(this));
        ActionMaps.copyEntries(am, false, table.getActionMap());
    }

    private void registerInputs() {
        InputMap im = getInputMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), OPEN_ACTION);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), DELETE_ACTION);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK), CLEAR_ACTION);
        InputMaps.copyEntries(im, false, table.getInputMap());
    }

    private void enableOpenOnDoubleClick() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!Charts.isPopup(e) && Charts.isDoubleClick(e)) {
                    ActionMaps.performAction(getActionMap(), OPEN_ACTION, e);
                }
            }
        });
    }

    private void enablePopupMenu() {
        table.setComponentPopupMenu(buildPopupMenu());
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

        result.addSeparator();

        item = new JMenuItem(actionMap.get(DELETE_ACTION));
        item.setText("Remove");
        item.setAccelerator(KeyStrokes.DELETE.get(0));
        result.add(item);

        item = new JMenuItem(actionMap.get(CLEAR_ACTION));
        item.setText("Clear");
        item.setAccelerator(KeyStrokes.CLEAR.get(0));
        result.add(item);

        return result.getPopupMenu();
    }

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
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
    //</editor-fold>

    private String[] names(int[] pos) {
        String[] n = new String[pos.length];
        TransRegTableModel m = (TransRegTableModel) table.getModel();
        for (int i = 0; i < pos.length; ++i) {
            n[i] = m.names[pos[i]];
        }
        return n;
    }

    //<editor-fold defaultstate="collapsed" desc="Commands">
    private static TransRegVar getSelectedVariable(TransRegVarList c) {
        if (c.table.getSelectedRowCount() == 1) {
            int idx = c.table.convertRowIndexToModel(c.table.getSelectedRow());
            String[] names = c.vars.getNames();
            ITsVariable result = c.vars.get(names[idx]);
            if (result instanceof TransRegVar) {
                return (TransRegVar) result;
            }
        }
        return null;
    }

    private static Ts toTs(TransRegVar variable) {
        return TsFactory.instance.createTs(variable.getDescription(), null, variable.getTsData());
    }

    private static final class OpenCommand extends JCommand<TransRegVarList> {

        public static final OpenCommand INSTANCE = new OpenCommand();

        @Override
        public void execute(TransRegVarList c) throws Exception {
            TransRegVar variable = getSelectedVariable(c);
            ITsAction tsAction = c.tsAction != null ? c.tsAction : DemetraUI.getDefault().getTsAction();
            tsAction.open(toTs(variable));
        }

        @Override
        public boolean isEnabled(TransRegVarList c) {
            return getSelectedVariable(c) != null;
        }

        @Override
        public JCommand.ActionAdapter toAction(TransRegVarList c) {
            return super.toAction(c).withWeakListSelectionListener(c.table.getSelectionModel());
        }
    }

    private static final class OpenWithCommand extends JCommand<TransRegVarList> {

        public static final OpenWithCommand INSTANCE = new OpenWithCommand();

        @Override
        public void execute(TransRegVarList c) throws Exception {
            // do nothing
        }

        @Override
        public boolean isEnabled(TransRegVarList c) {
            return c.table.getSelectedRowCount() == 1;
        }

        @Override
        public JCommand.ActionAdapter toAction(TransRegVarList c) {
            return super.toAction(c).withWeakListSelectionListener(c.table.getSelectionModel());
        }
    }

    private static final class OpenWithItemCommand extends JCommand<TransRegVarList> {

        private final ITsAction tsAction;

        OpenWithItemCommand(@Nonnull ITsAction tsAction) {
            this.tsAction = tsAction;
        }

        @Override
        public void execute(TransRegVarList c) throws Exception {
            tsAction.open(toTs(getSelectedVariable(c)));
        }
    }

    private static final class DeleteCommand extends JCommand<TransRegVarList> {

        public static final DeleteCommand INSTANCE = new DeleteCommand();

        @Override
        public void execute(TransRegVarList c) throws java.lang.Exception {
            int[] sel = c.table.getSelectedRows();
            if (sel.length == 0) {
                return;
            }
            NotifyDescriptor nd = new NotifyDescriptor.Confirmation("Are you sure you want to delete the selected items?", NotifyDescriptor.OK_CANCEL_OPTION);
            if (DialogDisplayer.getDefault().notify(nd) != NotifyDescriptor.OK_OPTION) {
                return;
            }

            String[] n = c.names(sel);
            for (int i = 0; i < n.length; ++i) {
                c.vars.remove(n[i]);
            }
            ((AbstractTableModel) c.table.getModel()).fireTableStructureChanged();
        }

        @Override
        public boolean isEnabled(TransRegVarList c) {
            return c.table.getSelectedRowCount() > 0;
        }

        @Override
        public JCommand.ActionAdapter toAction(TransRegVarList c) {
            return super.toAction(c).withWeakListSelectionListener(c.table.getSelectionModel());
        }
    }

    private static final class ClearCommand extends JCommand<TransRegVarList> {

        public static final ClearCommand INSTANCE = new ClearCommand();

        @Override
        public void execute(TransRegVarList c) throws java.lang.Exception {
            NotifyDescriptor nd = new NotifyDescriptor.Confirmation("Are you sure you want to clear the list?", NotifyDescriptor.OK_CANCEL_OPTION);
            if (DialogDisplayer.getDefault().notify(nd) != NotifyDescriptor.OK_OPTION) {
                return;
            }
            c.vars.clear();
            ((AbstractTableModel) c.table.getModel()).fireTableStructureChanged();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Table">
    private static final String[] information = new String[]{"Name", "Frequency", "Start", "End", "TransReg Info", "Data"};

    private XTable buildTable() {
        final XTable result = new XTable();
        result.setNoDataRenderer(new XTable.DefaultNoDataRenderer("Drop data here", "Drop data here"));

        result.setDefaultRenderer(TsData.class, new TsSparklineCellRenderer());
        result.setDefaultRenderer(TsPeriod.class, new TsPeriodTableCellRenderer());
        result.setDefaultRenderer(TsFrequency.class, new TsFrequencyTableCellRenderer());

        result.setModel(model);
        XTable.setWidthAsPercentages(result, .2, .1, .1, .1, .1, .4);

        final ListSelectionModel lsmodel = result.getSelectionModel();
        lsmodel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lsmodel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getValueIsAdjusting()) {
                    return;
                }
                String[] names = vars.getNames();

                firePropertyChange(SELECTION_CHANGE, null, vars.get(names[result.getSelectedRow()]));
            }
        });

        result.setAutoCreateRowSorter(true);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(result.getModel());
        result.setRowSorter(sorter);
        result.setDragEnabled(true);
        result.setTransferHandler(new TsVariableTransferHandler());
        result.setFillsViewportHeight(true);

        return result;
    }

    private class TsVariableTransferHandler extends TransferHandler {

        @Override
        public int getSourceActions(JComponent c) {
            return COPY;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            return null;
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            boolean result = TssTransferSupport.getDefault().canImport(support.getDataFlavors());
            if (result && support.isDrop()) {
                support.setDropAction(COPY);
            }
            return result;
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            TsCollection col = TssTransferSupport.getDefault().toTsCollection(support.getTransferable());
            if (col != null) {
                col.query(TsInformationType.All);
                if (!col.isEmpty()) {
                    appendTsVariables(col);
                }
                return true;
            }
            return false;
        }
    }

    private void appendTsVariables(TsCollection coll) {
        for (Ts s : coll) {
            TransRegVar v;
            if (s.getMoniker().isAnonymous()) {
                v = new TransRegVar(vars.nextName(), s.getTsData());
            } else {
                v = new TransRegVar(vars.nextName(), s.getMoniker(), s.getTsData());
            }
            v.calculate();
            vars.set(vars.nextName(), v);
        }
        ((AbstractTableModel) table.getModel()).fireTableStructureChanged();
    }

    private class TransRegTableModel extends AbstractTableModel {

        private String[] names;

        public TransRegTableModel() {
            if (vars != null) {
                names = vars.getNames();
            }
        }

        @Override
        public int getRowCount() {
            if (names != null) {
                return names.length;
            }
            return 0;
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public void fireTableStructureChanged() {
            if (names != null) {
                names = vars.getNames();
            }
            super.fireTableStructureChanged();
        }

        @Override
        public String getColumnName(int column
        ) {
            return information[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex
        ) {
            switch (columnIndex) {
                case 0:
                case 1:
                case 4:
                    return String.class;
                case 2:
                case 3:
                    return TsDomain.class;
                case 5:
                    return TsData.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            TransRegVar item = (TransRegVar) vars.get(names[rowIndex]);
            switch (columnIndex) {
                case 0:
                    int i = 0;
                    for (String s : vars.getNames()) {
                        if (vars.get(s).equals(item)) {
                            return names[i];
                        }
                        i++;
                    }
                    return null;
                case 1:
                    return item.getDefinitionFrequency();
                case 2: {
                    TsDomain d = item.getDefinitionDomain();
                    if (d != null) {
                        return d.getStart();
                    }
                }
                case 3: {
                    TsDomain d = item.getDefinitionDomain();
                    if (d != null) {
                        return d.getLast();
                    }
                }
                case 4:
                    // TransRegInfo : auf DefaultSettings, Datengleicheit pruefen
                    if (item.getSettings().isDefault()) {
                        return TransRegCalculationTool.testCenteruser(item.getTsData());
                    }
                    if (item.getSettings().getTimestamp() != null) {
                        return item.getSettings().getTimestamp().toString();
                    }
                    return item.getSettings().getInfo();
                case 5:
                    TsDomain d = item.getDefinitionDomain();
                    if (d != null) {
                        List<DataBlock> data = Collections.singletonList(new DataBlock(d.getLength()));
                        item.data(d, data);
                        return new TsData(d.getStart(), data.get(0));
                    }
                default:
                    return null;
            }
        }
        //</editor-fold>

    }
}
