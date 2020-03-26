/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calql8r.Values;

import calql8r.Enums.EnumAnswerFormat;
import calql8r.Enums.EnumError;

/**
 *
 * @author MARTIN
 */
public class CalAns extends GeneralCalValue {

    private EnumError error;
    private EnumAnswerFormat answerFormat;
    private final String answer_fraction, answer_time;

    public CalAns() {
        error = EnumError.NONE;
        answerFormat = EnumAnswerFormat.VALUE;
        answer_fraction = "0";
        answer_time = "0⁰0⁰0⁰";
    }

    public CalAns(double ans) {
        value = ans;
        error = EnumError.NONE;
        answerFormat = EnumAnswerFormat.VALUE;
  
        if(ans != 0){
          answer_fraction = calculateFractionFormat(ans);
          answer_time = calculateTimeFormat(ans);  
        }else{
            answer_time = "0⁰0⁰0⁰";
            answer_fraction = "0";  
        }
        
    }

    @Override
    public String displayText() {
        return "Ans";
    }

    public EnumError getError() {
        return error;
    }

    public void setError(EnumError error) {
        this.error = error;
    }

    public EnumAnswerFormat getAnswerFormat() {
        return answerFormat;
    }

    public void setAnswerFormat(EnumAnswerFormat answerFormat) {
        this.answerFormat = answerFormat;
    }

    public double getValue(int number_of_dps) {

        //convert double to string
        String value_str = String.valueOf(this.value);

        //locate decimal point
        int dp = value_str.indexOf(".");

        try {
            if (dp > 0) {
                //truncate the string
                value_str = value_str.substring(0, dp + number_of_dps + 1);

                //convert back to double
                double new_value = Double.parseDouble(value_str);

                return new_value;
            }

        } catch (NumberFormatException ex) {
            System.err.println("Error...0x14\nCalAns::GetValue Method Error\nNumber format could not be parse");
            return value;
        } catch (Exception ex) { //numbers of dps > substring length
            System.out.println("Error...0x15\nCalAns::GetValue Method Error\nNumber decimals Places greater than value");
            return value;
        }
        return value;
    }

    private String calculateFractionFormat(double ans) {

        int whole = (int) ans;
        boolean number_is_negative = (whole < 0);
        if (number_is_negative) {
            whole *= -1;
        }

        //get decimal part
        double decimal = Math.abs(ans) - Math.abs(whole);
        int numerator = -1, denomerator = -1;

        //number is already a integer
        if (decimal == 0) {
            return "" + ans;
        }

        //multiple value and see if it rurns into a whole number
        for (int i = 1; i < 1000; i++) {

            // its a whole number
            double n1 = decimal * i;
            int n2 = (int) n1;
            boolean number_is_an_integer = ((n1 - n2) == 0);
            if (number_is_an_integer) {
                denomerator = i;
                numerator = n2;
                break;
            }
        }

        //check if numerator and denomator where found
        if (numerator == -1 || denomerator == -1) {
            return String.valueOf(ans);
        } else {
            String fraction = numerator + "/" + denomerator;
            String whole_number = "";
            if (whole > 0) {
                if (number_is_negative) {
                    whole *= -1;
                }
                whole_number = whole + "/";
            }
            return whole_number + fraction;
        }
    }

    private String calculateTimeFormat(double ans) {
        final int HR_MIN_SEC = 60;
        //convert to time format
        if (ans > 0) {
            int hr = (int) ans;
            double min = ((ans - (int) ans) * HR_MIN_SEC);
            double sec = (min - (int) min) * HR_MIN_SEC;
            return hr + "⁰" + (int) min + "⁰" + (int) sec + "⁰";
        } else {
            return ""+ans;
        }
    }

    public String getAnswerFraction() {
        return answer_fraction;
    }

    public String getAnswerTime() {
        return answer_time;
    }

}
