package de.bundesbank.transreg.util;

/**
 *
 * @author Nina Gonschorreck
 */
@lombok.Data
public class Group {

    private int number;

    public Group(int n) {
        number = n;
    }

    @Override
    public String toString() {
        return "Group " + this.number;
    }
}
