/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calql8r.Functions;

import calql8r.Values.GeneralCalValue;

/**
 *
 * @author MARTIN
 */
public class CalTan extends GeneralCalFunction{

    @Override
    protected int getNumberOfNeededParameters() {
        return 1;
    }

    @Override
    public GeneralCalValue calculate() {
        return new GeneralCalValue(Math.tan(parameters[0].getValue()));
    }

    @Override
    public String displayText() {
        return "tan";
    }
    
}
