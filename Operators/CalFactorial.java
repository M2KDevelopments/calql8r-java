/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calql8r.Operators;

import calql8r.Enums.EnumCalObjs;
import calql8r.Values.GeneralCalValue;
import calql8r.Interfaces.InterfaceTypes;

/**
 *
 * @author MARTIN
 */
public class CalFactorial extends GeneralCalOperator implements InterfaceTypes{

    @Override
    public GeneralCalValue calculate() {
        if (numberIsDecimal(number1)){
            System.err.println("Error...0x4000 Factorial number is not an integer.");
            return mathError();
                       
        }else{
            long fc = factorial((int) number1);
            return  new GeneralCalValue((double) fc);
        }
    }

    @Override
    public String displayText() {
        return "!";
    }

    @Override
    public EnumCalObjs getType() {
        return EnumCalObjs.CAL_FACTORIAL;
    }

    @Override
    public boolean usesOneNumber() {
        return true;
    }
    
}
