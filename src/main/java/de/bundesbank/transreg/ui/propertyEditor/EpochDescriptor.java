/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui.propertyEditor;

import de.bundesbank.transreg.util.Epoch;
import ec.tstoolkit.descriptors.EnhancedPropertyDescriptor;
import ec.tstoolkit.descriptors.IObjectDescriptor;
import ec.tstoolkit.timeseries.Day;
import ec.tstoolkit.timeseries.regression.Ramp;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nina Gonschorreck
 */
public class EpochDescriptor implements IObjectDescriptor<Epoch> {

    private final Epoch epoch;

    @Override
    public String toString() {
        return epoch.toString();
    }

    public EpochDescriptor() {
        epoch = new Epoch();
    }

    public EpochDescriptor(Epoch e) {
        epoch = e;
    }

    @Override
    public Epoch getCore() {
        return epoch;
    }

    public Day getStart() {
        return epoch.getStart();
    }

    public void setStart(Day day) {
        epoch.setStart(day);
    }

    public Day getEnd() {
        return epoch.getEnd();
    }

    public void setEnd(Day day) {
        epoch.setEnd(day);
    }

    @Override
    public List<EnhancedPropertyDescriptor> getProperties() {
        ArrayList<EnhancedPropertyDescriptor> descs = new ArrayList<>();
        EnhancedPropertyDescriptor desc = startDesc();
        if (desc != null) {
            descs.add(desc);
        }
        desc = endDesc();
        if (desc != null) {
            descs.add(desc);
        }
        return descs;
    }
    private static final int START_ID = 1,
            END_ID = 2;

    private EnhancedPropertyDescriptor startDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("start", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, START_ID);
            desc.setDisplayName("Start");
            //edesc.setReadOnly(true);
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    private EnhancedPropertyDescriptor endDesc() {
        try {
            PropertyDescriptor desc = new PropertyDescriptor("end", this.getClass());
            EnhancedPropertyDescriptor edesc = new EnhancedPropertyDescriptor(desc, END_ID);
            desc.setDisplayName("End");
            //edesc.setReadOnly(true);
            return edesc;
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    @Override
    public String getDisplayName() {
        return "Epoch";
    }

}
