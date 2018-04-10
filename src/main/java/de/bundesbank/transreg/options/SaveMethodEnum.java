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
package de.bundesbank.transreg.options;

import org.openide.util.NbBundle;

/**
 *
 * @author s4504gn
 */
@NbBundle.Messages({"TRANSREG=in Utilities/TransReg", "VARIABLES=in Utilities/Variables", "BOTH=in Utiities/Variables and in Utilities/TransReg"})
public enum SaveMethodEnum {

    TRANSREG(Bundle.TRANSREG()), VARIABLES(Bundle.VARIABLES()), BOTH(Bundle.BOTH());

    private final String name;
    
    SaveMethodEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    
    public static SaveMethodEnum fromString(String text) {
        for (SaveMethodEnum b : SaveMethodEnum.values()) {
            if (text.equalsIgnoreCase(b.name)) {
                return b;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
    
//    public static int indexFromString(String s){
//        int index = 0;
//        
//        for(SaveMethodEnum b: SaveMethodEnum.values()){
//            if (s.equalsIgnoreCase(b.name)) {
//                return index;
//            }else{
//                index++;
//            }
//        }
//        return 0;
//    }
}
