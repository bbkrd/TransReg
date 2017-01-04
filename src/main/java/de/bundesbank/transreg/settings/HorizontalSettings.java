/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.settings;

/**
 *
 * @author s4504gn
 */
public class HorizontalSettings {

    private boolean enable = false;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean b) {
        enable = b;
    }

    public HorizontalSettings copy() {
        HorizontalSettings copy = new HorizontalSettings();
        copy.setEnable(enable);
        return copy;
    }

    public boolean isDefault() {
        return !enable;
    }

    public String getInfo() {
        return "Horizontal: " + enable + "";
    }
}
