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

import de.bundesbank.transreg.settings.CenteruserSettings;
import de.bundesbank.transreg.util.CenteruserEnum;
import ec.nbdemetra.ui.properties.l2fprod.CustomPropertyEditorRegistry;
import ec.tstoolkit.descriptors.EnhancedPropertyDescriptor;
import ec.tstoolkit.descriptors.IObjectDescriptor;
import ec.ui.descriptors.TsPeriodSelectorUI;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Nina Gonschorreck
 */
public class CenteruserSettingsUI implements IObjectDescriptor<CenteruserSettings> {

    static {
//        CustomPropertyEditorRegistry.INSTANCE.registerEnumEditor(CenteruserEnum.class, null);
        CustomPropertyEditorRegistry.INSTANCE.register(String.class, null);
    }

    private final CenteruserSettings core;
    private boolean readOnly = false;

    public CenteruserSettingsUI() {
        core = new CenteruserSettings();
    }

    public CenteruserSettingsUI(CenteruserSettings c) {
        core = c;
    }

    public void setReadOnly(boolean b) {
        readOnly = b;
    }

    @Override
    public CenteruserSettings getCore() {
        return core;
    }

    @Override
    public List<EnhancedPropertyDescriptor> getProperties() {
        ArrayList<EnhancedPropertyDescriptor> descs = new ArrayList<>();
        EnhancedPropertyDescriptor desc = methodDesc();
        if (desc != null) {
            descs.add(desc);
        }

        desc = spanDesc();
        if (desc != null) {
            descs.add(desc);
        }

        desc = extendingDesc();
        if (desc != null) {
            descs.add(desc);
        }

        return descs;
    }

    @Override
    public String getDisplayName() {
        return "Center user";
    }

    private static final int Method_ID = 1, Span_ID = 2, Extending_ID = 3;

    @Messages({
        "centeruserSettingsUI.methodDesc.name=Sample mean",
        "centeruserSettingsUI.methodDesc.desc=Type of sample mean used for centring."
    })
    private EnhancedPropertyDescriptor methodDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Method", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, Method_ID);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.centeruserSettingsUI_methodDesc_name());
            desc.setShortDescription(Bundle.centeruserSettingsUI_methodDesc_desc());
            edesc.setReadOnly(readOnly);
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public CenteruserEnum getMethod() {
        return core.getMethod();
    }

    public void setMethod(CenteruserEnum e) {
        core.setMethod(e);
    }

    @NbBundle.Messages({
        "centeruserSettingsUI.spanDesc.name=Calculation span",
        "centeruserSettingsUI.spanDesc.desc=Span used to calculate the sample mean specified by method."
    })
    private EnhancedPropertyDescriptor spanDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Span", this.getClass(), "getSpan", null);
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, Span_ID);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.centeruserSettingsUI_spanDesc_name());
            desc.setShortDescription(Bundle.centeruserSettingsUI_spanDesc_desc());
            edesc.setReadOnly(readOnly || core.getMethod().equals(CenteruserEnum.None));
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public TsPeriodSelectorUI getSpan() {
        return new TsPeriodSelectorUI(core.getSpan(), readOnly || core.getMethod().equals(CenteruserEnum.None));
    }

    @NbBundle.Messages({
        "centeruserSettingsUI.extendingDesc.name=Extending",
        "centeruserSettingsUI.extendingDesc.desc=Length to extend regressor in periods (positive values) or years (negative values)."
    })
    private EnhancedPropertyDescriptor extendingDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Extending", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, Extending_ID);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.centeruserSettingsUI_extendingDesc_name());
            desc.setShortDescription(Bundle.centeruserSettingsUI_extendingDesc_desc());
            edesc.setReadOnly(!(!readOnly && !(CenteruserEnum.None.equals(core.getMethod()))));
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public int getExtending() {
        return core.getExtendingPeriods();
    }

    public void setExtending(int e) {
        core.setExtendingPeriods(e);
    }
}
