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
package de.bundesbank.transreg.ui;

import de.bundesbank.transreg.settings.admin.SettingSelectionComponent;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import de.bundesbank.transreg.admin.TransRegDocument;
import de.bundesbank.transreg.admin.TransRegDocumentManager;
import de.bundesbank.transreg.admin.TransRegTransferHandler;
import de.bundesbank.transreg.logic.TransRegCalculationTool;
import de.bundesbank.transreg.logic.TransRegVar;
import de.bundesbank.transreg.settings.TransRegSettings;
import de.bundesbank.transreg.ui.nodes.NodesLevelEnum;
import de.bundesbank.transreg.ui.propertyEditor.TransRegSettingsUI;
import ec.nbdemetra.ui.DemetraUiIcon;
import ec.nbdemetra.ui.NbComponents;
import ec.nbdemetra.ui.awt.PopupMenuAdapter;
import ec.nbdemetra.ui.properties.l2fprod.PropertiesPanelFactory;
import ec.nbdemetra.ws.WorkspaceFactory;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.ui.WorkspaceTopComponent;
import ec.tstoolkit.timeseries.regression.ITsVariable;
import static ec.ui.view.tsprocessing.DefaultProcessingViewer.BUTTONS;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.PopupMenuEvent;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.DropDownButtonFactory;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 *
 * @author Nina Gonschorreck
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
    
    public static final String DEFAULT_SETTING_PROPERTY = "settingsProperty";
    
    private JToolBar toolBarRepresentation;
    private PropertySheetPanel propertyPanel;
    private TransRegVarOutlineView outlineview;
    
    private JLabel dropDataLabel;
    private JLabel defSettingLabel;
    private JButton runButton;
    
    private TransRegSettings currentSetting = TransRegSettings.DEFAULT;
    
    private static TransRegDocumentManager manager() {
        return WorkspaceFactory.getInstance().getManager(TransRegDocumentManager.class);
    }
    
    public TransRegTopComponent() {
        super(manager().create(WorkspaceFactory.getInstance().getActiveWorkspace()));
        currentSetting = super.getDocument().getElement().getSpecification();
        initDocument();
    }
    
    public TransRegTopComponent(WorkspaceItem<TransRegDocument> doc) {
        super(doc);
        currentSetting = doc.getElement().getSpecification();
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
        
        calc.addActionListener((ActionEvent e) -> {
            TransRegVar var = outlineview.getSelectedVariable();
            if (var != null) {
                TransRegDocument vars = outlineview.getVars();
                if (var.hasChildren()) {
                    ArrayList<TransRegVar> deleteVars = var.deleteChildren();
                    deleteVars.stream().forEach((t) -> {
                        vars.remove(t);
                    });
                }
                
                HashMap<NodesLevelEnum, ArrayList<TransRegVar>> calculated = TransRegCalculationTool.calculate(var);
                calculated.values().forEach((a) -> {
                    a.stream().forEach((child) -> {
                        vars.set(child.getName(), child);
                    });
                });
                
                outlineview.refresh();
                outlineview.repaint();
            }
        });
        buttonPanel.add(calc);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="TransRegSettingsPropertyPanel">
        TransRegSettingsUI settingsUI = new TransRegSettingsUI(currentSetting);
        settingsUI.setReadOnly(true);
        propertyPanel = PropertiesPanelFactory.INSTANCE.createPanel(settingsUI);
        propertyPanel.setVisible(false);
        propertyPanel.add(buttonPanel, BorderLayout.SOUTH);
        propertyPanel.setDescriptionVisible(true);
        add(propertyPanel, BorderLayout.EAST);

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="OutlineView">
        TransRegDocument regressors = getDocument().getElement();
        outlineview = new TransRegVarOutlineView(regressors);
        outlineview.setStartSettings(currentSetting);
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
                    propertyPanel.setDescriptionVisible(true);
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

        //<editor-fold defaultstate="collapsed" desc="Data drop here">
        dropDataLabel = new JLabel("Drop data here");
        dropDataLabel.setFont(new JLabel().getFont().deriveFont(Font.ITALIC));
        dropDataLabel.setPreferredSize(new Dimension(200, 40));
        dropDataLabel.setVisible(true);
        dropDataLabel.setTransferHandler(new TransRegTransferHandler(outlineview));
        toolBarRepresentation.add(dropDataLabel);
        toolBarRepresentation.addSeparator();
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Settings">
        JPopupMenu specPopup = new JPopupMenu();
        final JButton specButton = (JButton) toolBarRepresentation.add(DropDownButtonFactory.createDropDownButton(DemetraUiIcon.BLOG_16, specPopup));
        specPopup.add(new SettingSelectionComponent()).addPropertyChangeListener(evt -> {
            String p = evt.getPropertyName();
            if (p.equals(SettingSelectionComponent.SETTING_PROPERTY) && evt.getNewValue() != null) {
                setDefaultSetting((TransRegSettings) evt.getNewValue());
                outlineview.setStartSettings((TransRegSettings) evt.getNewValue());
            } else if (p.equals(SettingSelectionComponent.ICON_PROPERTY) && evt.getNewValue() != null) {
                specButton.setIcon(ImageUtilities.image2Icon((Image) evt.getNewValue()));
            }
        });
        
        specPopup.addPopupMenuListener(new PopupMenuAdapter() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                ((SettingSelectionComponent) ((JPopupMenu) e.getSource()).getComponent(0)).setSetting(currentSetting);
            }
        });
        
        defSettingLabel = (JLabel) toolBarRepresentation.add(new JLabel());
        defSettingLabel.setText(currentSetting == null ? "" : currentSetting.toString());

        //</editor-fold>
        toolBarRepresentation.addSeparator();

        //<editor-fold defaultstate="collapsed" desc="Runbutton">
        runButton = toolBarRepresentation.add(new AbstractAction("", DemetraUiIcon.COMPILE_16) {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransRegDocument vars = outlineview.getVars();
                ITsVariable[] varsArray = vars.variables().toArray(new ITsVariable[0]);
                for (ITsVariable v : varsArray) {
                    if (v instanceof TransRegVar) {
                        TransRegVar var = ((TransRegVar) v);
                        if (var.isRoot()) {
                            // falls Kinder vorhanden sind: alle löschen + Abhängigkeiten
                            // denn in calculate erfolgt kompletteneuberechnung
                            ArrayList<TransRegVar> deleteVars = var.deleteChildren();
                            deleteVars.stream().forEach((t) -> {
                                vars.remove(t);
                            });
                            
                            HashMap<NodesLevelEnum, ArrayList<TransRegVar>> calculated
                                    = TransRegCalculationTool.calculate(var);
                            
                            calculated.values().forEach((a) -> {
                                a.stream().forEach((child) -> {
                                    try {
                                        vars.set(child.getName(), child);
                                    } catch (Exception nothing) {
                                        System.out.println("Problem mit: " + child.getName());
                                    }
                                });
                            });
                        }
                    }
                }
//                runButton.setEnabled(false);
                outlineview.refresh();
                outlineview.repaint();
            }
            
        });
        runButton.setDisabledIcon(ImageUtilities.createDisabledIcon(runButton.getIcon()));
        //</editor-fold>

        add(toolBarRepresentation, BorderLayout.NORTH);
//</editor-fold>
    }
    
    @Override
    public void refresh() {
        outlineview.refresh();
        outlineview.repaint();
    }
    
    public void setDefaultSetting(TransRegSettings setting) {
        TransRegSettings old = this.currentSetting;
        this.currentSetting = setting;
        defSettingLabel.setText(currentSetting == null ? "Test" : currentSetting.toString());
        firePropertyChange(DEFAULT_SETTING_PROPERTY, old, this.currentSetting);
    }
    
    public void editDefaultSetting() {
        SettingSelectionComponent c = new SettingSelectionComponent();
        c.setSetting(currentSetting);
        DialogDescriptor dd = c.createDialogDescriptor("Choose active specification");
        if (DialogDisplayer.getDefault().notify(dd) == NotifyDescriptor.OK_OPTION) {
            setDefaultSetting(c.getSetting());
        }
    }
}
