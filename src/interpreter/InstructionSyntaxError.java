package interpreter;


public class InstructionSyntaxError extends Exception {  
    public InstructionSyntaxError(String errorMessage) {  
    super(errorMessage);  
    }  
}  