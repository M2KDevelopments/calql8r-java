/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calql8r.Enums;

import calql8r.Functions.*;
import calql8r.Operators.*;
import calql8r.Logic.*;
import calql8r.GeneralCalButton;
import calql8r.OperatorsExtra.CalTime;
import calql8r.Values.CalNumber;
import calql8r.Values.GeneralCalValue;

/**
 *
 * @author MARTIN
 */
public enum EnumCalObjs {

    //FUNCTIONS
    CAL_SINH(GeneralCalButton.FUNCTION,"sinh","Sinh",new CalSinh()),
    CAL_ARCSIN(GeneralCalButton.FUNCTION,"arcsin","asin",new CalArcSin()),
    CAL_SIN(GeneralCalButton.FUNCTION,"sin","Sin",new CalSin()),
    CAL_COSH(GeneralCalButton.FUNCTION,"cosh","Cosh",new CalCosh()),
    CAL_ARCCOS(GeneralCalButton.FUNCTION,"arccos","acos",new CalArcCos()),
    CAL_COS(GeneralCalButton.FUNCTION,"cos","Cos",new CalCos()),
    CAL_TANH(GeneralCalButton.FUNCTION,"tanh","Tanh",new CalTanh()),
    CAL_ARCTAN(GeneralCalButton.FUNCTION,"arctan","atan",new CalArcTan()),
    CAL_TAN(GeneralCalButton.FUNCTION,"tan","Tan",new CalTan()),
    CAL_EXP(GeneralCalButton.FUNCTION,"exp","e",new CalExp()),
    CAL_LN(GeneralCalButton.FUNCTION,"ln","Ln",new CalLn()),
    CAL_LOG(GeneralCalButton.FUNCTION,"log","Log",new CalLog()),
     
    //OPERATORS
    CAL_FACTORIAL(GeneralCalButton.OPERATOR,"!","!",new CalFactorial()),
    CAL_POW(GeneralCalButton.OPERATOR,"^","^",new CalPow()),
    CAL_COMBINATION(GeneralCalButton.OPERATOR,"C","C",new CalCombinations()),
    CAL_PERMUTATION(GeneralCalButton.OPERATOR,"P","P",new CalPermutations()), 
    CAL_ROOT(GeneralCalButton.OPERATOR,"R","ⁿ√",new CalRoot()),
    CAL_BASE10(GeneralCalButton.OPERATOR,"E","E",new CalBase10()),
    CAL_DIVISION(GeneralCalButton.OPERATOR,"/","÷",new CalDivision()),
    CAL_MULTIPLICATION(GeneralCalButton.OPERATOR,"*","x",new CalMultiplication()),   
    CAL_MODULUS(GeneralCalButton.OPERATOR,"M","MOD",new CalModulus()),
    CAL_SUBTRACT(GeneralCalButton.OPERATOR,"-","─",new CalSubtract()),
    CAL_ADD(GeneralCalButton.OPERATOR,"+","+",new CalAdd()),
    
    
    //EXTRA OPERATORS
    CAL_TIME(GeneralCalButton.OPERATOR,"⁰","t",new CalTime()),
    
    
    //LOGIC 
    CAL_BRACKET_OPEN(GeneralCalButton.LOGIC,"(","[",new CalBracketOpen()),
    CAL_BRACKET_CLOSE(GeneralCalButton.LOGIC,")","]", new CalBracketClose()),
    CAL_DECIMAL(GeneralCalButton.LOGIC,".",".",new CalDecimal()), 
    CAL_PARAMETERSEPARATOR(GeneralCalButton.LOGIC,",",",",new CalParameterSeparator()), 
    CAL_ABSOLUTE_BRACKET(GeneralCalButton.LOGIC,"|","|",new CalAbsBracket()),
    
    //VALUES
    CAL_NUMBER0(GeneralCalButton.VALUES,"0","0",new CalNumber(0)),
    CAL_NUMBER1(GeneralCalButton.VALUES,"1","1",new CalNumber(1)),
    CAL_NUMBER2(GeneralCalButton.VALUES,"2","2",new CalNumber(2)),
    CAL_NUMBER3(GeneralCalButton.VALUES,"3","3",new CalNumber(3)),
    CAL_NUMBER4(GeneralCalButton.VALUES,"4","4",new CalNumber(4)),
    CAL_NUMBER5(GeneralCalButton.VALUES,"5","5",new CalNumber(5)),
    CAL_NUMBER6(GeneralCalButton.VALUES,"6","6",new CalNumber(6)),
    CAL_NUMBER7(GeneralCalButton.VALUES,"7","7",new CalNumber(7)),
    CAL_NUMBER8(GeneralCalButton.VALUES,"8","8",new CalNumber(8)),
    CAL_NUMBER9(GeneralCalButton.VALUES,"9","9",new CalNumber(9)),
    CAL_PI(GeneralCalButton.VALUES,"pi","PI",new GeneralCalValue(Math.PI));
    
    //properties
    private final int type;
    private final String displayText1;
    private final String displayText2;
    private final GeneralCalButton calobject;
    
    EnumCalObjs(int type,String displayText1,String displayText2,GeneralCalButton calobject) {
        this.type = type;
        this.displayText1 = displayText1;
        this.displayText2 = displayText2;
        this.calobject = calobject;
    }

    public int getType(){
        return type;
    }
    
    public String getDisplayText1(){
        return displayText1;
    }
        
    public String getDisplayText2(){
        return displayText2;
    }
    public GeneralCalButton getCalculateObject(){
        return calobject;
    }
}
