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
