/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.ui.nodes;

/**
 *
 * @author s4504gn
 */
public enum NodesLevelEnum {

    ORIGINAL("Original"), GROUP("Group"), ACTIVE("Active"), CENTERUSER("Centeruser");

    private String name;

    private NodesLevelEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static NodesLevelEnum fromString(String text) {
        for (NodesLevelEnum b : NodesLevelEnum.values()) {
            if (text.equalsIgnoreCase(b.name)) {
                return b;
            }
        }

        return ORIGINAL;
    }

}
