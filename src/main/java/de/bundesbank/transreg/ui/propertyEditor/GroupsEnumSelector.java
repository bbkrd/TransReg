/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui.propertyEditor;

import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import de.bundesbank.transreg.options.TransRegOptionsPanelController;
import de.bundesbank.transreg.util.GroupsEnum;
import java.awt.Component;
import java.util.Arrays;
import java.util.prefs.Preferences;
import javax.swing.JComboBox;
import org.openide.util.NbPreferences;

/**
 *
 * @author s4504gn
 */
public class GroupsEnumSelector extends ComboBoxPropertyEditor {

    @Override
    public Component getCustomEditor() {
        GroupsEnum[] groups = GroupsEnum.values();

        Preferences node = NbPreferences.forModule(TransRegOptionsPanelController.class);
        int count = node.getInt(TransRegOptionsPanelController.TRANSREG_GROUPS, 2);

        setAvailableValues(Arrays.copyOfRange(groups, 0, count));

        JComboBox box = (JComboBox) super.getCustomEditor();
        return box;

    }
}
