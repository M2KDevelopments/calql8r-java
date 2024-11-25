# calql8r-java
Java calculator api that converts string expression to a calculateable mathematical expression and outs a numeric answer

# How to use CalQL8R
```
import calql8r.CalQl8R;

public class Main {
    public static void main(String[] args) {
        
        // Initialize CalQl8R object
        CalQL8R calculator = new CalQl8R();
        
        // Set the expression
        calculator.setExpression("1+1");

        // Get the answer
        String answer = calculator.getAnswer(2);
  
        // Print out the answer
        System.out.println(answer);
    }
}

```
