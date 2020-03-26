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
public class CalCombinations extends GeneralCalOperator implements InterfaceTypes{

    @Override
    public GeneralCalValue calculate() {
        boolean bothNumbersAreInts = !numberIsDecimal(number1) && !numberIsDecimal(number2);
        if (bothNumbersAreInts) {
            long n = factorial((int) number1);
            long r = factorial((int) number2);
            long n_r = factorial((int) number1 - (int) number2);
            double c = (double) n / ((n_r) * r);

            //number are to big for arithematic operations
            if (c < 0) {
                System.err.println("Error...0x2100 Combinations numbers are to big for arithematic operations");
                return mathError();
            }
            return new GeneralCalValue(c);
        } else {
            System.err.println("Error...0x2000 Combinations numbers have to be intergers");
            return mathError();
        }
    }

    @Override
    public String displayText() {
        return "C";
    }

    @Override
    public EnumCalObjs getType() {
        return EnumCalObjs.CAL_COMBINATION;
    }

    @Override
    public boolean usesOneNumber() {
 return false;
    }

}
