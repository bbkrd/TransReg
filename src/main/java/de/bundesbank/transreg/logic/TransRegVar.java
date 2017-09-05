/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.logic;

import de.bundesbank.transreg.settings.TransRegSettings;
import de.bundesbank.transreg.ui.nodes.NodesLevelEnum;
import de.bundesbank.transreg.util.GroupsEnum;
import ec.tss.Ts;
import ec.tss.TsFactory;
import ec.tss.TsInformationType;
import ec.tss.TsMoniker;
import ec.tstoolkit.information.InformationConverter;
import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.timeseries.regression.TsVariable;
import static ec.tstoolkit.timeseries.regression.TsVariables.LINKER;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.utilities.IDynamicObject;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;

/**
 *
 * @author s4504gn
 */
public class TransRegVar extends TsVariable implements IDynamicObject, Serializable {

    public static final String PROP_TITLE = "name";
    public static final String PROP_X = "xprop";
    public static final String PROP_APPEARANCE = "appearance";

    public static final String PROP_LEVEL = "level",
            PROP_FREQUENCY = "frequency",
            PROP_TIMESPAN = "timespan",
            PROP_DATA = "data",
            PROP_TIMESTAMP = "timestamp",
            PROP_CENTERUSER = "centeruser",
            PROP_CALC_SPAN = "calculation span",
            PROP_CALC_MEAN = "calculated mean";

    private GroupsEnum groupStatus = GroupsEnum.Group1;
    private NodesLevelEnum level = NodesLevelEnum.ORIGINAL;
    private TransRegSettings currentSettings;
    
    private String preTestResult; 

    // wird gesetzt wenn calculate() aufgerufen wurde 
    private String timestamp;
    private TsData calculatedData;
    private final TsMoniker moniker;

    private List<Double> mean;

    private UUID id;
    private List<UUID> childrenIDs = new ArrayList<>();
    private UUID parentID;

    public static final HashMap<UUID, TransRegVar> variables = new HashMap<>(); // warum static und final

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private boolean appear = true;
    
//<editor-fold defaultstate="collapsed" desc="Constructors"> 
    // for test classes
    public TransRegVar(@Nonnull TsData d) {
        super(d);
        moniker = null;
        calculatedData = d.clone();
        currentSettings = new TransRegSettings(d.getFrequency().intValue());
        id = UUID.randomUUID();
        super.setName("var");
        variables.put(id, this);
        preTestResult = TransRegCalculationTool.testCenteruser(calculatedData);
    }

    public TransRegVar(String s, @Nonnull TsData d) {
        super(s, d);
        moniker = null;
        calculatedData = d.clone();
        currentSettings = new TransRegSettings(d.getFrequency().intValue());
        super.setName(s);
        id = UUID.randomUUID();
        variables.put(id, this);
        preTestResult = TransRegCalculationTool.testCenteruser(calculatedData);
    }

    //CalculationTool: doGroups
    public TransRegVar(String s, TsMoniker m, @Nonnull TsData d) {
        super(s, d);
        moniker = m;
        calculatedData = d.clone();
        currentSettings = new TransRegSettings(d.getFrequency().intValue());
        super.setName(s);
        id = UUID.randomUUID();
        variables.put(id, this);
        preTestResult = TransRegCalculationTool.testCenteruser(calculatedData);
    }

    //Encoding
    private TransRegVar(String s, TsMoniker m, TsData data, TsData calculated, TransRegSettings current, UUID id) {
        super(s, data);
        moniker = m;
        calculatedData = calculated;
        currentSettings = current;
        super.setName(s);
        this.id = id;
        preTestResult = TransRegCalculationTool.testCenteruser(calculatedData);
    }
//</editor-fold>

    public String getPreTestResult(){
        return preTestResult;
    }
    
    public void rename(String s) {
        this.setName(s);
        this.setDescription(s);
    }

    public GroupsEnum getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(GroupsEnum groupStatus) {
        this.groupStatus = groupStatus;
    }

    public UUID getID() {
        return id;
    }

    public void setID(UUID id) {
        this.id = id;
    }

    public boolean getAppear() {
        return appear;
    }

    public void setAppear(boolean appear) {
        this.appear = appear;
    }

    public ArrayList<TransRegVar> getChildren() {
        ArrayList<TransRegVar> result = new ArrayList<>();
        for (UUID id : childrenIDs) {
            result.add(variables.get(id));
        }
        return result;
    }

    public void addChildren(List<TransRegVar> children) {
        for (TransRegVar t : children) {
            this.childrenIDs.add(t.getID());
            t.setParent(this);
        }
    }

    public void addChildrenID(List<UUID> ids) {
        this.childrenIDs.addAll(ids);
    }

    public void addChild(TransRegVar child) {
        childrenIDs.add(child.getID());
        child.setParent(this);
        child.setSettings(this.getSettings());
    }

    public void addChildID(UUID id) {
        childrenIDs.add(id);
        variables.get(id).setSettings(this.getSettings());
    }

    public boolean hasChildren() {
        return !getChildren().isEmpty();
    }

    public void removeChild(TransRegVar child) {
        childrenIDs.remove(child.getID());
    }

    public TransRegVar getParent() {
        return variables.get(parentID);
    }

    public void setParent(TransRegVar parent) {
        this.parentID = parent.getID();
        this.setSettings(parent.getSettings());
    }

    private void setParentID(UUID parentID) {
        this.parentID = parentID;
        this.setSettings(variables.get(parentID).getSettings());
    }

    public void removeParent() {
        parentID = null;
    }

    public TsMoniker getMoniker() {
        return moniker;
    }

    @Override
    public TsData getTsData() {
        return calculatedData;
    }

    public TsData getOriginalData() {
        return super.getTsData();
    }

    public TransRegSettings getSettings() {
        return currentSettings;
    }

    public void setSettings(TransRegSettings settings) {
        this.currentSettings = settings;
    }

    public NodesLevelEnum getLevel() {
        return level;
    }

    public String getLevelName() {

        switch (level) {
            case ORIGINAL:
                return "Original";
            case ACTIVE:
                // TODO: HorizontalSetting noch nicht fertig, getter+setter anpassen und Fallunterscheidung
                return "Active";
            case GROUP:
                return getGroupStatus().name();
            case CENTERUSER:
                String s = "Centred ";
                switch (currentSettings.getCenteruser().getMethod()) {
                    case Global:
                        s += "(global mean)";
                        break;
                    case Seasonal:
                        s += "(seasonal means)";
                        break;
                }
                return s;
        }

        return null;
    }

    public void setLevel(NodesLevelEnum level) {
        this.level = level;
    }

    public TsFrequency getFrequency() {
        return getOriginalData().getFrequency();
    }

    public String getCalculationSpan() {
        return currentSettings.getCenteruser().getSpan().toString();
    }

    public String getTimespan() {
        return getTsData().getStart().toString() + " to " + getTsData().getLastPeriod().toString();
    }

    public String getTimestamp() {
        if (timestamp != null) {
            return timestamp;
        }
        return "";
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp.toString();
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRoot() {
        /*
         *   Variable is root? 
         *   > important for generate first tree after load WS
         *   > either original variable or after deleting the original variable a "new root" without parent
         */
        return level.equals(NodesLevelEnum.ORIGINAL) || (getParent() == null);
    }

    public TransRegVar getRoot() {
        if (this.isRoot()) {
            return this;
        }
        return this.getParent().getRoot();
    }

    @Override
    public boolean refresh() {

        // load refreshed data
        Ts ts = TsFactory.instance.createTs(null, moniker, TsInformationType.Data);
        if (ts.getTsData() == null) {
            return false;
        }
        this.setDescription(ts.getName());
        this.setData(ts.getTsData());
        this.setTimestamp(LocalDateTime.now());

        if (!this.getLevel().equals(NodesLevelEnum.ORIGINAL)) {
            // calculate with settings

            TransRegVar root = new TransRegVar(ts.getTsData());
            root.setSettings(currentSettings);
            ArrayList<TransRegVar> vars = TransRegCalculationTool.calculate(root);

            // find the variable
            for (TransRegVar t : vars) {
                if (t.getGroupStatus().equals(this.getGroupStatus())) {
                    if (t.getLevel().equals(this.getLevel())) {
                        this.setCalculatedData(t.getTsData().clone());
                        this.setMean(t.getMean());
                        return true;
                    }
                }
            }
        }
        this.setCalculatedData(ts.getTsData());
        preTestResult = TransRegCalculationTool.testCenteruser(calculatedData);
        return true;
    }

    public boolean updateSettings(TransRegSettings settings) {
        if (this.hasChildren()) {
            for (TransRegVar child : this.getChildren()) {
                child.setSettings(this.getSettings());
                child.updateSettings(settings);
            }
        }
        return true;
    }

    public TsVariable convert() {
        return new TsVariable(getName(), calculatedData);
    }

//<editor-fold defaultstate="collapsed" desc="PropertyChangeListener">
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
//</editor-fold>

    void setCalculatedData(TsData tsData) {
        calculatedData = tsData;
    }

    public ArrayList<TransRegVar> deleteChildren() {
        ArrayList<TransRegVar> result = new ArrayList<>();
        if (this.hasChildren()) {
            result.addAll(this.getChildren());
            for (TransRegVar child : getChildren()) {
                result.addAll(child.deleteChildren());
                variables.remove(child.getID());
            }
            this.childrenIDs.clear();
        }
        return result;
    }

    public void setMean(double mean) {
        this.mean = new ArrayList<>();
        this.mean.add(mean);
    }

    public void setMean(double[] mean) {
        this.mean = new ArrayList<>();
        for (double d : mean) {
            this.mean.add(d);
        }
    }

    public void setMean(List<Double> mean) {
        this.mean = mean;
    }

    public List<Double> getMean() {
        return mean;
    }

    public String getMeanString() {
        StringBuilder text = new StringBuilder();
        if (mean != null) {
            mean.stream()
                    .map((d) -> Math.round(d * 10) / 10.0)
                    .forEach((d) -> {
                        text.append(d).append(" ");
                    });
        }
        return text.toString();
    }

    // for CalculationTool:doCenteruser
    public TransRegVar copy() {
        TransRegVar t = new TransRegVar(getName(), getMoniker(), getOriginalData());
        t.setCalculatedData(this.getTsData().clone());
        t.setSettings(currentSettings.copy());
        t.setGroupStatus(this.getGroupStatus());
        return t;
    }

//<editor-fold defaultstate="collapsed" desc="TransRegVarConverter">
    private static class TransRegVarConverter implements InformationConverter<TransRegVar> {

        @Override
        public TransRegVar decode(InformationSet info) {

            TsData data = info.get(DATA, TsData.class);
            String name = info.get(NAME, String.class);
            TsMoniker monk = info.get(MONIKER, TsMoniker.class);
            TsData original = info.get(ORIGINAL, TsData.class);
            String level = info.get(LEVEL, String.class);
            String stamp = info.get(TIMESTAMP, String.class);
            GroupsEnum group = GroupsEnum.valueOf(info.get(GROUPSTATUS, String.class));

            //read methode, auf null pruefen
            int frequency = data.getFrequency().intValue();
            TransRegSettings cur = new TransRegSettings(frequency);
            InformationSet curInfo = info.getSubSet(SETTINGS);
            if (curInfo != null) {
                cur.read(curInfo);
            }

            UUID myID;
            String ids = info.get(ID, String.class);
            if (ids != null) {
                myID = UUID.fromString(ids);
            } else {
                // sollte nicht vorkommen
                myID = UUID.randomUUID();
            }

            TransRegVar result = new TransRegVar(name, monk, original, data, cur, myID);
            result.setLevel(NodesLevelEnum.fromString(level));
            result.setGroupStatus(group);

            if (stamp != null) {
                result.setTimestamp(stamp);
            }

            String mean = info.get(MEAN, String.class);
            if (mean != null) {
                String[] s = mean.trim().split("\\s+");
                double[] means = new double[s.length];
                for (int i = 0; i < s.length; i++) {
                    means[i] = Double.parseDouble(s[i]);
                }
                result.setMean(means);
            }
            String parentID = info.get(PARENT, String.class);
            if (parentID != null) {
                result.setParentID(UUID.fromString(parentID));
            }

            String xmlString = info.get(CHILDREN, String.class);
            if (xmlString != null) {
                xmlString = xmlString.trim();
                ArrayList<UUID> idList = new ArrayList<>();
                for (String s : xmlString.split(" ")) {
                    idList.add(UUID.fromString(s));
                }
                result.addChildrenID(idList);
            }

            variables.put(myID, result); // in Konstruktor?

            return result;
        }

        @Override
        public InformationSet encode(TransRegVar t, boolean verbose) {

            InformationSet info = new InformationSet();
            info.set(ID, t.getID().toString());
            info.set(NAME, t.getName());
            info.set(MONIKER, t.getMoniker());
            info.set(DATA, t.getTsData());
            info.set(ORIGINAL, t.getOriginalData());
            info.set(LEVEL, t.getLevel().toString());
            info.set(GROUPSTATUS, t.getGroupStatus().toString());

            info.set(TIMESTAMP, t.getTimestamp());

            if (!t.isRoot()) {
                info.set(PARENT, t.getParent().getID().toString());
            }
            
            if (t.hasChildren()) {
                StringBuilder s = new StringBuilder();
                for (TransRegVar child : t.getChildren()) {
                    s.append(child.getID()).append(" ");
                }
                info.set(CHILDREN, s.toString());
            }
            if (!t.getMeanString().trim().isEmpty()) {
                info.set(MEAN, t.getMeanString());
            }

            InformationSet settings = t.getSettings().write(verbose);
            info.set(SETTINGS, settings);
            return info;
        }

        @Override
        public Class<TransRegVar> getInformationType() {
            return TransRegVar.class;
        }

        @Override
        public String getTypeDescription() {
            return TYPE;
        }
        static final String TYPE = "transformed time series",
                OLDSETTINGS = "old settings",
                SETTINGS = "settings",
                MONIKER = "moniker",
                DATA = "data",
                ORIGINAL = "original data",
                NAME = "name",
                ID = "id",
                PARENT = "parent",
                CHILDREN = "children",
                LEVEL = "level",
                TIMESTAMP = "timestamp",
                GROUPSTATUS = "groupsstatus",
                MEAN = "mean";
    }
//</editor-fold>

    private static final InformationConverter<TransRegVar> tsvar = new TransRegVarConverter();

    public static void register() {
        LINKER.register(TransRegVarConverter.TYPE, TransRegVar.class, tsvar);
    }
}
