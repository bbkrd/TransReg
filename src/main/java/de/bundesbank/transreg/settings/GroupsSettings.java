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
package de.bundesbank.transreg.settings;

import de.bundesbank.transreg.util.Group;
import de.bundesbank.transreg.util.DefaultValueEnum;
import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.information.InformationSetSerializable;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Nina Gonschorreck
 */
@lombok.Data
public class GroupsSettings implements InformationSetSerializable {
    
    private Group[] groups = new Group[]{new Group(1)};
    private boolean enabled = false;
    private DefaultValueEnum defaultValue = DefaultValueEnum.ZERO;
    
    public GroupsSettings() {
        
    }
    
    public GroupsSettings(int freq) {
//        enable = true;
        generateGroups(freq);
    }
    
    private void generateGroups(int freq) {
        groups = new Group[freq];
        Arrays.fill(groups, new Group(1));
    }
    
    public void setFreq(int f) {
        generateGroups(f);
    }

    public int getFreq() {
        if (enabled) {
            return groups.length;
        }
        return 0;
    }
    
    public boolean isDefault() {
        if (defaultValue.equals(DefaultValueEnum.NaN)) {
            return false;
        }
        // Default ist definiert als alle Gruppen mit Status Group1
        boolean result = true;
        for (Group g : groups) {
            result = result && g.equals(new Group(1));
        }
        return result || !enabled;
    }
    
    public GroupsSettings copy() {
        
        GroupsSettings copy = new GroupsSettings();
        copy.setGroups(groups.clone());
        copy.setDefaultValue(defaultValue);
        
        return copy;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Group g : groups) {
            result.append(g.toString());
        }
        return result.toString().trim();
    }
    
    private static String GROUPS = "groups", ENABLE = "enable", DEFAULTVALUE = "defaultvalue";
    
    @Override
    public InformationSet write(boolean verbose) {
        InformationSet info = new InformationSet();
        if (groups != null) {
            info.add(GROUPS, groups);
        }
        info.add(ENABLE, enabled);
        info.add(DEFAULTVALUE, defaultValue);
        
        return info;
    }
    
    @Override
    public boolean read(InformationSet info) {
        Group[] groupsInfo = info.get(GROUPS, Group[].class);
        if (groupsInfo != null) {
            groups = groupsInfo;
        }
        enabled = info.get(ENABLE, Boolean.class);
        defaultValue = DefaultValueEnum.fromString(info.get(DEFAULTVALUE, String.class));
        
        return true;
    }
    
    public Set<Group> getGivenGroups() {
        Set<Group> result = new TreeSet<>((Group o1, Group o2)
                -> o1.getNumber() - o2.getNumber()
        );
        for (Group g : getGroups()) {
            result.add(g);
        }
        return result;
    }
}
