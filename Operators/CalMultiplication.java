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
public class CalMultiplication  extends GeneralCalOperator implements InterfaceTypes{

    @Override
    public GeneralCalValue calculate() {
        double sln = number1 * number2;
        GeneralCalValue value = new GeneralCalValue(sln);
        return value;
    }

    @Override
    public String displayText() {
         return "×";
    }

    @Override
    public EnumCalObjs getType() {
         return  EnumCalObjs.CAL_MULTIPLICATION;
    }

    @Override
    public boolean usesOneNumber() {
 return false;
    }
    
}