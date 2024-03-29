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
package de.bundesbank.transreg.ui.propertyEditor;

import de.bundesbank.transreg.settings.GroupsSettings;
import de.bundesbank.transreg.util.Group;
import de.bundesbank.transreg.util.DefaultValueEnum;
import ec.nbdemetra.ui.properties.l2fprod.CustomPropertyEditorRegistry;
import ec.nbdemetra.ui.properties.l2fprod.CustomPropertyRendererFactory;
import ec.tstoolkit.descriptors.EnhancedPropertyDescriptor;
import ec.tstoolkit.descriptors.IObjectDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import org.openide.util.NbBundle;

/**
 *
 * @author Nina Gonschorreck
 */
public class GroupsSettingsUI implements IObjectDescriptor<GroupsSettings> {

    static {
        CustomPropertyEditorRegistry.INSTANCE.register(Group[].class, new GroupsEnumPropertyEditor());
        CustomPropertyRendererFactory.INSTANCE.getRegistry().registerRenderer(Group[].class, new ArrayRenderer());
        CustomPropertyEditorRegistry.INSTANCE.register(DefaultValueEnum.class, null);
        CustomPropertyEditorRegistry.INSTANCE.register(String.class, null);
    }

    private final GroupsSettings core;
    private boolean readOnly = false;

    public GroupsSettingsUI() {
        core = new GroupsSettings();
        readOnly = true;
    }

    public GroupsSettingsUI(int freq) {
        core = new GroupsSettings(freq);
        readOnly = false;
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

        desc = defaultValueDesc();
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
        "groupsSettingsUI.groupsDesc.desc= Assign periods to period-specific regresion variables."
    })
    private EnhancedPropertyDescriptor groupsDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Groups", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, 3);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.groupsSettingsUI_groupsDesc_name());
            desc.setShortDescription(Bundle.groupsSettingsUI_groupsDesc_desc());
            edesc.setReadOnly(!(!readOnly && core.isEnabled()));
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public Group[] getGroups() {
        return core.getGroups();
    }

    public void setGroups(Group[] g) {
        core.setGroups(g);
    }

    @NbBundle.Messages({
        "groupsSettingsUI.enableDesc.name=Enabled",
        "groupsSettingsUI.enableDesc.desc= Mark checkbox to split regression variable into period-specific variants."
    })
    private EnhancedPropertyDescriptor enableDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Enable", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, 2);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.groupsSettingsUI_enableDesc_name());
            desc.setShortDescription(Bundle.groupsSettingsUI_enableDesc_desc());
            edesc.setReadOnly(readOnly);
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public boolean isEnable() {
        return core.isEnabled();
    }

    public void setEnable(boolean b) {
        core.setEnabled(b);
    }

    @NbBundle.Messages({
        "groupsSettingsUI.defaultValueDesc.name=Value for inactive periods",
        "groupsSettingsUI.defaultValueDesc.desc=Value assigned to inactive periods of period-specific regression variables."
    })
    private EnhancedPropertyDescriptor defaultValueDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("DefaultValue", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, 1);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.groupsSettingsUI_defaultValueDesc_name());
            desc.setShortDescription(Bundle.groupsSettingsUI_defaultValueDesc_desc());
            edesc.setReadOnly(!(!readOnly && core.isEnabled()));
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public DefaultValueEnum getDefaultValue() {
        return core.getDefaultValue();
    }

    public void setDefaultValue(DefaultValueEnum b) {
        core.setDefaultValue(b);
    }
}

class ArrayRenderer extends DefaultTableCellRenderer {

    public ArrayRenderer() {
        super();
    }

    @Override
    protected void setValue(Object value) {
        //super.setValue(value);
        setText("");
    }

}
