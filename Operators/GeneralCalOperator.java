/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calql8r.Operators;

import calql8r.GeneralCalButton;
import calql8r.Values.GeneralCalValue;
import calql8r.Interfaces.InterfaceTypes;

/**
 *
 * @author MARTIN
 */
abstract public class GeneralCalOperator extends GeneralCalButton implements InterfaceTypes{
    
    protected double number1,number2;   
    abstract public GeneralCalValue calculate();      
    
    @Override
    abstract public String displayText();
    abstract public boolean usesOneNumber();
    
    protected boolean numberIsDecimal(double num){  
        //if the truncated number minus number is not 0. Then is it has decimal numbers
        return ((int)num) - num != 0;      
    }
        
    protected long factorial(int n){
        long fc =1;
        if (n == 0){
            return 1;
        }else if(n < 0){
            fc*=-1;
        }       
        for (int i = n; i >= 1; i--){
            fc *= i;
        }
        return fc;
    }
    
    public double getNumber1() {
        return number1;
    }

    public void setNumber1(double number1) {
        this.number1 = number1;
    }

    public double getNumber2() {
        return number2;
    }

    public void setNumber2(double number2) {
        this.number2 = number2;
    }
    
}
