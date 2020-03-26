/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calql8r.Functions;

import calql8r.GeneralCalButton;
import calql8r.Values.GeneralCalValue;

/**
 *
 * @author MARTIN
 */
abstract public class GeneralCalFunction extends GeneralCalButton {

    public GeneralCalFunction() {
        parameters = new GeneralCalValue[0];
    }   
    
    protected GeneralCalValue parameters[];
    
    protected abstract int getNumberOfNeededParameters();

    public abstract GeneralCalValue calculate();

    public void addParameter(GeneralCalValue value) {
    
        GeneralCalValue par[] = new GeneralCalValue[parameters.length + 1];
        //add item to array
        System.arraycopy(parameters, 0, par, 0, parameters.length);
        par[par.length - 1] = value;
        parameters = par;
    }

    /**
     * The class seems to store the same parameters for other functions. There is a need to clear the memory
     */
    public void clearParameterMemory(){
         parameters = new GeneralCalValue[0];
    }
    
    public boolean parameterCountIsOK() {
        return getNumberOfNeededParameters() == parameters.length;
    }
}
