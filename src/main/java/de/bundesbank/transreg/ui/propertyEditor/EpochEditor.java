/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui.propertyEditor;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import de.bundesbank.transreg.util.Epoch;
import ec.nbdemetra.ui.properties.l2fprod.ArrayEditorDialog;
import ec.tstoolkit.timeseries.Day;
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
                boolean permittedInput = true;
                do {
                    dialog.setVisible(true);
                    if (dialog.isDirty()) {
                        permittedInput = setDescriptors(dialog.getElements());
                    }
                } while (!permittedInput);

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

    private boolean setDescriptors(List<EpochDescriptor> elements) {
        Epoch[] old = epochs;
        epochs = new Epoch[elements.size()];
        for (int i = 0; i < epochs.length; ++i) {
            epochs[i] = elements.get(i).getCore();
        }

        /* Warning for Users because of overlapping regimes*/
        if (isOverlapping()) {
            JOptionPane.showMessageDialog(null, "Overlapping regimes are not allowed!", "Invalid regimes", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        firePropertyChange(old, epochs);
        return true;
    }

    private boolean isOverlapping() {
        int epochs_length = epochs.length;
        if (epochs_length >= 2) {
            for (int i = 0; i < epochs_length - 1; i++) {
                Day startX = epochs[i].getStart();
                Day endX = epochs[i].getEnd();
                for (int j = i + 1; j < epochs_length; j++) {
                    Day startY = epochs[j].getStart();
                    Day endY = epochs[j].getEnd();

                    if ((startX.isNotAfter(startY) && endX.isNotBefore(startY))
                            || (startX.isNotBefore(startY) && startX.isNotAfter(endY))
                            || (endX.isNotBefore(startY) && endX.isNotAfter(endY))) {
                        return true;
                    }
                }
            }
        }
        return false;
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
