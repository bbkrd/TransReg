package de.bundesbank.transreg.ui.propertyEditor;

import de.bundesbank.transreg.settings.EpochSettings;
import de.bundesbank.transreg.util.Epoch;
import de.bundesbank.transreg.util.DefaultValueEnum;
import ec.nbdemetra.ui.properties.l2fprod.CustomPropertyEditorRegistry;
import ec.nbdemetra.ui.properties.l2fprod.CustomPropertyRendererFactory;
import ec.tstoolkit.descriptors.EnhancedPropertyDescriptor;
import ec.tstoolkit.descriptors.IObjectDescriptor;
import ec.tstoolkit.timeseries.Day;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.NbBundle;

/**
 *
 * @author Nina Gonschorreck
 */
public class EpochSettingsUI implements IObjectDescriptor<EpochSettings> {

    private final EpochSettings core;
    private boolean readOnly = false;

    static {
        CustomPropertyEditorRegistry.INSTANCE.register(String.class, null);
        CustomPropertyEditorRegistry.INSTANCE.register(Epoch[].class, new EpochEditor());
        CustomPropertyEditorRegistry.INSTANCE.register(DefaultValueEnum.class, null);
        CustomPropertyRendererFactory.INSTANCE.getRegistry().registerRenderer(Epoch[].class, new ArrayRenderer());
    }
    
    public EpochSettingsUI(Day start, Day end) {
        core = new EpochSettings(start, end);
    }

    public EpochSettingsUI(EpochSettings epoch) {
        core = epoch;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public EpochSettings getCore() {
        return core;
    }

    @Override
    public List<EnhancedPropertyDescriptor> getProperties() {
        ArrayList<EnhancedPropertyDescriptor> descs = new ArrayList<>();
        
        EnhancedPropertyDescriptor desc = enableDesc();
        if (desc != null) {
            descs.add(desc);
        }

        desc = epochDesc();
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
        return "Regime";
    }

    @NbBundle.Messages({
        "epochSettingsUI.enableDesc.name=Enabled",
        "epochSettingsUI.enableDesc.desc= Mark checkbox to customise the active period."
    })
    private EnhancedPropertyDescriptor enableDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Enable", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, 1);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.epochSettingsUI_enableDesc_name());
            desc.setShortDescription(Bundle.epochSettingsUI_enableDesc_desc());
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
        "epochSettingsUI.epochDesc.name=Regimes",
        "epochSettingsUI.epochDesc.desc=Assign active period."
    })
    private EnhancedPropertyDescriptor epochDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("Epoch", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, 2);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.epochSettingsUI_epochDesc_name());
            desc.setShortDescription(Bundle.epochSettingsUI_epochDesc_desc());
            edesc.setReadOnly(!(!readOnly && core.isEnabled()));
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    public Epoch[] getEpoch() {
        return core.getEpochs(); //.toArray(new Epoch[0]);
    }

    public void setEpoch(Epoch[] e) {
        core.setEpochs(e);
    }

    @NbBundle.Messages({
        "epochSettingsUI.defaultValueDesc.name=Value for inactive periods",
        "epochSettingsUI.defaultValueDesc.desc=Value assigned to inactive periods."
    })
    private EnhancedPropertyDescriptor defaultValueDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("DefaultValue", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, 3);
            edesc.setRefreshMode(EnhancedPropertyDescriptor.Refresh.All);
            desc.setDisplayName(Bundle.epochSettingsUI_defaultValueDesc_name());
            desc.setShortDescription(Bundle.epochSettingsUI_defaultValueDesc_desc());
            edesc.setReadOnly(!(!readOnly && core.isEnabled()));
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }
    
    public void setDefaultValue(DefaultValueEnum value){
        core.setDefaultValue(value);
    }
    
    public DefaultValueEnum getDefaultValue(){
        return core.getDefaultValue();
    }
}
