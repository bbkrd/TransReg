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
    
    public static Group fromString(String text){
       return new Group(Integer.parseInt(text.substring(6)));
    }
}
