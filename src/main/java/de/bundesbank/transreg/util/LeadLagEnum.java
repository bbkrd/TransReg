package de.bundesbank.transreg.util;

/**
 *
 * @author Nina Gonschorreck
 */


public enum LeadLagEnum {
    
    Lead("Lead"),
    Lag("Lag");
    
    private String name;
    
    private LeadLagEnum(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    
    public static LeadLagEnum fromString(String text){
        for(LeadLagEnum b : LeadLagEnum.values()){
            if(text.equalsIgnoreCase(b.name)){
                return b;
            }
        }
        return null;
    }
    
}
