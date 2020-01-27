/* 
 * Copyright 2018 Deutsche Bundesbank
 * 
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent 
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl.html
 * 
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package de.bundesbank.transreg.ui.nodes;

/**
 *
 * @author s4504gn
 */
public enum NodesLevelEnum {

    ORIGINAL("Original"), GROUP("Group"), ACTIVE("Active"), CENTERUSER("Centeruser"), EXTENDING("Extending"), LEADLAG("Lead/Lag");

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
