package de.bundesbank.transreg.util;

import ec.tstoolkit.data.DataBlock;
import ec.tstoolkit.timeseries.Day;
import ec.tstoolkit.timeseries.regression.AbstractSingleTsVariable;
import ec.tstoolkit.timeseries.regression.IUserTsVariable;
import ec.tstoolkit.timeseries.simplets.TsDomain;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.timeseries.simplets.TsPeriod;

/**
 *
 * @author Nina Gonschorreck
 */
//ToDo Nina - 02: Warum sind soviele Methoden nicht implementiert, muss man dann extenden
public class Epoch extends AbstractSingleTsVariable implements IUserTsVariable, Cloneable {

    private Day start, end;

    public Epoch(Day start, Day end) {
        this.start = start;
        this.end = end;
    }

    public Epoch() {
        /*start = Day.toDay().minus(1);
        end = Day.toDay();*/
    }

    public Day getStart() {
        return start;
    }

    public void setStart(Day day) {
        start = day;
    }

    public Day getEnd() {
        return end;
    }

    public void setEnd(Day day) {
        end = day;
    }

    @Override
    public Epoch clone() {
        try {
            return (Epoch) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    @Override
    public void data(TsPeriod start, DataBlock data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDescription(TsFrequency context) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isSignificant(TsDomain domain) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        if (start != null && end != null) {
            return start.toString() + " - " + end.toString();
        }
        return "";
    }
}
