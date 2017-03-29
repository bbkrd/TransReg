/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui;

import com.l2fprod.common.propertysheet.PropertySheetPanel;
import de.bundesbank.transreg.admin.TransRegDocumentManager;
import de.bundesbank.transreg.admin.TransRegDocument;
import de.bundesbank.transreg.admin.TransRegTransferHandler;
import de.bundesbank.transreg.logic.TransRegCalculationTool;
import de.bundesbank.transreg.logic.TransRegVar;
import de.bundesbank.transreg.ui.propertyEditor.TransRegSettingsUI;
import ec.nbdemetra.ui.DemetraUiIcon;
import ec.nbdemetra.ui.NbComponents;
import ec.nbdemetra.ui.properties.l2fprod.PropertiesPanelFactory;
import ec.nbdemetra.ws.WorkspaceFactory;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.ui.WorkspaceTopComponent;
import static ec.ui.view.tsprocessing.DefaultProcessingViewer.BUTTONS;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.DropDownButtonFactory;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 *
 * @author s4504gn
 */
@TopComponent.Description(
        preferredID = "TransRegTopComponent",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "de.bundesbank.transReg.TransRegTopComponent")
@ActionReference(path = "Menu/Tools", position = 336)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_TransRegAction")
@NbBundle.Messages({
    "CTL_TransRegAction=TransReg",
    "CTL_TransRegTopComponent=TransReg Window",
    "HINT_TransRegTopComponent=This is a TransReg window"
})
public class TransRegTopComponent extends WorkspaceTopComponent<TransRegDocument> {

    private JToolBar toolBarRepresentation;
    private JButton runButton;
    private PropertySheetPanel propertyPanel;
    private TransRegVarOutlineView outlineview;

    private JLabel dropDataLabel;

    private static TransRegDocumentManager manager() {
        return WorkspaceFactory.getInstance().getManager(TransRegDocumentManager.class);
    }

    public TransRegTopComponent() {
        super(manager().create(WorkspaceFactory.getInstance().getActiveWorkspace()));
        initDocument();
    }

    public TransRegTopComponent(WorkspaceItem<TransRegDocument> doc) {
        super(doc);
        initDocument();
    }

    private void initDocument() {
        initComponents();
        setToolTipText(Bundle.HINT_TransRegTopComponent());
        setName(getDocument().getDisplayName());
    }

    @Override
    protected String getContextPath() {
        return TransRegDocumentManager.CONTEXTPATH;
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());

        //<editor-fold defaultstate="collapsed" desc="Calc-Button">
        JPanel buttonPanel = new JPanel();
        buttonPanel.setName(BUTTONS);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JButton calc = new JButton("Calculate");

        calc.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                TransRegVar var = outlineview.getSelectedVariable();
                if (var != null) {
                    TransRegDocument vars = outlineview.getVars();
                    if (var.hasChildren()) {
                        // falls Kinder vorhanden sind: alle löschen + Abhängigkeiten
                        // denn in calculate erfolgt kompletteneuberechnung
                        ArrayList<TransRegVar> deleteVars = var.deleteChildren();
                        for (TransRegVar t : deleteVars) {
                            vars.remove(t);
                        }
                    }

                    ArrayList<TransRegVar> calculated = TransRegCalculationTool.calculate(var);
                    for (TransRegVar child : calculated) {
                        vars.set(child.getName(), child);
                    }
                    outlineview.refresh();

                    outlineview.repaint();
                    outlineview.updateUI();
                }
            }
        });
        buttonPanel.add(calc);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="TransRegSettingsPropertyPanel">
        TransRegSettingsUI settingsUI = new TransRegSettingsUI();
        settingsUI.setReadOnly(true);
        propertyPanel = PropertiesPanelFactory.INSTANCE.createPanel(settingsUI);
        propertyPanel.setVisible(false);
        propertyPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(propertyPanel, BorderLayout.EAST);
        // TODO: Listner hinzufuegen, damit calc button blau wird, damit anwender Button drückt

        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="OutlineView">
        TransRegDocument regressors = getDocument().getElement();
        outlineview = new TransRegVarOutlineView(regressors);
        //<editor-fold defaultstate="collapsed" desc="Selected Row">
        /*
        *   If selected row in outlineview is changed -> valueChanged method
        *   1. get selected variable (var)
        *   2. update the (modified) settings from the view
        *   3. only root is modifiable, so all children have to be updated
        *   4. update view
        */
        outlineview.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            // if evt.equals
            if (evt.getPropertyName().equals(TransRegVarOutlineView.CHANGE_SELECTED_VAR)) {
                TransRegVar var = (TransRegVar) evt.getNewValue();
                if (var != null) {
                    // update settings
                    TransRegSettingsUI ui1 = new TransRegSettingsUI(var.getSettings());
                    // update all children
                    var.updateSettings(var.getSettings());
                    if (var.isRoot()) {
                        // only Root is modifiable
                        ui1.setReadOnly(false);
                        calc.setEnabled(true);
                    } else {
                        ui1.setReadOnly(true);
                        calc.setEnabled(false);
                    }
                    // update ui
                    PropertiesPanelFactory.INSTANCE.update(propertyPanel, ui1, null);
                    propertyPanel.setVisible(true);
                } else {
                    propertyPanel.setVisible(false);
                }
            }
        });
        //</editor-fold>
        add(outlineview);
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Toolbar">
        toolBarRepresentation = NbComponents.newInnerToolbar();
        
        dropDataLabel = new JLabel(" Drop data here");
        dropDataLabel.setVisible(true);
        dropDataLabel.setTransferHandler(new TransRegTransferHandler(outlineview));
        
        toolBarRepresentation.add(dropDataLabel);
        toolBarRepresentation.addSeparator();
        
        runButton = new JButton(DemetraUiIcon.COMPILE_16);
        runButton.setToolTipText("Refresh & calculate");
        
        toolBarRepresentation.add(runButton);
        toolBarRepresentation.setFloatable(false);
        toolBarRepresentation.addSeparator();
        
        // TODO: aus SaBatchUI, SpecVorauswahl anpassen an TransReg
        JPopupMenu preSelectionPopup = new JPopupMenu();
        JButton preSelectionButton = (JButton) toolBarRepresentation.add(DropDownButtonFactory.createDropDownButton(DemetraUiIcon.BLOG_16, preSelectionPopup));
        
        add(toolBarRepresentation, BorderLayout.NORTH);
//</editor-fold>
    }
}
