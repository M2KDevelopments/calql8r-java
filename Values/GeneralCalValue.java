/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calql8r.Values;

import calql8r.GeneralCalButton;

/**
 *
 * @author MARTIN
 */
public class GeneralCalValue extends GeneralCalButton {
 
    protected double value;
    
    public GeneralCalValue(){
         this.value= 0;
    }
    
    public GeneralCalValue(double value){
        this.value= value;
    }

    @Override
    public String displayText(){
        return "#GV ("+value+")";
    } 
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
}
