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
import java.util.function.Function;

/**
* Class creating a interpreter for the drawing langage.
*/
public class Interpreter {

    public final static int END_OF_SCRIPT = 0;
    public final static int INSTRUCTION_EXECUTED = 1;
    public final static int INSTRUCTION_SKIPED = 2;
    public final static int PROCESSE_DONE = 2;
    
    private DrawingTab parentTab; //DrawingTab where the Interpreter must execute on
    private List<String> parsedIntruction; //List of all the instructions
    private int index; //current index in parsedIntruction
    private List<Variable> variables; //list of all the variables declared
    
    private String PATH_TO_COMMAND = "interpreter.instructions.commandes.";
    // private String PATH_TO_FLOWCONTROL = "interpreter.instructions.flowControle.";
    // private String PATH_TO_MEMORY = "interpreter.instructions.memory.";
    
    private Interpreter subFlow = null; //Store a interpretor that will execute code from a sub local space (loop, if, ect..)
    public Function <Interpreter,Integer> onEndOfSCript = null;
    
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

    public void setIndex(int newIndex){
        this.index = newIndex;
    }

    /**
    * Get all the variables already defined
    *
    * @return List of all the variables
    */
    public List<Variable> getVariables(){
        return this.variables;
    }

    public void setIntruction(String instructions){
        this.parsedIntruction = Parser.getInstruction(instructions);
    }

    public void setIntruction(List<String> instructions){
        this.parsedIntruction = instructions;
    }

    public void addInstruction(String instructions){
        List<String> pI = Parser.getInstruction(instructions);
        for(int i=0; i<pI.size(); i++){
            this.parsedIntruction.add(pI.get(i));
        }
    }
    
    public void addInstruction(List<String> instructions){
        for(int i=0; i<instructions.size(); i++){
            this.parsedIntruction.add(instructions.get(i));
        }
    }
    
    /**
    * Execute the next instruction
    *
    * @return true if an instruction has been executed, false otherwise
    */
    public int runNextInstruction() throws SyntaxError, InvalidArgument{

        

        //test if finished
        if(this.index >= this.parsedIntruction.size()){
            if(this.onEndOfSCript != null){
                return onEndOfSCript.apply(this);
            }
            else{
                System.out.println("[Interpreter] No instruction remaining");
                return END_OF_SCRIPT;
            }
        }
        
        //test if currently in a sub local space
        if(this.subFlow != null){
            System.out.println("[Interpreter] Running subFlow ...");
            int resultCode = this.subFlow.runNextInstruction();

            if(resultCode != 0){ 
                return resultCode;
            }
            else{
                //if sub local space finished if finished
                System.out.println("[Interpreter] subFlow Finished. Going back on parent flow");
                this.index = this.subFlow.getIndex();
                this.subFlow = null;
            }
        }
        
        // skip non instruction
        if(!this.parsedIntruction.get(index).matches("[A-Z]+.*")){
            this.index++;
            return 2;
        }

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
        return INSTRUCTION_EXECUTED;
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
            m.invoke(commandClass.newInstance(), this.parentTab, finalArguments); 
        }
        catch(NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InstantiationException e){
            //unknow error while accessing/trying to running the methode
            throw new SyntaxError("Command exist but cannot be access");
        }
        catch(InvocationTargetException e){
            //handle excepted exception (wrong input)
            if(e.getCause().getClass().equals(SyntaxError.class)){
                throw (SyntaxError)e.getCause();
            } 
            else if(e.getCause().getClass().equals(InvalidArgument.class)){
                throw (InvalidArgument)e.getCause();
            }
            else{
                throw new InvalidArgument(e.getMessage());
            }
        }

        System.out.println("[Interpreter] Instruction executed\n");
    }

    /**
    * Run all instructions
    */
    public void runAllInstructions(long millis) throws SyntaxError,InvalidArgument,InterruptedException{
        int run = 1;
        while(run != 0){
            run = this.runNextInstruction();
            if(run == 1){
                Thread.sleep(millis);
            }
        }
    }
    
    public String toString(){
        return String.format("[Interpreter] CONFIGURATION: parsedIntruction.size = %d", this.parsedIntruction.size());
    }
}