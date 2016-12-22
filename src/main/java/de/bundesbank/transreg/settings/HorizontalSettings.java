/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bundesbank.transreg.settings;

import ec.tstoolkit.timeseries.TsPeriodSelector;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsObservation;
import java.util.Iterator;

/**
 *
 * @author s4504gn
 */
public class HorizontalSettings {
    
    TsPeriodSelector sel;

    public TsPeriodSelector getSel() {
        return sel;
    }

    public void setSel(TsPeriodSelector sel) {
        this.sel = sel;
    }
        
    public boolean[] getBooleanForTimeSpan(TsData data){
        boolean[] tmp = new boolean[data.getLength()];
        
        TsData selected = data.select(sel);
        
        int j = 0;
        Iterator<TsObservation> i_selected = selected.iterator();
        TsObservation obs_selected = i_selected.next();
        
        for(TsObservation obs: data){
            if(obs.getPeriod().equals(obs_selected.getPeriod())){
                tmp[j]=true;
                if(i_selected.hasNext()){
                    obs_selected=i_selected.next();
                }
            }else{
                tmp[j]=false;
            }
            j++;
        }
        
       
        return tmp;
    }
    
    public void print(){
        System.out.println("Horizontal Settings\n"+sel.toString());
    }
}
