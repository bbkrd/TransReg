/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui.propertyEditor;

import de.bundesbank.transreg.settings.GroupsSettings;
import de.bundesbank.transreg.settings.HorizontalSettings;
import ec.tstoolkit.descriptors.EnhancedPropertyDescriptor;
import ec.tstoolkit.descriptors.IObjectDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.NbBundle;

/**
 *
 * @author s4504gn
 */
public class HorizontalSettingsUI implements IObjectDescriptor<HorizontalSettings> {

    private HorizontalSettings core;
    private boolean readOnly = false;

    HorizontalSettingsUI(HorizontalSettings groups) {
        core = groups;
    }

    @Override
    public HorizontalSettings getCore() {
        return core;
    }

    public void setReadOnly(boolean b) {
        readOnly = b;
    }

    @Override
    public List<EnhancedPropertyDescriptor> getProperties() {
        ArrayList<EnhancedPropertyDescriptor> descs = new ArrayList<>();

        EnhancedPropertyDescriptor desc = enableDesc();
        if (desc != null) {
            descs.add(desc);
        }

//        desc = groupsDesc();
//        if (desc != null) {
//            descs.add(desc);
//        }
        return descs;
    }

    @Override
    public String getDisplayName() {
        return "Horizontal";
    }

    @NbBundle.Messages({
        "horizontalSettingsUI.enableDesc.name=Horizontal Groups",
        "horizontalSettingsUI.enableDesc.desc=Erklärung"
    })
    private EnhancedPropertyDescriptor enableDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Enable", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, 1);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.horizontalSettingsUI_enableDesc_name());
            desc.setShortDescription(Bundle.horizontalSettingsUI_enableDesc_desc());
            edesc.setReadOnly(readOnly);
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public boolean isEnable() {
        return core.isEnable();
    }

    public void setEnable(boolean b) {
        core.setEnable(b);
    }
}
