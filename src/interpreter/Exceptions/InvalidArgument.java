package interpreter.Exceptions;

/**
 * Must be thrown if an argument is invalid
 */
public class InvalidArgument extends Exception{
    public InvalidArgument(String errorMessage) {  
        super(errorMessage);  
    }  
}
