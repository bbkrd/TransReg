/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.settings;

import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.information.InformationSetSerializable;

/**
 *
 * @author s4504gn
 */
public class TransRegSettings implements InformationSetSerializable {

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
        if (!centeruser.isDefault()) {
            return false;
        }
        if (!groups.isDefault()) {
            return false;
        }
        if (!horizontal.isDefault()) {
            return false;
        }
        return true;
    }

    public TransRegSettings copy() {
        TransRegSettings copy = new TransRegSettings();
        copy.setCenteruser(centeruser.copy());
        copy.setGroups(groups.copy());
        copy.setHorizontal(horizontal.copy());

        return copy;
    }

//    public String getInfo() {
//        return centeruser.getInfo() + " " + groups.getInfo() + " " + horizontal.getInfo();
//    }
    
    //<editor-fold defaultstate="collapsed" desc="for Workspace">  
    @Override
    public InformationSet write(boolean verbose) {
        InformationSet info = new InformationSet();
//        if (timestamp != null) {
//            InformationSet t = new InformationSet();
//            t.add(TIMESTAMP, timestamp.toString());
//            info.add(TIMESTAMP, t);
//        }

        InformationSet tmp = centeruser.write(verbose);
        info.add(CENTERUSER, tmp);

        tmp = groups.write(verbose);
        info.add(GROUPS, tmp);

        //TODO: horizontal.write();
        return info;
    }

    @Override
    public boolean read(InformationSet info) {

        centeruser.read(info.getSubSet(CENTERUSER));
        groups.read(info.getSubSet(GROUPS));

//        InformationSet tmp = info.getSubSet(TIMESTAMP);
//        if (tmp != null) {
//            String s = tmp.get(TIMESTAMP, String.class);
//            if (s != null) {
//                timestamp = LocalDateTime.parse(s);
//            }
//        }

        return true;
    }

    private static String TIMESTAMP = "timestamp",
            CENTERUSER = "centeruser",
            GROUPS = "groups",
            HORIZONTAL = "horizontal";
//</editor-fold>
}
