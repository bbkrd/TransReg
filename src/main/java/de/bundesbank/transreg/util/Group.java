package de.bundesbank.transreg.util;

/**
 *
 * @author s4504gn
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
