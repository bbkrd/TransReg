/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui;

import com.l2fprod.common.propertysheet.PropertySheetPanel;
import de.bundesbank.transreg.admin.TransRegController;
import de.bundesbank.transreg.admin.TransRegDocumentManager;
import de.bundesbank.transreg.admin.TransRegDocument;
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
import java.beans.PropertyChangeEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
    private TransRegVarList variablesList;
    private PropertySheetPanel propertyPanel;
    
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
    
    public TransRegVarList getVariablesList() {
        return variablesList;
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
        
        toolBarRepresentation = NbComponents.newInnerToolbar();
        runButton = new JButton(DemetraUiIcon.COMPILE_16);
        runButton.setToolTipText("Refresh & calculate");
        toolBarRepresentation.add(runButton);
        toolBarRepresentation.setFloatable(false);
        toolBarRepresentation.addSeparator();

        // TODO: aus SaBatchUI, SpecVorauswahl anpassen an TransReg
        JPopupMenu preSelectionPopup = new JPopupMenu();
        JButton preSelectionButton = (JButton) toolBarRepresentation.add(DropDownButtonFactory.createDropDownButton(DemetraUiIcon.BLOG_16, preSelectionPopup));
        
        TransRegDocument regressors = getDocument().getElement();
        variablesList = new TransRegVarList(regressors);

        //<editor-fold defaultstate="collapsed" desc="Buttons">
        JPanel buttonPanel = new JPanel();
        buttonPanel.setName(BUTTONS);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JButton calc = new JButton("Calculate");

//        calc.addActionListener(new TransRegController(this));
        calc.addActionListener((ActionEvent e) -> {
            TransRegVar var = variablesList.getSelectedVariable();
            if (var != null) {
                var.calculate();
                variablesList.refresh();
                // refresh getTsData Anzeige
            }
        });
        buttonPanel.add(calc);
        
        JButton restore = new JButton("Restore");
        restore.addActionListener((ActionEvent e) -> {
            TransRegVar var = variablesList.getSelectedVariable();
            if (var != null) {
                var.restore();
                variablesList.refresh();
            }
        });
        buttonPanel.add(restore);
        //</editor-fold>

        TransRegSettingsUI settingsUI = new TransRegSettingsUI();
        settingsUI.setReadOnly(true);
        propertyPanel = PropertiesPanelFactory.INSTANCE.createPanel(settingsUI);
        
        propertyPanel.setVisible(false);
        
        propertyPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(toolBarRepresentation, BorderLayout.NORTH);
        add(propertyPanel, BorderLayout.EAST);
        add(variablesList);
        
        variablesList.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            if (TransRegVarList.SELECTION_CHANGE.equals(evt.getPropertyName())) {
                if (evt.getNewValue() != null) {
                    PropertiesPanelFactory.INSTANCE.update(propertyPanel, new TransRegSettingsUI(((TransRegVar) evt.getNewValue()).getSettings()), null);
                    propertyPanel.setVisible(true);
                } else {
                    propertyPanel.setVisible(false);
                }
            }
        });
    }
}
