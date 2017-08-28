/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.util;

/**
 *
 * @author s4504gn
 */
public enum CenteruserEnum {
    
    None("None"),
    Global("Global mean"), 
    Seasonal("Seasonal mean");
    
    private String name;

    private CenteruserEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static CenteruserEnum fromString(String text) {
        for (CenteruserEnum b : CenteruserEnum.values()) {
            if (text.equalsIgnoreCase(b.name)) {
                return b;
            }
        }
        return None;
    }
}
