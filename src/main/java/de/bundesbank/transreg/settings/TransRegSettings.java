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

import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.information.InformationSetSerializable;

/**
 *
 * @author s4504gn
 */
public class TransRegSettings implements InformationSetSerializable {

    private CenteruserSettings centeruser;
    private GroupsSettings groups;
//    private HorizontalSettings horizontal;
    private ExtendingSettings extending;
    private LeadLagSettings leadLag;

    public TransRegSettings() {
        centeruser = new CenteruserSettings();
        groups = new GroupsSettings();
//        horizontal = new HorizontalSettings();
        extending = new ExtendingSettings();
        leadLag = new LeadLagSettings();
    }

    public TransRegSettings(int freq) {
        centeruser = new CenteruserSettings();
        groups = new GroupsSettings(freq);
//        horizontal = new HorizontalSettings();
        extending = new ExtendingSettings();
        leadLag = new LeadLagSettings();
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

    public LeadLagSettings getLeadLag() {
        return leadLag;
    }

    public void setLeadLag(LeadLagSettings leadLag) {
        this.leadLag = leadLag;
    }

    public boolean isDefault() {
        if (!centeruser.isDefault()) {
            return false;
        }
        if (!groups.isDefault()) {
            return false;
        }

        if (!extending.isDefault()) {
            return false;
        }

        if (!leadLag.isDefault()) {
            return false;
        }

        return true;
    }

    public TransRegSettings copy() {
        TransRegSettings copy = new TransRegSettings();
        copy.setCenteruser(centeruser.copy());
        copy.setGroups(groups.copy());
        copy.setExtending(extending.copy());
        copy.setLeadLag(leadLag.copy());

        return copy;
    }

    public ExtendingSettings getExtending() {
        return extending;
    }

    public void setExtending(ExtendingSettings extending) {
        this.extending = extending;
    }

    //<editor-fold defaultstate="collapsed" desc="for Workspace">
    @Override
    public InformationSet write(boolean verbose) {
        InformationSet info = new InformationSet();
        InformationSet tmp = centeruser.write(verbose);
        info.add(CENTERUSER, tmp);

        tmp = groups.write(verbose);
        info.add(GROUPS, tmp);

        tmp = extending.write(verbose);
        info.add(EXTENDING, tmp);

        tmp = leadLag.write(verbose);
        info.add(LEADLAG, tmp);

        return info;
    }

    @Override
    public boolean read(InformationSet info) {

        centeruser.read(info.getSubSet(CENTERUSER));
        groups.read(info.getSubSet(GROUPS));
        extending.read(info.getSubSet(EXTENDING));
        leadLag.read(info.getSubSet(LEADLAG));

        return true;
    }

    private static String CENTERUSER = "centeruser", GROUPS = "groups", EXTENDING = "extending", LEADLAG = "leadlag";
//</editor-fold>
}
