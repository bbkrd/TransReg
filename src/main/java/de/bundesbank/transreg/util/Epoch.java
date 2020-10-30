package de.bundesbank.transreg.util;

import ec.tstoolkit.timeseries.Day;
import ec.tstoolkit.timeseries.Month;

/**
 *
 * @author Nina Gonschorreck
 */

public class Epoch{

    private Day start, end;

    public Epoch(Day start, Day end) {
        this.start = start;
        this.end = end;
    }

    public Epoch() {
        start = new Day(1900, Month.January, 0);
        end = new Day(2100, Month.December, 30);
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
        return new Epoch(start, end);
    }

    @Override
    public String toString() {
        if (start != null && end != null) {
            return start.toString() + " - " + end.toString();
        }
        return "";
    }
}
