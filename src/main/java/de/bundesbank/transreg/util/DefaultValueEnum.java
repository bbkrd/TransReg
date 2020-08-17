package de.bundesbank.transreg.util;

/**
 *
 * @author s4504gn
 */
public enum DefaultValueEnum {

    ZERO("0.0", 0.0),
    NaN("NaN", Double.NaN);

    private final String name;
    private final double value;

    private DefaultValueEnum(String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name;
    }

    public static DefaultValueEnum fromString(String text) {
        if (ZERO.name.equalsIgnoreCase(text)) {
            return ZERO;
        } else {
            return NaN;
        }
    }

    public double getValue() {
        return value;
    }
}
