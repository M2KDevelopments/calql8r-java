/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calql8r.OperatorsExtra;

import calql8r.GeneralCalButton;
import calql8r.Values.GeneralCalValue;

/**
 *
 * @author MARTIN
 */
public class CalTime extends GeneralCalButton {

    private double hr, min, sec;

    @Override
    public String displayText() {
        return "⁰";
    }

    public void setHours(GeneralCalValue hours) {
        hr = hours.getValue();
    }

    public void setMinutes(GeneralCalValue minutes) {
        min = minutes.getValue();
    }

    public void setSeconds(GeneralCalValue seconds) {
        sec = seconds.getValue();
    }

    public GeneralCalValue calculate() {
        
        //carry of the 60
        final int HR_MIN_SEC = 60;
        min += (int) (sec / HR_MIN_SEC); //add 60 seconds as a minutes
        sec = sec % HR_MIN_SEC;

        hr += (int) (min / HR_MIN_SEC); //add 60 minuutes as an hour
        min = (min % HR_MIN_SEC); //set remainder as minutes
        
        
        //fix decimals in values

        //hour        
        min += (hr - (int)hr) * HR_MIN_SEC;//convert and add decimals to minutes
        hr = (int) hr;
        
        //minutes
        sec += (min - (int) min) * HR_MIN_SEC;//convert and add decimals to seconds
        min = (int) min;
        
        //convert to one value
        double time_value = (hr) + (min/HR_MIN_SEC) + (sec/(HR_MIN_SEC*HR_MIN_SEC));
        return new GeneralCalValue(time_value);
    }
}
