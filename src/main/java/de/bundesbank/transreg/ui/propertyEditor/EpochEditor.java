/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui.propertyEditor;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import de.bundesbank.transreg.util.Epoch;
import ec.nbdemetra.ui.properties.l2fprod.ArrayEditorDialog;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Nina Gonschorreck
 */
public class EpochEditor extends AbstractPropertyEditor {

    private Epoch[] epochs;

    public EpochEditor() {
        editor = new JButton(new AbstractAction("...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                final ArrayEditorDialog<EpochDescriptor> dialog = new ArrayEditorDialog<>(SwingUtilities.getWindowAncestor(editor),
                        null != epochs ? getDescriptors() : new EpochDescriptor[]{}, EpochDescriptor.class);
                dialog.setTitle("Regimes");
                dialog.setVisible(true);
                if (dialog.isDirty()) {
                    setDescriptors(dialog.getElements());
                }
            }
        });
    }

    private EpochDescriptor[] getDescriptors() {
        EpochDescriptor[] descs = new EpochDescriptor[epochs.length];
        for (int i = 0; i < descs.length; ++i) {
            descs[i] = new EpochDescriptor(epochs[i]);
        }
        return descs;
    }

    private void setDescriptors(List<EpochDescriptor> elements) {
        Epoch[] old = epochs;
        epochs = new Epoch[elements.size()];
        for (int i = 0; i < epochs.length; ++i) {
            epochs[i] = elements.get(i).getCore();
        }
        
        /* Warning for Users because of overlapping regimes*/
        int epochs_length = epochs.length;
        boolean overlapping = false;
        if (epochs_length >= 2) {
            for (int i = 0; i < epochs_length - 1; i++) {
                for (int j = i + 1; j < epochs_length; j++) {
                    if (epochs[i].getEnd().isAfter(epochs[j].getStart()) || epochs[i].getEnd().equals(epochs[j].getStart())) {
                        overlapping = true;
                    }
                }
            }
            if (overlapping) {
                JOptionPane.showMessageDialog(null, "Warning: Overlapping regimes.");
            }
        }
        firePropertyChange(old, epochs);
    }

    @Override
    public Object getValue() {
        return epochs;
    }

    @Override
    public void setValue(Object value) {
        if (null != value && value instanceof Epoch[]) {
            Epoch[] val = (Epoch[]) value;
            epochs = new Epoch[val.length];
            for (int i = 0; i < val.length; ++i) {
                epochs[i] = val[i].clone();
            }
        } else {
            epochs = new Epoch[0];
        }
    }
}
