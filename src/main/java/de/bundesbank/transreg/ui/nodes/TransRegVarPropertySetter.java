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
package de.bundesbank.transreg.ui.nodes;

import de.bundesbank.transreg.logic.TransRegVar;
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
    private AppearanceProperty appearanceProperty;
    private LevelProperty levelProperty;
    private FrequencyProperty freqProperty;
    private TsPeriodProperty timespanProperty;
    private TimestampProperty timestampProperty;
    private CenteruserTestProperty centeruserProperty;
    private CalculationSpanProperty calculationspanProperty;
    private CalculatedMeanProperty calculatedMeanProperty;

    public TransRegVarPropertySetter(TransRegVar model) {

        this.var = model;
        this.levelProperty = new LevelProperty();
        this.freqProperty = new FrequencyProperty();
        this.timespanProperty = new TsPeriodProperty();
        this.timestampProperty = new TimestampProperty();
        this.centeruserProperty = new CenteruserTestProperty();
        this.calculationspanProperty = new CalculationSpanProperty();
        this.calculatedMeanProperty = new CalculatedMeanProperty();

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

//<editor-fold defaultstate="collapsed" desc="AppearanceProperty">
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
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="FrequencyProperty">
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
            String freq = var.getFrequency().toString();

            if (var.getFrequency().HalfYearly.equals(var.getFrequency())) {
                freq = "Biannual";
            }
            return freq;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="LevelProperty">
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
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="TimespanProperty">
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
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="TimestampProperty">
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
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="CenteruserTestProperty">
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
            if (!var.getLevel().equals(NodesLevelEnum.CENTERUSER)) {
                return var.getPreTestResult();
            }
            return "";
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Calculation span">
    public CalculationSpanProperty getCalculationspanProperty() {
        return calculationspanProperty;
    }

    public class CalculationSpanProperty extends PropertySupport.ReadOnly<String> {

        public CalculationSpanProperty() {
            super(TransRegVar.PROP_CALC_SPAN, String.class, "TsPeriod", null);
        }

        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return toString();
        }

        @Override
        public String toString() {
            if (var.getLevel().equals(NodesLevelEnum.CENTERUSER)) {
                return var.getCalculationSpan();
            } else {
                return " ";
            }
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Calculated mean">
    public CalculatedMeanProperty getCalculatedMeanProperty() {
        return calculatedMeanProperty;
    }

    public class CalculatedMeanProperty extends PropertySupport.ReadOnly<String> {

        public CalculatedMeanProperty() {
            super(TransRegVar.PROP_CALC_MEAN, String.class, "Calculated Mean", null);
        }

        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return toString();
        }

        @Override
        public String toString() {
            return var.getMeanString();
        }
    }
//</editor-fold>
}
