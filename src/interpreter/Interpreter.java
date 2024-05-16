package interpreter;

import fx.*;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* Class creating a interpreter for the drawing langage.
*/
public class Interpreter {
    
    private DrawingTab parentTab; //DrawingTab where the Interpreter must execute on
    private List<String> parsedIntruction; //List of all the instructions
    private int index; //current index in parsedIntruction
    private List<Variable> variables; //list of all the variables declared
    
    private String PATH_TO_COMMAND = "interpreter.instructions.commandes.";
    // private String PATH_TO_FLOWCONTROL = "interpreter.instructions.flowControle.";
    // private String PATH_TO_MEMORY = "interpreter.instructions.memory.";
    
    private Interpreter subFlow = null; //Store a interpretor that will execute code from a sub local space (loop, if, ect..)
    
    /**
    * Constructor
    *
    * @param  parentTab  DrawingTab where the Interpreter must execute on
    * @param  instructions  String of all the instruction
    */
    public Interpreter(DrawingTab parentTab, String instructions){
        this.parentTab = parentTab;
        this.parsedIntruction = Parser.getInstruction(instructions);
        this.index = 0;
        this.variables = new ArrayList<Variable>();
        System.out.println(this);
    }
    
    /**
    * Constructor
    *
    * @param  parentTab  DrawingTab where the Interpreter must execute on
    * @param  instructions  String of all the instruction
    * @param  defaultDefinedVariables  List of already defined variables
    */
    public Interpreter(DrawingTab parentTab, String instructions, List<Variable> defaultDefinedVariables){
        this.parentTab = parentTab;
        this.parsedIntruction = Parser.getInstruction(instructions);
        this.index = 0;
        this.variables = new ArrayList<Variable>(defaultDefinedVariables);
    }
    
    /**
    * Get the index at wish the interpretor is currently at
    *
    * @return current index
    */
    public int getIndex(){
        return this.index;
    }

    /**
    * Get all the variables already defined
    *
    * @return List of all the variables
    */
    public List<Variable> getVariables(){
        return this.variables;
    }
    
    /**
    * Reinitialize the interpretor with a new set of instruction
    *
    * @param  instructions  String of all the instruction
    */
    public void reinitialize(String instructions){
        this.parsedIntruction = Parser.getInstruction(instructions);
        this.index = 0;
        this.variables = new ArrayList<Variable>();
    }
    
    /**
    * Reinitialize the interpretor with a new set of instruction
    *
    * @param  instructions  String of all the instruction
    * @param  defaultDefinedVariables  List of already defined variables
    */
    public void reinitialize(String instructions, List<Variable> defaultDefinedVariables){
        this.parsedIntruction = Parser.getInstruction(instructions);
        this.index = 0;
        this.variables = new ArrayList<Variable>(defaultDefinedVariables);
    }
    
    /**
    * Execute the next instruction
    *
    * @return true if an instruction has been executed, false otherwise
    */
    public boolean runNextInstruction() throws SyntaxError, InvalidArgument{

        //test if finished
        if(this.index >= this.parsedIntruction.size()){
            System.out.println("[Interpreter] No instruction remaining");
            return false;
        }
        
        //test if currently in a sub local space
        if(this.subFlow != null){
            System.out.println("[Interpreter] Running subFlow ...");

            if(this.subFlow.runNextInstruction()){ 
                return true;
            }
            else{
                //if sub local space finished if finished
                System.out.println("[Interpreter] subFlow Finished. Going back on parent flow");
                this.index = this.subFlow.getIndex();
                this.subFlow = null;
            }
        }
        
        // Exctracting instruction structure
        String currentInstruction = Parser.cleanUpInstruction(this.parsedIntruction.get(index));
        System.out.println(String.format("[Interpreter] Instruction: '%s'", currentInstruction));

        List<String> currentInstructionParsed =Arrays.asList(currentInstruction.split(" ", 2));
        
        String name = currentInstructionParsed.get(0);
        String arguments =currentInstructionParsed.get(1);
        
        System.out.println(String.format("[Interpreter] Instruction result (pre-traitement): NAME: '%s', ARG: '%s'", name, arguments));

        //Identification of instruction types
        
        try{
            Class<?> cls = Class.forName(PATH_TO_COMMAND+name);
            System.out.println("[Interpreter] instruction identified as "+cls.getName());
            this.runCommand(cls, arguments);
        }
        catch(ClassNotFoundException e){
            throw new SyntaxError("Instruction doesnt exist");
        }
        
        this.index++;
        return true;
    }


    /**
    * Run a command instruction
    */
    public void runCommand(Class<?> commandClass, String arguments) throws SyntaxError,InvalidArgument{
        //parse the argument to get theire respective value containt in Variables class
        List<Variable> finalArguments = Parser.getValueFromArgument(arguments, this.variables);


        System.out.println(String.format("[Interpreter] Instruction result (post-traitement): NAME: '%s', ARG: '%s'", commandClass.getName(), Arrays.toString(finalArguments.toArray())) );

        try{
            //get the methode execute from the command sub class
            Method m = commandClass.getMethod("execute", DrawingTab.class, List.class );
            //run
            m.invoke(null, this.parentTab, finalArguments); 
        }
        catch(NoSuchMethodException | IllegalAccessException | IllegalArgumentException e){
            //unknow error while accessing/trying to running the methode
            throw new SyntaxError("Command exist but cannot be access");
        }
        catch(InvocationTargetException e){
            //handle excepted exception (wrong input)
            if(e.getCause().getClass().equals(SyntaxError.class)){
                throw (SyntaxError)e.getCause();
            } 
            else{
                throw (InvalidArgument)e.getCause();
            }
        }

        System.out.println("[Interpreter] Instruction executed\n");
    }

    /**
    * Run all instructions
    */
    public void runAllInstructions() throws SyntaxError,InvalidArgument{
        while(this.runNextInstruction());
    }
    
    public String toString(){
        return String.format("[Interpreter] CONFIGURATION: parsedIntruction.size = %d", this.parsedIntruction.size());
    }
}