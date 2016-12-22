/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.settings;

import de.bundesbank.transreg.util.GroupsEnum;
import java.util.Arrays;

/**
 *
 * @author s4504gn
 */
public class GroupsSettings {

    private GroupsEnum[] groups;

    public GroupsSettings() {
        generateGroups(4);
    }

    public GroupsSettings(int freq) {
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
        return groups.length;
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
}
