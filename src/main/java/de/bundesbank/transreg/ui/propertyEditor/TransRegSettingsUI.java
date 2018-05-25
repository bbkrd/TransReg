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
    private boolean readOnly = false;

    public TransRegSettingsUI() {
        core = new TransRegSettings();
    }

    public TransRegSettingsUI(TransRegSettings t) {
        core = t;
    }

    public TransRegSettingsUI(int freq) {
        core = new TransRegSettings(freq);
    }

    public void setReadOnly(boolean b) {
        readOnly = b;
    }

    @Override
    public TransRegSettings getCore() {
        return core;
    }

    @Override
    public List<EnhancedPropertyDescriptor> getProperties() {
        ArrayList<EnhancedPropertyDescriptor> descs = new ArrayList<>();

        EnhancedPropertyDescriptor desc = groupsDesc(); // horizontalverticaldesc
        if (desc != null) {
            descs.add(desc);
        }
//          desc = horizontalDesc();
//        if (desc != null) {
//            descs.add(desc);
//        }

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

    private static final int Centeruser_ID = 2, Groups_ID = 1;

    @Messages({
        "transregSettingsUI.centeruserDesc.name=CENTRING",
        "transregSettingsUI.centeruserDesc.desc= "
    })
    private EnhancedPropertyDescriptor centeruserDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Centeruser", this.getClass(), "getCenteruser", null);
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, Centeruser_ID);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.transregSettingsUI_centeruserDesc_name());
            desc.setShortDescription(Bundle.transregSettingsUI_centeruserDesc_desc());
            edesc.setReadOnly(readOnly);
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public CenteruserSettingsUI getCenteruser() {
        CenteruserSettingsUI ui = new CenteruserSettingsUI(core.getCenteruser());
        ui.setReadOnly(readOnly);
        return ui;
    }

    @Messages({
        "transregSettingsUI.groupsDesc.name=GROUPING",
        "transregSettingsUI.groupsDesc.desc= "
    })
    private EnhancedPropertyDescriptor groupsDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Groups", this.getClass(), "getGroups", null);
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, Groups_ID);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.transregSettingsUI_groupsDesc_name());
            desc.setShortDescription(Bundle.transregSettingsUI_groupsDesc_desc());
            edesc.setReadOnly(readOnly);
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public GroupsSettingsUI getGroups() {
        GroupsSettingsUI ui = new GroupsSettingsUI(core.getGroups());
        ui.setDefaultValue(core.getGroups().getDefaultValue());
        ui.setReadOnly(readOnly);
        return ui;
    }

    @Messages({
        "transregSettingsUI.horizontalDesc.name=Horizontal GROUPS",
        "transregSettingsUI.horizontalDesc.desc= "
    })
    private EnhancedPropertyDescriptor horizontalDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Horizontal", this.getClass(), "getHorizontal", null);
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, Groups_ID);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.transregSettingsUI_horizontalDesc_name());
            desc.setShortDescription(Bundle.transregSettingsUI_horizontalDesc_desc());
            edesc.setReadOnly(readOnly);
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public HorizontalSettingsUI getHorizontal() {
        HorizontalSettingsUI ui = new HorizontalSettingsUI(core.getHorizontal());
        ui.setReadOnly(readOnly);
        return ui;
    }
}
