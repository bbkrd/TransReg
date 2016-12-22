/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui.propertyEditor;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import de.bundesbank.transreg.util.GroupsEnum;
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
import javax.swing.DefaultComboBoxModel;
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

    private GroupsEnum[] groups;

    public GroupsEnumPropertyEditor() {
        editor = new GroupsEnumEditor();
    }

    void fireChanged(GroupsEnum[] groups) {
        GroupsEnum[] old = this.groups;
        this.groups = groups;
        firePropertyChange(old, this.groups);
    }

    @Override
    public Object getValue() {
        return groups;
    }

    @Override
    public void setValue(Object value) {
        if (null != value && value instanceof GroupsEnum[]) {
            groups = (GroupsEnum[]) value;
            ((GroupsEnumEditor) editor).setArray(groups);
        }
    }

    class GroupsEnumEditor extends JPanel {

        private GroupsEnum[] ops;

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
                                        return GroupsEnum.class;
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
                                        ops[row] = (GroupsEnum) aValue;
                                    }
                                    fireTableCellUpdated(row, column);
                                }
                            });

                    table.setDefaultEditor(GroupsEnum.class, new CustomGroupsEnumEditor()); 
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

        public void setArray(final GroupsEnum[] param) {
            ops = param.clone();
        }
    }
}

class CustomGroupsEnumEditor extends AbstractCellEditor implements TableCellEditor {

    private final JComboBox cb_;

    public CustomGroupsEnumEditor() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(GroupsEnum.values());
        cb_ = new JComboBox(model);
    }

    @Override
    public Object getCellEditorValue() {
        return cb_.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        cb_.setSelectedItem(value);
        return cb_;
    }
}  