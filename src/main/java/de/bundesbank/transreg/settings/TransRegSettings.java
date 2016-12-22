/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.settings;

/**
 *
 * @author s4504gn
 */
public class TransRegSettings {

    private CenteruserSettings centeruser;
    private GroupsSettings groups;

    public TransRegSettings() {
        centeruser = new CenteruserSettings();
        groups = new GroupsSettings();
    }

    public TransRegSettings(int freq) {
        centeruser = new CenteruserSettings();
        groups = new GroupsSettings(freq);
    }

    public CenteruserSettings getCenteruser() {
        return centeruser;
    }

    public void setCenteruser(CenteruserSettings centeruser) {
        this.centeruser = centeruser;
    }

    public GroupsSettings getGroups() {
        return groups;
    }

    public void setGroups(GroupsSettings groups) {
        this.groups = groups;
    }

    public boolean isDefault() {
        if (centeruser.isDefault() && groups.isDefault()) {
            return true;
        }
        return false;
    }
    
    public TransRegSettings copy(){
        
        TransRegSettings copy = new TransRegSettings();
        copy.setCenteruser(centeruser.copy());
        copy.setGroups(groups.copy());
        
        return copy;
    }
}
