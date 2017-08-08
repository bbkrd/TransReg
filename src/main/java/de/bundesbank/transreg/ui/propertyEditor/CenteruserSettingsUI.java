/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author s4504gn
 */
public class CenteruserSettingsUI implements IObjectDescriptor<CenteruserSettings> {
    static{
        CustomPropertyEditorRegistry.INSTANCE.registerEnumEditor(CenteruserEnum.class);
    }

    private CenteruserSettings core;
    private boolean readOnly = false;

    public CenteruserSettingsUI() {
        core = new CenteruserSettings();
    }

    public CenteruserSettingsUI(CenteruserSettings c) {
        core = c;
    }

    public void setReadOnly(boolean b){
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
        return descs;
    }

    @Override
    public String getDisplayName() {
        return "Center user";
    }

    private static final int Method_ID = 1, Span_ID = 2;

    @Messages({
        "centeruserSettingsUI.methodDesc.name=Method",
        "centeruserSettingsUI.methodDesc.desc=Type of sample mean used for centring"
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
        "centeruserSettingsUI.spanDesc.name=Mean calculation span",
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

}
