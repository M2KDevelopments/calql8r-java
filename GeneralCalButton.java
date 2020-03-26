/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calql8r;

import calql8r.Enums.EnumError;
import calql8r.Values.CalAns;

/**
 *
 * @author MARTIN
 */
abstract public class GeneralCalButton {

    public static final int FUNCTION = 1;
    public static final int OPERATOR = 2;
    public static final int EXTRA_OPERATOR = 3;
    public static final int LOGIC = 4;
    public static final int VALUES = 5;

    abstract public String displayText();

    @Override
    public String toString() {
        return displayText();
    }

    protected CalAns mathError() {
        CalAns a = new CalAns(0);
        a.setError(EnumError.MATH);
        return a;
    }

    protected CalAns syntaxError() {
        CalAns a = new CalAns(0);
        a.setError(EnumError.SYNTAX);
        return a;
    }
}
