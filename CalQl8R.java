package calql8r;
import calql8r.Enums.*;
import calql8r.Functions.GeneralCalFunction;
import calql8r.Values.*;
import calql8r.Logic.*;
import calql8r.Operators.CalAdd;
import calql8r.Operators.CalBase10;
import calql8r.Operators.CalFactorial;
import calql8r.Operators.CalPow;
import calql8r.Operators.CalSubtract;
import calql8r.Operators.GeneralCalOperator;
import calql8r.OperatorsExtra.CalTime;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author MARTIN
 */
public final class CalQl8R implements Runnable {

    private ArrayList<GeneralCalButton> calObjs;
    private CalAns answer;

    //CONSTRUCTORS
    public CalQl8R() {
        calObjs = new ArrayList<>();
        answer = new CalAns(0);
        calObjs.clear();
    }

    /**
     * @param expression The expression to be calculated. For example: 1+3/4*5-(42-24).
     */
    public CalQl8R(String expression) {
        calObjs = new ArrayList<>();
        answer = new CalAns(0);
        calObjs.clear();
        setExpression(expression);
    }

    @Override
    public void run() {
        calObjs = decimalLoop(calObjs);
        calObjs = numberLoop(calObjs);
        calObjs = timeLoop(calObjs);
        calObjs = positiveAndNegativeLoop(calObjs);
        calObjs = bracketAbsLoop(calObjs);
        calObjs = functionLoop(calObjs);
        answer = (CalAns) calculateSolution(calObjs);
    }

    private GeneralCalValue calculateSolution(ArrayList<GeneralCalButton> calculatorObjs) {
        calculatorObjs = decimalLoop(calculatorObjs);
        calculatorObjs = numberLoop(calculatorObjs);
        calculatorObjs = positiveAndNegativeLoop(calculatorObjs);  
        calculatorObjs = bracketLoop(calculatorObjs);
        calculatorObjs = positiveAndNegativeLoop(calculatorObjs);
        return operatorLoop(calculatorObjs);
    }

    private GeneralCalValue calculateSolution(GeneralCalButton... calculatorObjs) {
        ArrayList<GeneralCalButton> calculatorObjsList = new ArrayList<>();
        calculatorObjsList.addAll(Arrays.asList(calculatorObjs));
        calculatorObjsList = functionLoop(calculatorObjsList);
        return calculateSolution(calculatorObjsList);
    }

    public void clearMemory() {
        calObjs.clear();
    }

    /**
     * This method finds the preceeding and proceeding number from the decimal point and combines them.
     * @param calculatorObj
     * @return
     */
    private ArrayList<GeneralCalButton> decimalLoop(ArrayList<GeneralCalButton> calculatorObj) {

        try {
            int searchingIndex = 0;
            while (searchingIndex < calculatorObj.size()) {

                if (calculatorObj.get(searchingIndex) instanceof CalDecimal) {

                    //find preceeding numbers
                    int preceedIndex = searchingIndex - 1;
                    while (calculatorObj.get(preceedIndex) instanceof CalNumber) {
                        preceedIndex--;
                        if (preceedIndex == -1) {
                            preceedIndex = 0;
                            break;
                        }
                    }

                    //find proceeding numbers
                    int proceedIndex = searchingIndex + 1;
                    while (calculatorObj.get(proceedIndex) instanceof CalNumber) {
                        proceedIndex++;
                        if (proceedIndex == calculatorObj.size()) {
                            proceedIndex--;
                            break;
                        }
                    }

                    //fix the indices to corrct first and last values
                    if (preceedIndex > 0) {
                        preceedIndex++;
                    } else if (!(calculatorObj.get(0) instanceof CalNumber)) {
                        preceedIndex++;
                    }
                    if (proceedIndex < (calculatorObj.size() - 1)) {
                        proceedIndex--;
                    } else if (!(calculatorObj.get(calculatorObj.size() - 1) instanceof CalNumber)) {
                        proceedIndex--;
                    }

                    //create the preceeding and proceeding numbers
                    double preceedingNumber = 0;
                    double proceedingNumber = 0;
                    final int BASE = 10;
                    for (int i = preceedIndex; i < searchingIndex; i++) {
                        GeneralCalValue gv = (GeneralCalValue) calculatorObj.get(i);
                        preceedingNumber *= BASE;
                        preceedingNumber += gv.getValue();
                    }

                    int pow = 0;
                    try {
                        for (int i = searchingIndex + 1; i <= proceedIndex; i++) {
                            GeneralCalValue gv = (GeneralCalValue) calculatorObj.get(i);
                            proceedingNumber += gv.getValue() / Math.pow(BASE, ++pow);
                        }
                    } catch (Exception ex) {
                        System.err.println("Error...0x01\n" + "decimal loop error:proceeding number\n" + ex.getMessage());

                    }

                    //replace the objects
                    int number_of_object = proceedIndex - preceedIndex;
                    calculatorObj.add(preceedIndex, new GeneralCalValue(preceedingNumber + proceedingNumber));

                    //removing
                    for (int i = 0; i <= number_of_object; i++) {
                        int del = preceedIndex + 1;
                        calculatorObj.remove(del);
                    }

                    //reset searching 
                    searchingIndex = -1;
                }

                //contiune or break the searching loop
                searchingIndex++;
            }

            return calculatorObj;
        } catch (Exception ex) {
            System.err.println("Error...0x01\n" + "decimal loop error\n" + ex.getMessage());
        }
        return null;
    }

    /**
     * Convert adjacent one object numbers into one general value number
     * @param calculatorObj
     * @return
     */
    private ArrayList<GeneralCalButton> numberLoop(ArrayList<GeneralCalButton> calculatorObj) {
        try {
            int searchingIndex = 0;
            while (searchingIndex < calculatorObj.size()) {
                if (calculatorObj.get(searchingIndex) instanceof CalNumber) {

                    //count adjacent numbers
                    int last_number = searchingIndex;
                    while (calculatorObj.get(last_number) instanceof CalNumber) {
                        last_number++;
                        if (last_number == calculatorObj.size()) {
                            break;
                        }
                    }

                    double value = 0;
                    final int BASE = 10;
                    for (int i = searchingIndex; i < last_number; i++) {
                        value *= BASE;
                        value += ((CalNumber) calculatorObj.get(i)).getValue();
                    }

                    calculatorObj.add(searchingIndex, new GeneralCalValue(value));
                    //delete existing CalNumbers
                    for (int i = searchingIndex + 1; i <= last_number; i++) {
                        int del = searchingIndex + 1;
                        calculatorObj.remove(del);
                    }

                    //reset search loop
                    searchingIndex = 0;
                    continue;
                }

                searchingIndex++;
            }

            return calculatorObj;
        } catch (Exception ex) {
            System.err.println("Error...0x2\n" + "number loop error\n" + ex.getMessage());
        }
        return null;
    }

    /**
     * This method finds an operator and calculate the value from left and right values.
     * @param calculatorObj
     * @return
     */
    private CalAns operatorLoop(ArrayList<GeneralCalButton> calculatorObj) {
        try {

            //loop through emumeration of operators to mimic bodmas
            for (EnumCalObjs operator : EnumCalObjs.values()) {

                int searchingIndex = 0;
                while (searchingIndex < calculatorObj.size()) {
                    if (calculatorObj.get(searchingIndex) instanceof GeneralCalOperator) {
                        GeneralCalOperator opr = ((GeneralCalOperator) calculatorObj.get(searchingIndex));
                        if (opr.getType() == operator) {

                            try {
                                //get and calculate values
                                GeneralCalValue gv1 = (GeneralCalValue) calculatorObj.get(searchingIndex - 1);
                                if (!opr.usesOneNumber()) {

                                    GeneralCalValue gv2 = (GeneralCalValue) calculatorObj.get(searchingIndex + 1);

                                    opr.setNumber1(gv1.getValue());
                                    opr.setNumber2(gv2.getValue());
                                } else {

                                    opr.setNumber1(gv1.getValue());
                                    opr.setNumber2(gv1.getValue());
                                }

                                GeneralCalValue gv = opr.calculate();

                                if (gv instanceof CalAns) {
                                    System.err.println("Error...0x3000 Calculation Error");
                                    return (CalAns) gv;
                                }

                                //replace expression
                                calculatorObj.add(searchingIndex - 1, gv);
                                calculatorObj.remove(searchingIndex);//num1
                                calculatorObj.remove(searchingIndex);//operator
                                if (!opr.usesOneNumber()) {
                                    calculatorObj.remove(searchingIndex);//num2
                                }

                                //reset search
                                searchingIndex = -1;

                            } catch (Exception ex) {
                                System.err.println("Error...0x4\n" + "operator loop error\ncalculatorObj.size()=" + calculatorObj.size());
                                calculatorObj.forEach((calBtn) -> {
                                    System.err.print(calBtn.displayText());
                                });
                                ex.printStackTrace();
                                
                                CalAns ans = new CalAns();
                                ans.setError(EnumError.SYNTAX);
                                return ans;
                            }
                        }

                    }
                    searchingIndex++;
                }
            }
            try {
                double ans = ((GeneralCalValue) calculatorObj.get(0)).getValue();
                return new CalAns(ans);
            } catch (Exception ex) {
                System.err.println("Error...0x3\n" + "operator loop error: last object not an answer\n" + ex.getMessage());
            }

        } catch (Exception ex) {
            System.err.println("Error...0x3\n" + "operator loop error\n" + ex.getMessage());
        }
        CalAns a = new CalAns(0);
        a.setError(EnumError.SYNTAX);
        return a;
    }

    /**
     * This method finds the inner most bracket and calculate the solution to the expression
     * @param calculatorObj
     * @return
     */
    private ArrayList<GeneralCalButton> bracketLoop(ArrayList<GeneralCalButton> calculatorObj) {
        try {

            int searching = 0;
            while (searching < calculatorObj.size()) {
                if (calculatorObj.get(searching) instanceof CalBracketOpen) {
                    final int NONE = -1;
                    int last_opening_bracket = NONE;
                    int first_closing_bracket = NONE;

                    //get index of innermost opening bracket
                    for (int i = 0; i < calculatorObj.size(); i++) {
                        if (calculatorObj.get(i) instanceof CalBracketOpen) {
                            last_opening_bracket = i;
                        }
                    }

                    //get index of first closing bracket
                    int closing_bracket_search = last_opening_bracket;
                    while (closing_bracket_search < calculatorObj.size()) {

                        if (calculatorObj.get(closing_bracket_search) instanceof CalBracketClose) {
                            first_closing_bracket = closing_bracket_search;

                            //break this local loop
                            closing_bracket_search = calculatorObj.size();
                        }
                        closing_bracket_search++;
                    }

                    //check if brackets are in pairs
                    if ((first_closing_bracket != NONE) && (last_opening_bracket == NONE)) {
                        System.err.println("Error...0x06\n" + "Bracket Loop Error: No Opening Bracket\n");
                        return null;
                    }
                    //check if brackets are NOT ) (
                    if ((first_closing_bracket < last_opening_bracket)) {
                        System.err.println("Error...0x07\n" + "Bracket Loop Error: No Opening Bracket\n");
                        return null;
                    }
                    //check if brackets are in pairs
                    if ((first_closing_bracket == NONE) && (last_opening_bracket != NONE)) {
                        System.err.println("Error...0x08\n" + "Bracket Loop Error: No Closing Bracket\n");
                        return null;
                    }

                    //get the expression inside the bracket
                    ArrayList<GeneralCalButton> mini_expression = new ArrayList<>();
                    mini_expression.clear();
                    for (int i = last_opening_bracket + 1; i < first_closing_bracket; i++) {
                        mini_expression.add(calculatorObj.get(i));
                    }

                    //calculate solution of the expression inside the bracket
                    GeneralCalValue gv = (GeneralCalValue) operatorLoop(mini_expression);

                    //replace brackets and exprssion with general value object
                    calculatorObj.add(last_opening_bracket, gv);
                    int number_of_items = first_closing_bracket - last_opening_bracket;
                    //removing objects
                    for (int i = 0; i <= number_of_items; i++) {
                        int del = last_opening_bracket + 1;
                        calculatorObj.remove(del);
                    }

                    //reset loop
                    searching = -1;
                }
                searching++;
            }

            return calculatorObj;
        } catch (Exception ex) {
            System.err.println("Error...0x05\n" + "Bracket Loop Error\n" + ex.getMessage());
        }
        return null;
    }

    /**
     * This method finds repeating minus and addition signs and replaces them
     * @param calculatorObj
     * @return
     */
    private ArrayList<GeneralCalButton> positiveAndNegativeLoop(ArrayList<GeneralCalButton> calculatorObj) {
        try {

            int searchingIndex = 0;
            while (searchingIndex < calculatorObj.size() - 1) {

                if (calculatorObj.get(searchingIndex) instanceof CalSubtract && (searchingIndex != calculatorObj.size() - 1)) {
                    try {
                        //is the subtract/minus is the first object
                        if (searchingIndex == 0 && calculatorObj.get(searchingIndex + 1) instanceof GeneralCalValue) {
                            double value = ((GeneralCalValue) calculatorObj.get(searchingIndex + 1)).getValue();
                            ((GeneralCalValue) calculatorObj.get(searchingIndex + 1)).setValue(-value);
                            calculatorObj.remove(0);
                            continue;
                        }

                        //if subtract poses as a minus 
                        boolean search_within_range = searchingIndex > 0 && searchingIndex < calculatorObj.size() - 1;

                        if (search_within_range) {
                            boolean left_object_is_potential_value = (calculatorObj.get(searchingIndex - 1) instanceof GeneralCalValue)
                                    || (calculatorObj.get(searchingIndex - 1) instanceof CalBracketClose);
                            
                            
                            boolean right_object_is_value = calculatorObj.get(searchingIndex + 1) instanceof GeneralCalValue;

                            if (!left_object_is_potential_value && right_object_is_value) {
                                double value = ((GeneralCalValue) calculatorObj.get(searchingIndex + 1)).getValue();
                                ((GeneralCalValue) calculatorObj.get(searchingIndex + 1)).setValue(-value);
                                calculatorObj.remove(searchingIndex);
                            }
                        } //if there are two minus objects 
                        else if ((calculatorObj.get(searchingIndex + 1) instanceof CalMinus)
                                || (calculatorObj.get(searchingIndex + 1) instanceof CalSubtract)) {
                            calculatorObj.add(searchingIndex, new CalAdd());
                            calculatorObj.remove(searchingIndex + 1);//minus
                            calculatorObj.remove(searchingIndex + 1);//subtraction

                            //reset search loop
                            searchingIndex = -1;
                        } else if (calculatorObj.get(searchingIndex + 1) instanceof CalAdd) {
                            calculatorObj.remove(searchingIndex + 1);//remove addition
                            //reset search loop
                            searchingIndex = -1;
                        }

                    } catch (Exception ex) {
                        System.err.println("Error...0x10\n" + "Positive and Negative Loop Error: No next object\n" + ex.getMessage());
                    }

                } else if (calculatorObj.get(searchingIndex) instanceof CalMinus) {
                    try {
                        //if there are two minus objects 
                        if ((calculatorObj.get(searchingIndex + 1) instanceof CalMinus)) {
                            calculatorObj.add(searchingIndex, new CalAdd());
                            calculatorObj.remove(searchingIndex + 1);//minus
                            calculatorObj.remove(searchingIndex + 1);//subtraction

                            //reset search loop
                            searchingIndex = -1;

                        } else if ((calculatorObj.get(searchingIndex + 1) instanceof GeneralCalValue)) {
                            //remove minus and negate value
                            double value = -((GeneralCalValue) calculatorObj.get(searchingIndex + 1)).getValue();
                            ((GeneralCalValue) calculatorObj.get(searchingIndex + 1)).setValue(value);
                            calculatorObj.remove(searchingIndex);

                            //reset search loop
                            searchingIndex = -1;
                        }

                    } catch (Exception ex) {
                        System.err.println("Error..0x11\n" + "Positive and Negative Loop Error: No next object" + ex.getMessage());
                    }

                } else if (calculatorObj.get(searchingIndex) instanceof CalAdd && (searchingIndex != calculatorObj.size() - 1)) {

                    if ((calculatorObj.get(searchingIndex + 1) instanceof CalMinus)
                            || (calculatorObj.get(searchingIndex + 1) instanceof CalSubtract)) {
                        try {
                            calculatorObj.add(searchingIndex, new CalSubtract());
                            calculatorObj.remove(searchingIndex + 1);//remove addition
                            calculatorObj.remove(searchingIndex + 1);//remove negative

                            //reset search loop
                            searchingIndex = -1;
                        } catch (Exception ex) {
                            System.err.println("Error..0x12\n" + "Positive and Negative Loop Error: No next object" + ex.getMessage());
                        }

                    } else if ((calculatorObj.get(searchingIndex + 1) instanceof CalAdd)) {
                        try {
                            calculatorObj.remove(searchingIndex);//remove one addition
                            //reset search loop
                            searchingIndex = -1;
                        } catch (Exception ex) {
                            System.err.println("Error..0x13\n" + "Positive and Negative Loop Error: No next object" + ex.getMessage());
                        }
                    }else if((searchingIndex > 1)){
                        
                        //remove the reduntant plus if it is inbetween base 10 and value e.g 10E+7 for 10E7
                        if((calculatorObj.get(searchingIndex - 1) instanceof CalBase10)){
                            try {
                            calculatorObj.remove(searchingIndex);//remove one addition
                            //reset search loop
                            searchingIndex = -1;
                        } catch (Exception ex) {
                            System.err.println("Error..0x13\n" + "Positive and Negative Loop Error: No next object" + ex.getMessage());
                        }
                        }
                    }
                }

                //continue loop
                searchingIndex++;
            }
            return calculatorObj;
        } catch (Exception ex) {
            System.err.println("Error..0x09\n" + "Positive and Negative Loop Error" + ex.getMessage());
        }
        return null;
    }

    /**
     * This methods finds the three time point objects and converts the adjacent time values into a single value 
     * @param calculatorObjs
     * @return
     */
    private ArrayList<GeneralCalButton> timeLoop(ArrayList<GeneralCalButton> calculatorObjs) {
        try {
            int searchingIndex = 0;
            int number_of_found_time_points = 0;
            while (searchingIndex < calculatorObjs.size()) {

                //if still count time points
                if ((searchingIndex == calculatorObjs.size() - 1)
                        && (!(calculatorObjs.get(searchingIndex) instanceof CalTime))
                        && (number_of_found_time_points != 2 && number_of_found_time_points != 0)
                        && calculatorObjs.size() > 1) {
                    System.err.println("Error...0x19000 Time Loop Syntax");
                    return null;
                } else if (calculatorObjs.get(searchingIndex) instanceof CalTime) {
                    CalTime calTime = (CalTime) calculatorObjs.get(searchingIndex);
                    number_of_found_time_points++;

                    //set preceed value
                    if (calculatorObjs.get(searchingIndex - 1) instanceof GeneralCalValue) {
                        GeneralCalValue gv = (GeneralCalValue) calculatorObjs.get(searchingIndex - 1);

                        //if the value is positive
                        if (gv.getValue() > 0) {
                            if (number_of_found_time_points == 1) {
                                calTime.setHours(gv);
                            } else { //the object before the general value must be a time point
                                if (calculatorObjs.get(searchingIndex - 2) instanceof CalTime) {

                                    if (number_of_found_time_points == 2) {
                                        calTime.setMinutes(gv);
                                    } else if (number_of_found_time_points == 3) {
                                        calTime.setSeconds(gv);

                                        //get calculated answer
                                        gv = calTime.calculate();

                                        //add calculated time
                                        int time_expression_length = 6; //#t #t #t
                                        calculatorObjs.add(searchingIndex - (time_expression_length - 1), gv); //add answer

                                        //remove time expression
                                        for (int i = 1; i <= time_expression_length; i++) {
                                            int del = searchingIndex - time_expression_length + 2;
                                            calculatorObjs.remove(del);
                                        }

                                        //reset function
                                        searchingIndex = -1;
                                        number_of_found_time_points = 0;
                                    }
                                } else {
                                    System.err.println("Error...0x16000 Time Loop Syntax Error Brackets");
                                    return null;
                                }
                            }
                        }

                    }//find and evaluate bracket expression then set value
                    else if (calculatorObjs.get(searchingIndex - 1) instanceof CalBracketClose) {
                        int number_of_closed_brackets = 0;
                        int number_of_open_brackets = 0;
                        int start_index = -1;
                        int end_index = searchingIndex - 1;

                        //find indices to encapsulate brackets
                        for (int i = searchingIndex - 1; i >= 0; i--) {
                            if (calculatorObjs.get(i) instanceof CalBracketOpen) {
                                number_of_open_brackets++;
                            } else if (calculatorObjs.get(i) instanceof CalBracketClose) {
                                number_of_closed_brackets++;
                            }
                            if (number_of_closed_brackets == number_of_open_brackets) {
                                start_index = i;
                                break;
                            }
                        }

                        if (start_index != -1) {
                            //calculate bracket expression
                            ArrayList<GeneralCalButton> local_expression = new ArrayList<>();

                            for (int i = start_index; i <= end_index; i++) {
                                local_expression.add(calculatorObjs.get(i));//add    
                            }

                            //get local solution
                            GeneralCalValue gv = calculateSolution(local_expression);

                            //add value 
                            calculatorObjs.add(start_index, gv);

                            //remove bracket expression
                            for (int i = start_index; i <= end_index; i++) {
                                int del = start_index + 1;
                                calculatorObjs.remove(del);
                            }

                            //reset function
                            searchingIndex = -1;
                            number_of_found_time_points = 0;
                            local_expression.clear();

                        } else {
                            System.err.println("Error...0x17000 Time Loop Syntax Error Brackets");
                            return null;
                        }

                    } else {
                        System.err.println("Error...0x18000 Time Loop Syntax Error");
                        return null;
                    }
                }
                searchingIndex++;
            }

            return calculatorObjs;

        } catch (Exception ex) {
            System.err.println("Error...0x15000 Time Loop Error\n" + ex.getMessage());
            return null;
        }

    }

    /**
     * This methods finds the inner most function object and its corresponding
     * parameters and evaluates the expression.
     * @param calculatorObj
     * @return
     */
    private ArrayList<GeneralCalButton> functionLoop(ArrayList<GeneralCalButton> calculatorObj) {

        int searchingIndex = 0;
        int firstOpenBracket = 0;
        int lastClosingBracket = calculatorObj.size() - 1;

        try {

            while (searchingIndex <= lastClosingBracket) {
                if (calculatorObj.get(searchingIndex) instanceof GeneralCalFunction) {

                    //if generalvalue is the next object
                    if (calculatorObj.get(searchingIndex + 1) instanceof GeneralCalValue) {

                        //get the proceed value and calculate the one parametic function
                        GeneralCalFunction fnx = (GeneralCalFunction) calculatorObj.get(searchingIndex);

                        //if value value is being amplified by power, factorial or base
                        if (searchingIndex + 3 <= calculatorObj.size() - 1) {

                            //if the next object is amplifying the number
                            if (calculatorObj.get(searchingIndex + 2) instanceof CalPow
                                    || calculatorObj.get(searchingIndex + 2) instanceof CalFactorial
                                    || calculatorObj.get(searchingIndex + 2) instanceof CalBase10) {

                                //if the very next object is a value
                                if (calculatorObj.get(searchingIndex + 3) instanceof GeneralCalValue) {

                                    //evaluate for single value
                                    GeneralCalOperator opr = (GeneralCalOperator) calculatorObj.get(searchingIndex + 2);
                                    GeneralCalValue value1 = (GeneralCalValue) calculatorObj.get(searchingIndex + 1);
                                    GeneralCalValue value2 = (GeneralCalValue) calculatorObj.get(searchingIndex + 3);
                                    opr.setNumber1(value1.getValue());
                                    opr.setNumber2(value2.getValue());

                                    try {
                                        //calculate
                                        GeneralCalValue gv = opr.calculate();

                                        //replace values and objects
                                        calculatorObj.add(searchingIndex + 1, gv);
                                        calculatorObj.remove(searchingIndex + 2);//remove v1
                                        calculatorObj.remove(searchingIndex + 2);//remove operator that amplifies number
                                        calculatorObj.remove(searchingIndex + 2);//remove v2
                                    } catch (Exception ex) {
                                        System.err.println("Error...0x14000 Function Loop Operator Calculations\n" + ex.getMessage());
                                        return null;
                                    }

                                }
                            }
                        }

                        //add parameter to function
                        fnx.addParameter((GeneralCalValue) calculatorObj.get(searchingIndex + 1));

                        if (fnx.parameterCountIsOK()) {

                            try {

                                GeneralCalValue value = fnx.calculate();

                                calculatorObj.add(searchingIndex, value);

                                calculatorObj.remove(searchingIndex + 1);//remove the function object

                                calculatorObj.remove(searchingIndex + 1);//remove the value

                            } catch (Exception ex) {
                                //return syntax error
                                System.err.println("Error...0x13000 Function Loop Error\n" + ex.getMessage());
                                return null;
                            }

                            /*
                            The class seems to store the same parameters for other functions. 
                            There is a need to clear the memory
                             */
                            fnx.clearParameterMemory();

                            //reset
                            firstOpenBracket = 0;
                            lastClosingBracket = calculatorObj.size() - 1;
                            searchingIndex = -1;

                        } else {
                            //return syntax error
                            System.err.println("Error...0x12000 Function Loop Error");
                            return null;
                        }

                        //if an open bracket is the next object
                    } else if (calculatorObj.get(searchingIndex + 1) instanceof CalBracketOpen) {
                        firstOpenBracket = searchingIndex + 1;
                        int numberOfOpenedBracket = 0;
                        int numberOfClosedBracket = 0;

                        for (int i = firstOpenBracket; i <= lastClosingBracket; i++) {

                            //count the number of brackets
                            if (calculatorObj.get(i) instanceof CalBracketOpen) {
                                numberOfOpenedBracket++;
                            } else if (calculatorObj.get(i) instanceof CalBracketClose) {
                                numberOfClosedBracket++;
                            }

                            //check if the equal there are equal number of coresponding brackets
                            if (numberOfOpenedBracket == numberOfClosedBracket) {
                                lastClosingBracket = i;
                                break;
                            }
                        }

                    }
                } else {
                    //function expression limits where found 
                    if (firstOpenBracket != 0) {

                        //check if there is a inner function
                        boolean innerFunctionExists = false;
                        for (int i = firstOpenBracket; i <= lastClosingBracket; i++) {
                            if (calculatorObj.get(i) instanceof GeneralCalFunction) {
                                innerFunctionExists = true;
                            }
                        }
                        if (!innerFunctionExists) {

                            //storage for all local parameter separators
                            ArrayList<Integer> parameterIndexList = new ArrayList<>();

                            //add index of first open bracket
                            parameterIndexList.add(firstOpenBracket);

                            for (int i = firstOpenBracket; i <= lastClosingBracket; i++) {

                                //get locations of all local parameter separators
                                if (calculatorObj.get(i) instanceof CalParameterSeparator) {
                                    parameterIndexList.add(i);
                                }
                            }
                            //add index of last closing bracket
                            parameterIndexList.add(lastClosingBracket);

                            //split the parameter into a list base on the indices
                            GeneralCalButton parameters[][] = new GeneralCalButton[parameterIndexList.size() - 1][];

                            for (int i = 0; i < parameterIndexList.size(); i++) {
                                final int last_but_one = parameterIndexList.size() - 2;

                                //calculate number of objects in the between the separators
                                int number_of_objects = parameterIndexList.get(i + 1) - parameterIndexList.get(i) - 1;

                                GeneralCalButton expression[] = new GeneralCalButton[number_of_objects];

                                //get expression for parameter
                                int index = 0;
                                for (int j = parameterIndexList.get(i) + 1; j < parameterIndexList.get(i + 1); j++) {
                                    expression[index++] = calculatorObj.get(j);
                                }

                                //add expression to list
                                parameters[i] = expression;

                                if (i == last_but_one) {
                                    break;
                                }
                            }

                            //calculate function solution
                            GeneralCalFunction fnx = (GeneralCalFunction) calculatorObj.get(firstOpenBracket - 1);

                            for (GeneralCalButton[] parameter : parameters) {
                                GeneralCalValue gv = calculateSolution(parameter);
                                fnx.addParameter(gv);
                            }
                            if (fnx.parameterCountIsOK()) {
                                calculatorObj.add(firstOpenBracket - 1, fnx.calculate());

                                //remove calculator objects
                                for (int i = firstOpenBracket - 1; i <= lastClosingBracket; i++) {
                                    int del = firstOpenBracket;
                                    calculatorObj.remove(del);
                                }

                            } else {
                                //return syntax error
                                System.err.println("Error...0x10000 Function Loop Error");
                                return null;
                            }

                            //reset
                            firstOpenBracket = 0;
                            lastClosingBracket = calculatorObj.size() - 1;
                            searchingIndex = -1;

                            //clear memory
                            parameterIndexList.clear();
                            fnx.clearParameterMemory();
                        }
                    }
                }
                //update loop
                searchingIndex++;
            }

            return calculatorObj;
        } catch (Exception ex) {
            System.out.println("Error...0x5000 Function Loop Error " + ex.getMessage());
        }
        return null;
    }

    private ArrayList<GeneralCalButton> bracketAbsLoop(ArrayList<GeneralCalButton> calculatorObj) {

        try {

            int number_of_abs_brackets = 0;
            for (int i = 0; i < calculatorObj.size(); i++) {
                if (calculatorObj.get(i) instanceof CalAbsBracket) {
                    number_of_abs_brackets++;
                }
            }

            //process if the abs bracket count is an even number
            if ((number_of_abs_brackets % 2) == 0) {

                if (number_of_abs_brackets != 0) {
                    //indices for bracket encapsulation
                    int last_open_bracket = -1;
                    int first_closed_bracket = -1;
                    int searching_index = 0;
                    int bracket_count = 0;

                    //search for brackets
                    while (searching_index < calculatorObj.size()) {
                        if (calculatorObj.get(searching_index) instanceof CalAbsBracket) {
                            bracket_count++;

                            //when the last open bracket found
                            if ((number_of_abs_brackets / 2) == bracket_count) {
                                last_open_bracket = searching_index;

                                //find the corresponding closing bracket
                                for (int i = searching_index; i < calculatorObj.size(); i++) {
                                    if (calculatorObj.get(i) instanceof CalAbsBracket) {
                                        first_closed_bracket = i;
                                    }
                                }

                                //if both indices are not -1
                                if (!(last_open_bracket == -1 || first_closed_bracket == -1)) {

                                    //get local expression and calculate
                                    ArrayList<GeneralCalButton> local_expression = new ArrayList<>();
                                    for (int i = last_open_bracket + 1; i < first_closed_bracket; i++) {
                                        local_expression.add(calculatorObj.get(i));
                                    }

                                    //caculate expression
                                    GeneralCalValue gv = calculateSolution(functionLoop(local_expression));

                                    //makea a negative answer positive
                                    if (gv.getValue() < 0) {
                                        gv.setValue(gv.getValue() * -1);
                                    }

                                    //add answer to list
                                    calculatorObj.add(last_open_bracket, gv);

                                    //remove bracket expression
                                    for (int i = last_open_bracket + 1; i <= first_closed_bracket; i++) {
                                        int del = last_open_bracket + 1;
                                        calculatorObj.remove(del);
                                    }

                                    //reset 
                                    number_of_abs_brackets -= 2;//remove the two brackets
                                    searching_index = -1;
                                    first_closed_bracket = -1;
                                    last_open_bracket = -1;
                                    local_expression.clear();
                                }
                            }
                        }

                        //continue loop 
                        searching_index++;
                    }

                }

            } else {
                System.err.println("Error...0x21000 Bracket Absolute Loop Error: Absolute Bracket Count are not even" + number_of_abs_brackets);
                return null;
            }

            return calculatorObj;

        } catch (Exception ex) {
            System.out.println("Error...0x20000 Bracket Absolute Loop Error\n" + ex.getMessage());
        }
        return null;
    }

    /**
     * This method converts the string expression to calculator objects for calculations.
     * @param expression The expression to calculate.
     * @return Returns if a boolean indicating if expression was converted successfully 
     */
    public boolean setExpression(String expression) {
        //remove all white space
        expression = expression.replaceAll(" ", "");

        clearMemory();

        for (int i = 0; i < expression.length(); i++) {
            for (EnumCalObjs calculatorObj : EnumCalObjs.values()) {

                int index1 = expression.indexOf(calculatorObj.getDisplayText1());
                int index2 = expression.indexOf(calculatorObj.getDisplayText2());

                boolean the_object_is_first = (index1 == 0 || index2 == 0);

                if (the_object_is_first) {
                    calObjs.add(calculatorObj.getCalculateObject());

                    //get length
                    int length_of_finding_text;
                    if (index1 == 0) {
                        length_of_finding_text = calculatorObj.getDisplayText1().length();
                    } else {
                        length_of_finding_text = calculatorObj.getDisplayText2().length();
                    }

                    //truncate beginning of the text
                    expression = expression.substring(length_of_finding_text, expression.length());

                    //reset for loop
                    i = -1;
                }
            }
        }

        //error in converting string
        if (expression.length() > 0) {
            System.err.println("Error...0x16\nCalQl8R::SetExpression Method Error\n");
            CalAns a = new CalAns(0);
            a.setError(EnumError.SYNTAX);
            clearMemory();
            calObjs.add(a);
            return false;
        }

        return true;

    }

    public String getAnswer() {
        final int NO_LIMITS_TO_SIG_FIGS = -1;
        return getAnswer(NO_LIMITS_TO_SIG_FIGS);
    }

    public String getAnswer(int number_of_dps) {

        if (answer.getError() == EnumError.MATH) {
            return EnumError.MATH.TEXT;
        } else if (answer.getError() == EnumError.SYNTAX) {
            return EnumError.SYNTAX.TEXT;
        }

        //round off ans value to remove unnecessary trailing values
        //get base on answer foramt
        if (null != answer.getAnswerFormat()) {
            switch (answer.getAnswerFormat()) {
                case VALUE:
                    //round off ans value to remove unnecessary trailing values
                    String ansText;
                    if (number_of_dps == -1) {
                        ansText = String.valueOf(answer.getValue());
                    } else {
                        ansText = String.valueOf(answer.getValue(number_of_dps));
                    }
                    BigDecimal ans = new BigDecimal(ansText).stripTrailingZeros();
                    ans.round(MathContext.DECIMAL64);
                    return ans.toEngineeringString();
                case FRACTION:
                    return answer.getAnswerFraction();
                case TIME:
                    return answer.getAnswerTime();
                default:
                    break;
            }
        }
        return "0";
    }

    /**
     * This method change the form of the answer in a time form. *Set after the calculator has already run*
     */
    public void setAnswerFormatToTime() {
        answer.setAnswerFormat(EnumAnswerFormat.TIME);
    }

    /**
     * This method change the form of the answer in a fraction form. For instance 1.5 to 1/1/2. *Set after the calculator has already run*
     */
    public void setAnswerFormatToFraction() {
        answer.setAnswerFormat(EnumAnswerFormat.FRACTION);
    }
}
