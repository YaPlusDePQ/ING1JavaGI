package interpreter.Exceptions;


/**
 * Must be thrown if the syntax isn't respected
 */
public class SyntaxError extends Exception{
    public SyntaxError(String errorMessage) {  
        super(errorMessage);  
    }  
}
