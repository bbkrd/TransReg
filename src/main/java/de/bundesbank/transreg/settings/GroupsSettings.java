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

import de.bundesbank.transreg.util.GroupsDefaultValueEnum;
import de.bundesbank.transreg.util.GroupsEnum;
import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.information.InformationSetSerializable;
import java.util.Arrays;

/**
 *
 * @author s4504gn
 */
public class GroupsSettings implements InformationSetSerializable {

    // TODO: myGroup entfernen?
//    private GroupsEnum myGroup = GroupsEnum.Group1;
    private GroupsEnum[] groups = new GroupsEnum[]{GroupsEnum.Group1};
    private boolean enable = false;
    private GroupsDefaultValueEnum defaultValue = GroupsDefaultValueEnum.ZERO;

    public GroupsDefaultValueEnum getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(GroupsDefaultValueEnum g) {
        defaultValue = g;
    }

    public boolean isEnabled() {
        return enable;
    }

    public void setEnabled(boolean enable) {
        this.enable = enable;
    }

    public GroupsSettings() {

    }

    public GroupsSettings(int freq) {
//        enable = true;
        generateGroups(freq);
    }

    private void generateGroups(int freq) {
        groups = new GroupsEnum[freq];
        Arrays.fill(groups, GroupsEnum.Group1);
    }

    public GroupsEnum[] getGroups() {
        return groups.clone();
    }

    public void setGroups(GroupsEnum[] g) {
        groups = g;
    }

    public int getFreq() {
        if (enable) {
            return groups.length;
        }
        return 0;
    }

    public boolean isDefault() {
        if (defaultValue.equals(GroupsDefaultValueEnum.NaN)) {
            return false;
        }
        // Default ist definiert als alle Gruppen mit Status Group1
        boolean result = true;
        for (GroupsEnum g : groups) {
            result = result && (g.equals(GroupsEnum.Group1));
        }
        return result || !enable;
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
        for (GroupsEnum g : groups) {
            result.append(g.name()).append(" ");
        }
        return result.toString().trim();
    }

    private static String GROUPS = "groups", ENABLE = "enable", DEFAULTVALUE = "defaultvalue";

    @Override
    public InformationSet write(boolean verbose) {
        InformationSet info = new InformationSet();
        if (groups != null) {
            String[] groupsInfo = new String[groups.length];
            for (int i = 0; i < groupsInfo.length; ++i) {
                groupsInfo[i] = groups[i].name();
            }
            info.add(GROUPS, groupsInfo);
        }
        info.add(ENABLE, enable);
        info.add(DEFAULTVALUE, defaultValue);

        return info;
    }

    @Override
    public boolean read(InformationSet info) {
        String[] groupsInfo = info.get(GROUPS, String[].class);
        if (groupsInfo != null) {
            groups = new GroupsEnum[groupsInfo.length];
            for (int i = 0; i < groupsInfo.length; ++i) {
                groups[i] = GroupsEnum.valueOf(groupsInfo[i]);
            }
        }
        enable = info.get(ENABLE, Boolean.class);
        defaultValue = GroupsDefaultValueEnum.fromString(info.get(DEFAULTVALUE, String.class));

        return true;
    }

    public int getMaxGroupNumber() {
        int result = 0;
        for (GroupsEnum g : getGroups()) {
            if (g.ordinal() > result) {
                result = g.ordinal();
            }
        }
        return result;
    }
}
