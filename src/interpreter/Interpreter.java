package interpreter;

import fx.*;


public class Interpreter {
    
    private DrawingTab parentTab;
    private String rawInstruction;

    public Interpreter(DrawingTab parentTab, String instructions){
        this.parentTab = parentTab;
        this.rawInstruction = instructions;

    }
    
}
