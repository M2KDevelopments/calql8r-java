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
public class CalPermutations extends GeneralCalOperator implements InterfaceTypes{

    @Override
    public GeneralCalValue calculate() {
        boolean bothNumbersAreInts = !numberIsDecimal(number1) && !numberIsDecimal(number2);
        if (bothNumbersAreInts) {

            long n = factorial((int) number1);
            long n_r = factorial((int) number1 - (int) number2);
            double p = (double) n / (n_r);

            //number are to big for arithematic operations
            if (p < 0) {
                System.err.println("Error...0x1100 Permutation numbers are to big for arithematic operations");
                return mathError();
            }
        
            return new GeneralCalValue(p);
        } else {
            System.err.println("Error...0x1000 Permutation numbers have to be intergers");
            return   mathError();
        }
    }

    @Override
    public String displayText() {
        return "P";
    }

    @Override
    public EnumCalObjs getType() {
        return EnumCalObjs.CAL_PERMUTATION;
    }

    @Override
    public boolean usesOneNumber() {
 return false;
    }

}
