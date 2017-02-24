/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui.nodes;

import de.bundesbank.transreg.logic.TransRegCalculationTool;
import de.bundesbank.transreg.logic.TransRegVar;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.openide.util.Exceptions;

/**
 *
 * @author s4504gn
 */
public class TransRegVarPropertySetter {

    private TransRegVar var;
    private ActiveProperty activeProperty;
    private AppearanceProperty appearanceProperty;
    private LevelProperty levelProperty;
    private FrequencyProperty freqProperty;
    private TsPeriodProperty timespanProperty;
    private DataProperty dataProperty;
    private TimestampProperty timestampProperty;
    private CenteruserTestProperty centeruserProperty;

    public TransRegVarPropertySetter(TransRegVar model) {

        this.var = model;
        this.activeProperty = new ActiveProperty();
        this.levelProperty = new LevelProperty();
        this.freqProperty = new FrequencyProperty();
        this.timespanProperty = new TsPeriodProperty();
//        this.dataProperty = new DataProperty();
        this.timestampProperty = new TimestampProperty();
        this.centeruserProperty = new CenteruserTestProperty();

//        this.var.addPropertyChangeListener(new PropertyChangeListener() {
//
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                // Methode wenn häkchen gesetzt wird
//            }
//        });
        if (model.hasChildren()) {
            this.appearanceProperty = new AppearanceProperty();
            this.var.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (TransRegVar.PROP_APPEARANCE.equals(evt.getPropertyName())) {
                        try {
                            appearanceProperty.setValue((Boolean) evt.getNewValue());
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            });
        }
    }

    /*Active*/
    public ActiveProperty getActiveProperty() {
        return activeProperty;
    }

    public class ActiveProperty extends PropertySupport.ReadOnly<Boolean> {

        public ActiveProperty() {
            super(TransRegVar.PROP_X, Boolean.class, "Active", null);
        }

        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return var.getX();
        }

        @Override
        public void setValue(Boolean val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        }
    }

    /*Apperance*/
    public AppearanceProperty getAppearanceProperty() {
        return appearanceProperty;
    }

    public class AppearanceProperty extends PropertySupport.ReadOnly<Boolean> {

        public AppearanceProperty() {
            super(TransRegVar.PROP_APPEARANCE, Boolean.class, "Appear", null);
        }

        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return var.getAppear();
        }

        @Override
        public void setValue(Boolean val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        }
    }

    /*Data*/
    public DataProperty getDataProperty() {
        return dataProperty;
    }

    public class DataProperty extends PropertySupport.ReadOnly<TsData> {

        public DataProperty() {
            super(TransRegVar.PROP_DATA, TsData.class, "TsData", null);
        }

        @Override
        public TsData getValue() throws IllegalAccessException, InvocationTargetException {
            return var.getTsData();
        }

        @Override
        public String toString() {
            return "Hallo";
        }
    }

    /*Frequency*/
    public FrequencyProperty getFreqProperty() {
        return freqProperty;
    }

    public class FrequencyProperty extends PropertySupport.ReadOnly<String> {

        public FrequencyProperty() {
            super(TransRegVar.PROP_FREQUENCY, String.class, "Frequency", null);
        }

        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return toString();
        }

        @Override
        public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        }

        @Override
        public String toString() {
            return var.getFrequency().toString();
        }
    }

    /*Level*/
    public LevelProperty getLevelProperty() {
        return levelProperty;
    }

    public class LevelProperty extends PropertySupport.ReadOnly<String> {

        public LevelProperty() {
            super(TransRegVar.PROP_LEVEL, String.class, "Level", null);
        }

        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return toString();
        }

        @Override
        public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        }

        @Override
        public String toString() {
            return var.getLevelName(); //To change body of generated methods, choose Tools | Templates.
        }
    }

    /*Timespan*/
    public TsPeriodProperty getTimespanProperty() {
        return timespanProperty;
    }

    public class TsPeriodProperty extends PropertySupport.ReadOnly<String> {

        public TsPeriodProperty() {
            super(TransRegVar.PROP_TIMESPAN, String.class, "TsPeriod", null);
        }

        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return toString();
        }

        @Override
        public String toString() {
            return var.getTimespan();
        }
    }

    /*Timestamp*/
    public TimestampProperty getTimestampProperty() {
        return timestampProperty;
    }

    public class TimestampProperty extends PropertySupport.ReadOnly<String> {

        public TimestampProperty() {
            super(TransRegVar.PROP_TIMESTAMP, String.class, "Timestamp", null);
        }

        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return toString();
        }

        @Override
        public String toString() {
            return var.getTimestamp();
        }
    }

    /*Centeruser Test*/
    public CenteruserTestProperty getCenteruserTestProperty() {
        return centeruserProperty;
    }

    public class CenteruserTestProperty extends PropertySupport.ReadOnly<String> {

        public CenteruserTestProperty() {
            super(TransRegVar.PROP_CENTERUSER, String.class, "Centeruser Test", null);
        }

        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return toString();
        }

        @Override
        public String toString() {
            return TransRegCalculationTool.testCenteruser(var.getTsData());
        }
    }

}
