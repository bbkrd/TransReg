package de.bundesbank.transreg.util;

import ec.tstoolkit.timeseries.Day;
import ec.tstoolkit.timeseries.Month;
import java.text.ParseException;
import org.openide.util.Exceptions;

/**
 *
 * @author Nina Gonschorreck
 */
public class Epoch {

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

    public static Epoch fromString(String text) {
        Epoch result = new Epoch();
        if (text.matches("\\d{4}-\\d{2}-\\d{2} - \\d{4}-\\d{2}-\\d{2}")) {
            String[] split = text.split(" - ");
            try {
                result.setStart(Day.fromString(split[0]));
                result.setEnd(Day.fromString(split[1]));
            } catch (ParseException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return result;
    }
}
