package interpreter.Exceptions;

public class SyntaxError extends Exception{
    public SyntaxError(String errorMessage) {  
        super(errorMessage);  
    }  
}
