/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calql8r.Values;

/**
 *
 * @author MARTIN KULULANGA
 */
public class CalNumber extends GeneralCalValue {
    public CalNumber(int num){
        value = num;
    }

    @Override
    public String displayText() {
       return String.valueOf((int)value);
    }
}
