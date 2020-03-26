/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calql8r.Values;
import java.util.Random;

/**
 *
 * @author MARTIN
 */
public class CalRandom extends GeneralCalValue{

    //Constructor create a three-valued random decimal number e.g 0.142
    public CalRandom(){
       Random rnd = new Random((int) (Math.random() * 7000));
       int a = rnd.nextInt(100);
       int b = rnd.nextInt(100)/10;
       int c = rnd.nextInt(100)/100;
       value = (a+b+c)/100.0;
    }
    
    @Override
    public String displayText() {
       return "#Rand";
    }
    
}
