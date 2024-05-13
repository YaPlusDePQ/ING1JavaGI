package interpreter;

import fx.*;
import interpreter.variables.Variable;
import interpreter.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Interpreter {
    
    private DrawingTab parentTab;
    private String rawInstruction;
    private List<String> parsedIntruction;
    private int index;
    private List<Variable> variables;

    public Interpreter(DrawingTab parentTab, String instructions){
        this.parentTab = parentTab;
        this.rawInstruction = instructions;
        
        this.parsedIntruction = Parser.getInstruction(instructions);
        this.index = 0;
        this.variables = new ArrayList<Variable>();
    }

    public Interpreter(DrawingTab parentTab, String instructions, List<Variable> defaultDefinedVariables){
        this.parentTab = parentTab;
        this.rawInstruction = instructions;
        this.parsedIntruction = Parser.getInstruction(instructions);
        this.index = 0;
        this.variables = new ArrayList<Variable>(defaultDefinedVariables);
    }

    public void reinitialize(String instructions){
        this.rawInstruction = instructions;
        this.parsedIntruction = Parser.getInstruction(instructions);
        this.index = 0;
        this.variables = new ArrayList<Variable>();
    }

    public void reinitialize(String instructions, List<Variable> defaultDefinedVariables){
        this.rawInstruction = instructions;
        this.parsedIntruction = Parser.getInstruction(instructions);
        this.index = 0;
        this.variables = new ArrayList<Variable>(defaultDefinedVariables);
    }


    public void runNextInstruction() throws UnknowInstruction{



        this.index++;
    }

    public void runCommand(String Instruction) throws ClassNotFoundException{
        List<String> currentInstruction = Arrays.asList(Instruction.split(" "));

        String command = "interpreter.instruction.command."+currentInstruction.get(0);
        currentInstruction.remove(0);
        
        List<String> argumentsList = Arrays.asList(String.join(" ", currentInstruction).split(","));

        Class<?> cls = Class.forName(command);

        parentTab.drawLine();
    }
    
}
