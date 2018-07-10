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
package de.bundesbank.transreg.ui.propertyEditor;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import de.bundesbank.transreg.util.Group;
import ec.nbdemetra.ui.NbComponents;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.timeseries.simplets.TsPeriod;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author s4504gn
 */
public class GroupsEnumPropertyEditor extends AbstractPropertyEditor {

    private Group[] groups;

    public GroupsEnumPropertyEditor() {
        editor = new GroupsEnumEditor();
    }

    void fireChanged(Group[] groups) {
        Group[] old = this.groups;
        this.groups = groups;
        firePropertyChange(old, this.groups);
    }

    @Override
    public Object getValue() {
        return groups;
    }

    @Override
    public void setValue(Object value) {
        if (null != value && value instanceof Group[]) {
            groups = (Group[]) value; // auch clone
            ((GroupsEnumEditor) editor).setArray(groups);
        }
    }

    class GroupsEnumEditor extends JPanel {

        private Group[] ops;

        public GroupsEnumEditor() {
            final JButton button = new JButton("...");
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JPanel pane = new JPanel(new BorderLayout());
                    final JTable table = new JTable(
                            new DefaultTableModel() {

                        @Override
                        public int getColumnCount() {
                            return 2;
                        }

                        @Override
                        public String getColumnName(int column) {
                            if (column == 0) {
                                return "Period";
                            } else {
                                return "Group";
                            }
                        }

                        @Override
                        public Class<?> getColumnClass(int columnIndex) {
                            if (columnIndex == 0) {
                                return String.class;
                            } else {
                                return Group.class;
                            }
                        }

                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return column == 1;
                        }

                        @Override
                        public int getRowCount() {
                            return ops.length;
                        }

                        @Override
                        public Object getValueAt(int row, int column) {
                            if (column == 0) {
                                return TsPeriod.formatPeriod(TsFrequency.valueOf(ops.length), row);
                            } else {
                                return ops[row];
                            }
                        }

                        @Override
                        public void setValueAt(Object aValue, int row, int column) {
                            if (column == 1) {
                                ops[row] = new Group((int) aValue);
                            }
                            fireTableCellUpdated(row, column);
                        }
                    });

                    int maxNumber = ops.length; // aendern
                    CustomGroupsEnumEditor editor = new CustomGroupsEnumEditor(maxNumber);
                    table.setDefaultEditor(Group.class, editor);
                    table.setFillsViewportHeight(true);
                    pane.add(NbComponents.newJScrollPane(table), BorderLayout.CENTER);

                    JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(button), Dialog.ModalityType.TOOLKIT_MODAL);
                    dlg.setContentPane(pane);
                    dlg.addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowClosing(WindowEvent e) {
                            if (table.getCellEditor() != null) {
                                table.getCellEditor().stopCellEditing();
                            }
                            fireChanged(ops);
                        }
                    });
                    dlg.setMinimumSize(new Dimension(300, 300));
                    dlg.setModal(true);
                    dlg.setVisible(true);
                    if (table.getCellEditor() != null) {
                        table.getCellEditor().stopCellEditing();
                    }
                }
            });

            setLayout(new BorderLayout());
            add(button, BorderLayout.CENTER);
        }

        public void setArray(final Group[] param) {
            ops = param.clone();
        }
    }
}

class CustomGroupsEnumEditor extends AbstractCellEditor implements TableCellEditor {

    private JComboBox cb_;
    private int number;

    public CustomGroupsEnumEditor(int numbersOfGroups) {
        number = numbersOfGroups;
        Object[] x = new Object[numbersOfGroups];
        for (int i = 0; i < numbersOfGroups; i++) {
            x[i] = "Group " + (i + 1);
        }
        cb_ = new JComboBox(x);
    }

    @Override
    public Object getCellEditorValue() {
        // s from Constructor: "Group x"
        String s = (String) cb_.getSelectedItem();
        return Integer.parseInt(s.substring(6));
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        cb_.setSelectedItem(value);
        return cb_;
    }
}
