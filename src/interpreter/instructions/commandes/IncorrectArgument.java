package interpreter.instructions.commandes;

public class IncorrectArgument extends Exception {
    public IncorrectArgument(String errorMessage) {  
        super(errorMessage);  
    }
}
