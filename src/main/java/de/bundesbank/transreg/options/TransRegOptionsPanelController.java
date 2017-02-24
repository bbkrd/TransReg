/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.options;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

@OptionsPanelController.SubRegistration(
        location = "Demetra",
        displayName = "#AdvancedOption_DisplayName_TransReg",
        keywords = "#AdvancedOption_Keywords_TransReg",
        keywordsCategory = "Demetra/TransReg",
        id = TransRegOptionsPanelController.ID//, position = 7
)
@org.openide.util.NbBundle.Messages({"AdvancedOption_DisplayName_TransReg=TransReg", "AdvancedOption_Keywords_TransReg=TransReg"})
public final class TransRegOptionsPanelController extends OptionsPanelController {

    public static final String ID = "Demetra/TransReg";
    public static final String TRANSREG_SAVE_METHOD = "transreg_save_method";
    public static final String TRANSREG_VERTICAL_GROUPS = "transreg_vertical_groups";
    public static final String TRANSREG_HORIZONTAL_GROUPS = "transreg_horizontal_groups";
    public static final String TRANSREG_UPPER_LIMIT = "transreg_upper_limit";
    public static final String TRANSREG_LOWER_LIMIT = "transreg_lower_limit";

    private TransRegPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;

    @Override
    public void update() {
        getPanel().load();
        changed = false;
    }

    @Override
    public void applyChanges() {
        getPanel().store();
        changed = false;
    }

    @Override
    public void cancel() {
        // need not do anything special, if no changes have been persisted yet
    }

    @Override
    public boolean isValid() {
        return getPanel().valid();
    }

    @Override
    public boolean isChanged() {
        return changed;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null; // new HelpCtx("...ID") if you have a help set
    }

    @Override
    public JComponent getComponent(Lookup masterLookup) {
        return getPanel();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    private TransRegPanel getPanel() {
        if (panel == null) {
            panel = new TransRegPanel(this);
        }
        return panel;
    }

    void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }

}
