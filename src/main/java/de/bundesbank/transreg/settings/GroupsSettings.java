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

    private GroupsEnum myGroup = GroupsEnum.Group1;
    private GroupsEnum[] groups;
    private boolean enable = false;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public GroupsSettings() {

    }

    public GroupsSettings(int freq) {
        enable = true;
        generateGroups(freq);
    }

    private void generateGroups(int freq) {
        groups = new GroupsEnum[freq];
        Arrays.fill(groups, GroupsEnum.Group1);
    }

    public GroupsEnum[] getGroups() {
        return groups;
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
        // Default ist definiert als alle Gruppen 
        boolean result = true;
        for (GroupsEnum g : groups) {
            result = result && (g.equals(GroupsEnum.Group1));
        }
        return result;
    }

    public GroupsSettings copy() {

        GroupsSettings copy = new GroupsSettings();
        copy.setGroups(groups.clone());

        return copy;
    }

    public String getInfo() {
        if (enable) {
            return myGroup.toString();
        }
        return "";
    }

    @Override
    public String toString() {
        String result = "";
        for (GroupsEnum g : groups) {
            result += g.name() + " ";
        }
        return result.trim();
    }

    private static String GROUPS = "groups", STATUS = "status";
    
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
        info.add(STATUS, myGroup.name());
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
        myGroup = GroupsEnum.valueOf(info.get(STATUS, String.class));
        return true;
    }
}
