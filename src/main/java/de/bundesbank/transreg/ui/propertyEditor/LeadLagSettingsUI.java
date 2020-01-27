package de.bundesbank.transreg.ui.propertyEditor;

import de.bundesbank.transreg.settings.LeadLagSettings;
import de.bundesbank.transreg.util.LeadLagEnum;
import ec.nbdemetra.ui.properties.l2fprod.CustomPropertyEditorRegistry;
import ec.tstoolkit.descriptors.EnhancedPropertyDescriptor;
import ec.tstoolkit.descriptors.IObjectDescriptor;
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
public class LeadLagSettingsUI implements IObjectDescriptor<LeadLagSettings> {

    static {
        CustomPropertyEditorRegistry.INSTANCE.register(String.class, null);
        CustomPropertyEditorRegistry.INSTANCE.register(Integer.class, null);
    }

    private final LeadLagSettings core;
    private boolean readOnly = false;

    public LeadLagSettingsUI() {
        core = new LeadLagSettings();
    }

    public LeadLagSettingsUI(LeadLagSettings settings) {
        core = settings;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public LeadLagSettings getCore() {
        return core;
    }

    @Override
    public List<EnhancedPropertyDescriptor> getProperties() {
        ArrayList<EnhancedPropertyDescriptor> descs = new ArrayList<>();
        EnhancedPropertyDescriptor desc = enableDesc();
        if (desc != null) {
            descs.add(desc);
        }

        desc = methodDesc();
        if (desc != null) {
            descs.add(desc);
        }

        desc = periodsDesc();
        if (desc != null) {
            descs.add(desc);
        }

        return descs;
    }

    @Override
    public String getDisplayName() {
        return "Lead/Lag";
    }

    @NbBundle.Messages({
        "leadLagSettingsUI.enableDesc.name=Enable",
        "leadLagSettingsUI.enableDesc.desc= Mark checkbox to lead or lad the regressor."
    })
    private EnhancedPropertyDescriptor enableDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Enable", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, 1);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.leadLagSettingsUI_enableDesc_name());
            desc.setShortDescription(Bundle.leadLagSettingsUI_enableDesc_desc());
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

    @Messages({
        "leadLagSettingsUI.methodDesc.name=Lead/Lag",
        "leadLagSettingsUI.methodDesc.desc=..."
    })
    private EnhancedPropertyDescriptor methodDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Method", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, 2);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.leadLagSettingsUI_methodDesc_name());
            desc.setShortDescription(Bundle.leadLagSettingsUI_methodDesc_desc());
            edesc.setReadOnly(!(!readOnly && core.isEnabled()));
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public LeadLagEnum getMethod() {
        return core.getMethod();
    }

    public void setMethod(LeadLagEnum e) {
        core.setMethod(e);
    }

    @NbBundle.Messages({
        "leadLagSettingsUI.endDesc.name=Number of periods",
        "leadLagSettingsUI.endDesc.desc=..."
    })
    private EnhancedPropertyDescriptor periodsDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Periods", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, 3);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.leadLagSettingsUI_endDesc_name());
            desc.setShortDescription(Bundle.leadLagSettingsUI_endDesc_desc());
            edesc.setReadOnly(!(!readOnly && core.isEnabled()));
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public int getPeriods() {
        return core.getnPeriods();
    }

    public void setPeriods(int e) {
        core.setnPeriods(e);
    }
}
