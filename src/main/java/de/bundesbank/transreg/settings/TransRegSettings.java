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
import ec.tstoolkit.timeseries.Day;

/**
 *
 * @author Nina Gonschorreck
 */
public class TransRegSettings implements InformationSetSerializable {

    private CenteruserSettings centeruser;
    private GroupsSettings groups;
    private LeadLagSettings leadLag;
    private EpochSettings epoch;

    private String specificationName = "Default";

    public static final TransRegSettings DEFAULT;

    static {
        DEFAULT = new TransRegSettings();
    }

    public static final TransRegSettings[] allSettings() {
        return new TransRegSettings[]{DEFAULT};
    }

    public TransRegSettings() {
        centeruser = new CenteruserSettings();
        groups = new GroupsSettings();
        leadLag = new LeadLagSettings();
        epoch = new EpochSettings();
    }

    public TransRegSettings(int freq) {
        centeruser = new CenteruserSettings();
        groups = new GroupsSettings(freq);
        leadLag = new LeadLagSettings();
        epoch = new EpochSettings();
    }

    public TransRegSettings(Day start, Day end) {
        centeruser = new CenteruserSettings();
        groups = new GroupsSettings();
        leadLag = new LeadLagSettings();
        epoch = new EpochSettings(start, end);
    }

    public TransRegSettings(int freq, Day start, Day end) {
        centeruser = new CenteruserSettings();
        groups = new GroupsSettings(freq);
        leadLag = new LeadLagSettings();
        epoch = new EpochSettings(start, end);
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

        if (!leadLag.isDefault()) {
            return false;
        }

        return epoch.isDefault();
    }

    public TransRegSettings copy() {
        TransRegSettings copy = new TransRegSettings();
        copy.setSpecificationName(this.specificationName);
        copy.setCenteruser(centeruser.copy());
        copy.setGroups(groups.copy());
        copy.setLeadLag(leadLag.copy());
        copy.setEpoch(epoch.copy());

        return copy;
    }

    public EpochSettings getEpoch() {
        return epoch;
    }

    public void setEpoch(EpochSettings epoch) {
        this.epoch = epoch;
    }

    public void setSettings(TransRegSettings settings) {
        centeruser = settings.getCenteruser();
        epoch = settings.getEpoch();
        groups = settings.getGroups();
        leadLag = settings.getLeadLag();
    }

    @Override
    public String toString() {
        return specificationName;
    }

    public void setSpecificationName(String s) {
        specificationName = s;
    }

    //<editor-fold defaultstate="collapsed" desc="for Workspace">
    @Override
    public InformationSet write(boolean verbose) {
        InformationSet info = new InformationSet();
        info.add(SPECNAME, specificationName);

        InformationSet tmp;
        tmp = leadLag.write(verbose);
        info.add(LEADLAG, tmp);
        tmp = epoch.write(verbose);
        info.add(EPOCH, tmp);
        tmp = groups.write(verbose);
        info.add(GROUPS, tmp);
        tmp = centeruser.write(verbose);
        info.add(CENTERUSER, tmp);

        return info;
    }

    @Override
    public boolean read(InformationSet info) {

        specificationName = info.get(SPECNAME, String.class);
        InformationSet leadLagInfo = info.getSubSet(LEADLAG);
        if (leadLagInfo != null) {
            leadLag.read(leadLagInfo);
        }
        InformationSet epochInfo = info.getSubSet(EPOCH);
        if (epochInfo != null) {
            epoch.read(epochInfo);
        }
        InformationSet centeruserInfo = info.getSubSet(CENTERUSER);
        if (centeruserInfo != null) {
            centeruser.read(centeruserInfo);
        }
        InformationSet groupsInfo = info.getSubSet(GROUPS);
        if (groupsInfo != null) {
            groups.read(groupsInfo);
        }
        return true;
    }

    private static String CENTERUSER = "centeruser", GROUPS = "groups", LEADLAG = "leadlag", EPOCH = "epoch", SPECNAME = "specname";
//</editor-fold>
}
