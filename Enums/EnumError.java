/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calql8r.Enums;

/**
 *
 * @author MARTIN
 */
public enum EnumError {
    NONE("NONE"),
    MATH("MATH_ERROR"),
    SYNTAX("SYNTAX_ERROR");
    public final String TEXT;
    EnumError(String error){
        TEXT = error;
    }
}
