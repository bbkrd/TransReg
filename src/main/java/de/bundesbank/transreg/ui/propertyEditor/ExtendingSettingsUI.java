package de.bundesbank.transreg.ui.propertyEditor;

import de.bundesbank.transreg.settings.ExtendingSettings;
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
 * @author Nina Gonschorreck
 */
public class ExtendingSettingsUI implements IObjectDescriptor<ExtendingSettings> {

    static {
        CustomPropertyEditorRegistry.INSTANCE.register(String.class, null);
        CustomPropertyEditorRegistry.INSTANCE.register(Integer.class, null);
    }

    private final ExtendingSettings core;
    private boolean readOnly = false;

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public ExtendingSettingsUI() {
        core = new ExtendingSettings();
    }

    public ExtendingSettingsUI(ExtendingSettings extending) {
        core = extending;
    }

    @Override
    public ExtendingSettings getCore() {
        return core;
    }

    @Override
    public List<EnhancedPropertyDescriptor> getProperties() {
        ArrayList<EnhancedPropertyDescriptor> descs = new ArrayList<>();
        EnhancedPropertyDescriptor desc = enableDesc();
        if (desc != null) {
            descs.add(desc);
        }

        desc = endDesc();
        if (desc != null) {
            descs.add(desc);
        }
        return descs;
    }

    @Override
    public String getDisplayName() {
        return "Extending";
    }

    @NbBundle.Messages({
        "extendingSettingsUI.enableDesc.name=Enable",
        "extendingSettingsUI.enableDesc.desc= Mark checkbox to extend the regressor."
    })
    private EnhancedPropertyDescriptor enableDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Enable", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, 1);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.extendingSettingsUI_enableDesc_name());
            desc.setShortDescription(Bundle.extendingSettingsUI_enableDesc_desc());
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
        "extendingSettingsUI.endDesc.name=Number of periods",
        "extendingSettingsUI.endDesc.desc=..."
    })
    private EnhancedPropertyDescriptor endDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("End", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, 2);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.extendingSettingsUI_endDesc_name());
            desc.setShortDescription(Bundle.extendingSettingsUI_endDesc_desc());
            edesc.setReadOnly(!(!readOnly && core.isEnabled()));
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public int getEnd() {
        return core.getEnd();
    }
    
    public void setEnd(int e){
        core.setEnd(e);
    }
}
