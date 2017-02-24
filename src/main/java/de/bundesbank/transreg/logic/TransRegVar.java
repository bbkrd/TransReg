/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.logic;

import de.bundesbank.transreg.settings.TransRegSettings;
import de.bundesbank.transreg.ui.nodes.NodesLevelEnum;
import de.bundesbank.transreg.util.GroupsEnum;
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
            PROP_CENTERUSER= "centeruser";

    private String name;
    private GroupsEnum groupStatus = GroupsEnum.Group1;
    private NodesLevelEnum level = NodesLevelEnum.ORIGINAL;
    private TransRegSettings currentSettings;
    private TransRegSettings oldSettings;

    // wird gesetzt wenn calculate() aufgerufen wurde 
    private LocalDateTime timestamp;
    private TsData calculatedData;
    private final TsMoniker moniker;

//    private List<TransRegVar> children = new ArrayList<>();
//    private TransRegVar parent;
    private UUID id;
    private List<UUID> childrenIDs = new ArrayList<>();
    private UUID parentID;

    public static final HashMap<UUID, TransRegVar> variables = new HashMap<>();

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private Boolean appear = true;
    private Boolean x = false;

//<editor-fold defaultstate="collapsed" desc="Constructors"> 
    // for test classes
    public TransRegVar(TsData d) {
        super(d);
        moniker = null;
        calculatedData = d.clone();
        currentSettings = new TransRegSettings(d.getFrequency().intValue());
        oldSettings = currentSettings.copy();
        name = "var";
        id = UUID.randomUUID();
        variables.put(id, this);
    }

    public TransRegVar(String s, TsData d) {
        super(s, d);
        moniker = null;
        calculatedData = d.clone();
        currentSettings = new TransRegSettings(d.getFrequency().intValue());
        oldSettings = currentSettings.copy();
        name = s;
        id = UUID.randomUUID();
        variables.put(id, this);
    }

    //CalculationTool: doGroups
    public TransRegVar(String s, TsMoniker m, TsData d) {
        super(s.replaceAll("\\n", " "), d);
        moniker = m;
        calculatedData = d.clone();
        currentSettings = new TransRegSettings(d.getFrequency().intValue());
        oldSettings = currentSettings.copy();
        name = s;
        id = UUID.randomUUID();
        variables.put(id, this);
    }

    //Encoding
    private TransRegVar(String s, TsMoniker m, TsData data, TsData calculated, TransRegSettings current, UUID id) {
        super(s, data);
        moniker = m;
        calculatedData = calculated;
        currentSettings = current;
        oldSettings = currentSettings.copy();
        name = s;
        this.id = id;
    }
//</editor-fold>

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String s) {
        name = s;
    }

    public void rename(String s) {
        this.setName(s);
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

    public Boolean getX() {
        return x;
    }

    public void setX(Boolean val) {
        x = val;
    }

    public Boolean getAppear() {
        return appear;
    }

    public void setAppear(Boolean appear) {
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
        getChildren().remove(child);
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

    public void removeParent(){
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
        this.oldSettings = currentSettings.copy();
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
                return "centeruser = " + currentSettings.getCenteruser().getMethod().name();
//                return currentSettings.getCenteruser().getMethod().toString();
        }

        return null;
    }

    public void setLevel(NodesLevelEnum level) {
        this.level = level;
    }

    public TsFrequency getFrequency() {
        return getOriginalData().getFrequency();
    }

    public String getTimespan() {
        return getTsData().getStart().toString() + " to " + getTsData().getEnd().toString();
    }

    public String getTimestamp() {
        if (timestamp != null) {
            return timestamp.toString();
        }
        return "";
    }

    public void setTimestamp(LocalDateTime timestamp) {
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

    public void restore() {
//        currentSettings = oldSettings.copy();
//        calculatedData = getOriginalData().clone();
//        if (!currentSettings.isDefault()) {
//            currentSettings.setTimestamp(LocalDateTime.now());
//        } else {
//            currentSettings.setTimestamp(null);
//        }
//        calculate();
    }

    public TransRegSettings getOldSettings() {
        return oldSettings;
    }

    // TODO: ueberarbeiten
    @Override
    public boolean refresh() {
//
//        if (this.getMoniker() != null) {
//            Ts s = TsFactory.instance.createTs(null, this.getMoniker(), TsInformationType.Data);
//            if (s.hasData() == TsStatus.Undefined) {
//                s.load(TsInformationType.Data);
//            }
//
//            TsData data = s.getTsData();
//            if (data == null) {
//                return false;
//            }
//            setData(data);
//            calculate();
//            return true;
//        }
        return false;
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

//    public TransRegVar copy() {
//        // TODO: ID auch kopieren?
//        return new TransRegVar(this.getDescription(getDefinitionFrequency()) + "_copy", moniker, calculatedData.clone(), calculatedData.clone(), currentSettings.copy(), this.getID());
//    }
    public TsVariable convert() {
        return new TsVariable(getDescription(getDefinitionFrequency()), calculatedData);
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

//<editor-fold defaultstate="collapsed" desc="TransRegVarConverter">
    private static class TransRegVarConverter implements InformationConverter<TransRegVar> {

        @Override
        public TransRegVar decode(InformationSet info) {

            TsData data = info.get(DATA, TsData.class);
            String desc = info.get(DESC, String.class);
            TsMoniker monk = info.get(MONIKER, TsMoniker.class);
            TsData original = info.get(ORIGINAL, TsData.class);

            String level = info.get(LEVEL, String.class);

            //read methode, auf null prÃ¼fen
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

            TransRegVar result = new TransRegVar(desc, monk, original, data, cur, myID);
            result.setLevel(NodesLevelEnum.fromString(level));

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
            info.set(DESC, t.getDescription(t.getDefinitionFrequency()));
            info.set(MONIKER, t.getMoniker());
            info.set(DATA, t.getTsData());
            info.set(ORIGINAL, t.getOriginalData());
            info.set(LEVEL, t.getLevel().toString());

            if (!t.isRoot()) {
                info.set(PARENT, t.getParent().getID().toString());
            }
            if (t.hasChildren()) {
                String s = "";
                for (TransRegVar child : t.getChildren()) {
                    s = s + child.getID() + " ";
                }
                info.set(CHILDREN, s);
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
                DESC = "description",
                ID = "id",
                PARENT = "parent",
                CHILDREN = "children",
                LEVEL = "level";
    }
//</editor-fold>

    private static final InformationConverter<TransRegVar> tsvar = new TransRegVarConverter();

    public static void register() {
        LINKER.register(TransRegVarConverter.TYPE, TransRegVar.class, tsvar);
    }
}
