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
    private HorizontalSettings horizontal;

    public TransRegSettings() {
        centeruser = new CenteruserSettings();
        groups = new GroupsSettings();
        horizontal = new HorizontalSettings();
    }

    public TransRegSettings(int freq) {
        centeruser = new CenteruserSettings();
        groups = new GroupsSettings(freq);
        horizontal = new HorizontalSettings();
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

    public HorizontalSettings getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(HorizontalSettings horizontal) {
        this.horizontal = horizontal;
    }

    public boolean isDefault() {
        if (centeruser.isDefault() && groups.isDefault() && horizontal.isDefault()) {
            return true;
        }
        return false;
    }

    public TransRegSettings copy() {

        TransRegSettings copy = new TransRegSettings();
        copy.setCenteruser(centeruser.copy());
        copy.setGroups(groups.copy());
        copy.setHorizontal(horizontal.copy());

        return copy;
    }

    public String getInfo() {
        return centeruser.getInfo() + " " + groups.getInfo() + " " + horizontal.getInfo();
    }
}
