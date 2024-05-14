package interpreter.instructions.commandes;

public class incorrectArgument extends Exception {
    public incorrectArgument(String errorMessage) {  
        super(errorMessage);  
    }
}
