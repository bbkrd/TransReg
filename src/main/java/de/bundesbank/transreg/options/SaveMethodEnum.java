/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.options;

import org.openide.util.NbBundle;

/**
 *
 * @author s4504gn
 */
@NbBundle.Messages({"NONE=None", "AUTOMATIC=Automatic", "REQUEST=Request"})
public enum SaveMethodEnum {

    NONE(Bundle.NONE()), AUTOMATIC(Bundle.AUTOMATIC()), REQUEST(Bundle.REQUEST());

    private final String name;
    
    SaveMethodEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.toLowerCase();
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
