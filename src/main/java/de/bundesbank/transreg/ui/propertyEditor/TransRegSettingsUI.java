/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui.propertyEditor;

import de.bundesbank.transreg.settings.TransRegSettings;
import ec.tstoolkit.descriptors.EnhancedPropertyDescriptor;
import ec.tstoolkit.descriptors.IObjectDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author s4504gn
 */
public class TransRegSettingsUI implements IObjectDescriptor<TransRegSettings> {

    private TransRegSettings core;

    public TransRegSettingsUI() {
        core = new TransRegSettings();
    }

    public TransRegSettingsUI(TransRegSettings t) {
        core = t;
    }

    public TransRegSettingsUI(int freq) {
        core = new TransRegSettings(freq);
    }

    @Override
    public TransRegSettings getCore() {
        return core;
    }

    @Override
    public List<EnhancedPropertyDescriptor> getProperties() {
        ArrayList<EnhancedPropertyDescriptor> descs = new ArrayList<>();
        EnhancedPropertyDescriptor desc = groupsDesc();
        if (desc != null) {
            descs.add(desc);
        }
        desc = centeruserDesc();
        if (desc != null) {
            descs.add(desc);
        }
        return descs;
    }

    @Override
    public String getDisplayName() {
        return "TransReg Settings";
    }

    private static final int Centeruser_ID = 1, Groups_ID = 2;

    @Messages({
        "transregSettingsUI.centeruserDesc.name=CENTER USER",
        "transregSettingsUI.centeruserDesc.desc=Erklärung"
    })
    private EnhancedPropertyDescriptor centeruserDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Centeruser", this.getClass(), "getCenteruser", null);
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, Centeruser_ID);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.transregSettingsUI_centeruserDesc_name());
            desc.setShortDescription(Bundle.transregSettingsUI_centeruserDesc_desc());
            edesc.setReadOnly(false);
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public CenteruserSettingsUI getCenteruser() {
        return new CenteruserSettingsUI(core.getCenteruser());
    }

    @Messages({
        "transregSettingsUI.groupsDesc.name=GROUPS",
        "transregSettingsUI.groupsDesc.desc=Erklärung"
    })
    private EnhancedPropertyDescriptor groupsDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Groups", this.getClass(), "getGroups", null);
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, Groups_ID);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.transregSettingsUI_groupsDesc_name());
            desc.setShortDescription(Bundle.transregSettingsUI_groupsDesc_desc());
            edesc.setReadOnly(false);
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public GroupsSettingsUI getGroups() {
        return new GroupsSettingsUI(core.getGroups());
    }
}
