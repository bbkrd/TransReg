/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.settings;

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

        return copy;
    }

//    public String getInfo() {
//        if (enable) {
//            return myGroup.toString();
//        }
//        return "";
//    }

//    public String getMyGroup() {
//        return myGroup.toString();
//    }
//
//    public void setMyGroup(GroupsEnum myGroup) {
//        this.myGroup = myGroup;
//    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (GroupsEnum g : groups) {
            result.append(g.name()).append(" ");
        }
        return result.toString().trim();
    }

    private static String GROUPS = "groups", ENABLE = "enable";

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
//        info.add(STATUS, myGroup.name());
        info.add(ENABLE, enable);
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
//        myGroup = GroupsEnum.valueOf(info.get(STATUS, String.class));
        enable = info.get(ENABLE, Boolean.class);
        return true;
    }

//    public void setGroupsStatus(int i) {
//        String tmp = "GroupsEnum.Group" + i;
//        myGroup = GroupsEnum.valueOf(tmp);
//    }

//    public int getGroupNumber() {
//        return myGroup.ordinal();
//    }

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
