/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui.propertyEditor;

import de.bundesbank.transreg.settings.GroupsSettings;
import de.bundesbank.transreg.util.GroupsEnum;
import ec.nbdemetra.ui.properties.l2fprod.CustomPropertyEditorRegistry;
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
public class GroupsSettingsUI implements IObjectDescriptor<GroupsSettings> {

    static {
        CustomPropertyEditorRegistry.INSTANCE.registerEnumEditor(GroupsEnum.class);
        CustomPropertyEditorRegistry.INSTANCE.register(GroupsEnum[].class, new GroupsEnumPropertyEditor());
        //TODO: Button CustomPropertyRendererFactory.INSTANCE.getRegistry().registerRenderer(GroupsEnum[].class, new ArrayRenderer());
    }

    private GroupsSettings core;
    private boolean readOnly = false;

    public GroupsSettingsUI() {
        core = new GroupsSettings();
    }

    public GroupsSettingsUI(int freq) {
        core = new GroupsSettings(freq);
    }

    public GroupsSettingsUI(GroupsSettings g) {
        core = g;
    }

    public void setReadOnly(boolean b) {
        readOnly = b;
    }

    @Override
    public GroupsSettings getCore() {
        return core;
    }

    @Override
    public List<EnhancedPropertyDescriptor> getProperties() {
        ArrayList<EnhancedPropertyDescriptor> descs = new ArrayList<>();
        
        EnhancedPropertyDescriptor desc = enableDesc();
        if (desc != null) {
            descs.add(desc);
        }
        
        desc = groupsDesc();
        if (desc != null) {
            descs.add(desc);
        }
        return descs;
    }

    @Override
    public String getDisplayName() {
        return "Groups";
    }

    @NbBundle.Messages({
        "groupsSettingsUI.groupsDesc.name=Groups",
        "groupsSettingsUI.groupsDesc.desc=Erklärung"
    })
    private EnhancedPropertyDescriptor groupsDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Groups", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, 2);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.groupsSettingsUI_groupsDesc_name());
            desc.setShortDescription(Bundle.groupsSettingsUI_groupsDesc_desc());
            edesc.setReadOnly(!(!readOnly && core.isEnable()));
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public GroupsEnum[] getGroups() {
        return core.getGroups();
    }

    public void setGroups(GroupsEnum[] g) {
        core.setGroups(g);
    }
    
    @NbBundle.Messages({
        "groupsSettingsUI.enableDesc.name=Vertical Groups",
        "groupsSettingsUI.enableDesc.desc=Erklärung"
    })
    private EnhancedPropertyDescriptor enableDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Enable", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, 1);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.groupsSettingsUI_enableDesc_name());
            desc.setShortDescription(Bundle.groupsSettingsUI_enableDesc_desc());
            edesc.setReadOnly(readOnly);
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }
    
    public boolean isEnable(){
        return core.isEnable();
    }
    
    public void setEnable(boolean b){
        core.setEnable(b);
    }
}
