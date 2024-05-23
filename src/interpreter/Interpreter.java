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
    /**
    * Constant for return result possible after an instruction as been executed
    * <p>
    * Indicated that the interpreter is at the end of the script (no instruction left)
    */
    public final static int END_OF_SCRIPT = 0;
    /**
    * Constant for return result possible after an instruction as been executed
    * <p>
    * An instruction with change on the drawing canvas as been done
    */
    public final static int INSTRUCTION_EXECUTED = 1;
    /**
    * Constant for return result possible after an instruction as been executed
    * <p>
    * indicated that the instruction is empty or doesnt look like one and it as been skiped
    */
    public final static int INSTRUCTION_SKIPED = 2;
    /**
    * Constant for return result possible after an instruction as been executed
    * <p>
    * Indicated that an instrcution with no change on the drawing canvas as been done
    */
    public final static int PROCESSE_DONE = 2;
    
    private DrawingTab parentTab; //DrawingTab where the Interpreter will execute on
    private List<String> parsedIntruction; //List of all the instructions
    private int index; //current index in parsedIntruction
    private List<Variable> variables; //list of all the variables declared
    
    //path to possible type of instruction
    private final String PATH_TO_COMMAND = "interpreter.instructions.commandes.";
    private final String PATH_TO_FLOW = "interpreter.instructions.flow.";
    private final String PATH_TO_MEMORY = "interpreter.instructions.memory.";
    
    private Interpreter subFlow = null; //Store a interpretor that will execute code from a sub local space (loop, if, ect..)
    
    /**
    * Store a Function that will be executed when the interpreter as no instructions left
    */
    public Function <Interpreter,Integer> onEndOfSCript = null;
    
    /**
    * Constructor
    *
    * @param  parentTab  DrawingTab where the Interpreter will execute on
    * @param  instructions  String of all the instruction
    */
    public Interpreter(DrawingTab parentTab, String instructions){
        this.parentTab = parentTab;
        this.parsedIntruction = Parser.getInstruction(instructions);
        this.index = 0;
        this.variables = new ArrayList<Variable>();
        this.subFlow = null;
        System.out.println(this);
    }
    
    /**
    * Constructor
    *
    * @param  parentTab  DrawingTab where the Interpreter will execute on
    * @param  instructions  String of all the instruction
    * @param  defaultDefinedVariables  List of already defined variables
    */
    public Interpreter(DrawingTab parentTab, String instructions, List<Variable> defaultDefinedVariables){
        this.parentTab = parentTab;
        this.parsedIntruction = Parser.getInstruction(instructions);
        this.index = 0;
        this.variables = new ArrayList<Variable>(defaultDefinedVariables);
        this.subFlow = null;
    }
    
    /**
    * Get the index at wich the interpretor is currently at
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
    * Get if the interpreter as no instruction left.
    *
    * @return true if yes
    */
    public boolean isFinished(){
        return this.index >= this.parsedIntruction.size();
    }
    
    
    /**
    * Set the index at wich the interpretor is currently at
    *
    */
    public void setIndex(int newIndex){
        this.index = newIndex;
    }
    
    /**
     * Set the instruction that need to be
     * @param instructions String of all the instruction
     * @throws SyntaxError if { } count doesnt match
     */
    public void setIntruction(String instructions) throws SyntaxError{
        if(!Parser.isValide(instructions)){
            throw new SyntaxError("Opening Bracket doesnt match closing one.");
        }
        this.index = 0;
        this.parsedIntruction = Parser.getInstruction(instructions);
    }
    
    /**
     * Set the instruction that need to be
     * @param parsedInstructions List of already parsed instruction, aka list of instruction line per line
     * @throws SyntaxError if { } count doesnt match
     */
    public void setIntruction(List<String> parsedInstructions) throws SyntaxError {
        if(!Parser.isValide(parsedInstructions)){
            throw new SyntaxError("Opening Bracket doesnt match closing one.");
        }
        this.index = 0;
        this.parsedIntruction = parsedInstructions;
    }
    
    
    /**
    * Set an interpreter as if no instruction as ever been executed
    */
    public void reset(){
        this.index = 0;
        this.variables.clear();
        this.subFlow = null;
        this.parentTab.getAllDrawingCursor().clear();
        this.parentTab.addCursor(0);
        this.parentTab.setActiveCursor(0);
    }
    
    
    /**
     * Execute the next instruction in List
     * @return INSTRUCTION_EXECUTED, END_OF_SCRIPT, PROCESSE_DONE or INSTRUCTION_SKIPED
     * @throws SyntaxError
     * @throws InvalidArgument
     */
    public int runNextInstruction() throws SyntaxError, InvalidArgument{
        
        //test if it currently running a flow
        if(this.subFlow != null){
            System.out.println("[Interpreter] Running subFlow ...");
            int resultCode = this.subFlow.runNextInstruction();

            if(resultCode != END_OF_SCRIPT){ 
                System.out.println("[Interpreter] Running done");
                return resultCode;
            }
            else{
                //if flow as finished, kill it and directly run the next instruction after the block
                System.out.println("[Interpreter] subFlow Finished. Going back on parent flow");
                this.subFlow = null;
            }
        }
        
        //test if finished
        if(this.index >= this.parsedIntruction.size()){

            if(this.onEndOfSCript != null){
                //execute the script if it existe
                return onEndOfSCript.apply(this);
            }
            else{
                System.out.println("[Interpreter] No instruction remaining");
                return END_OF_SCRIPT;
            }
        }
        
        // skip non instruction like
        if(!this.parsedIntruction.get(index).matches(" *[A-Z]+.*")){
            this.index++;
            return INSTRUCTION_SKIPED;
        }
        
        String currentInstruction = Parser.cleanUpInstruction(this.parsedIntruction.get(index)); //clean up unnecessary space
        System.out.println(String.format("[Interpreter] Instruction: '%s'", currentInstruction));
        
        // get the Insctruction name and the argumetns separeted
        List<String> currentInstructionParsed = Arrays.asList(currentInstruction.split(" ", 2)); 
        String name = currentInstructionParsed.get(0);
        String arguments =currentInstructionParsed.get(1);
        
        System.out.println(String.format("[Interpreter] Instruction result (pre-traitement): NAME: '%s', ARG: '%s'", name, arguments));
        
        //Identification of instruction types
        try{
            //in case of a command
            Class<?> cls = Class.forName(PATH_TO_COMMAND+name); //get the class if it existe
            System.out.println("[Interpreter] instruction identified as "+cls.getName());
            this.runCommand(cls, arguments); 
        }
        catch(ClassNotFoundException e0){
            try{
                //in case of a flow
                Class<?> cls = Class.forName(PATH_TO_FLOW+name);
                System.out.println("[Interpreter] instruction identified as "+cls.getName());
                this.runFlow(cls, arguments);
                return PROCESSE_DONE;
            }
            catch(ClassNotFoundException e1){
                try{
                    //in case of a memory management
                    Class<?> cls = Class.forName(PATH_TO_MEMORY+name);
                    System.out.println("[Interpreter] instruction identified as "+cls.getName());
                    this.runMemory(cls, arguments);
                }
                catch(ClassNotFoundException e){
                    throw new SyntaxError("Instruction doesnt exist");
                }
            }
        }
        
        this.index++;
        return INSTRUCTION_EXECUTED;
    }
    
    
    /**
     * Run a command
     * @param commandClass Class of the Command
     * @param arguments arguments of the instruction
     * @throws SyntaxError
     * @throws InvalidArgument
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
     * run a flow
     * @param flowClass Class of the flow
     * @param arguments arguments of the instruction
     * @throws SyntaxError
     * @throws InvalidArgument
     */
    public void runFlow(Class<?> flowClass, String arguments) throws SyntaxError,InvalidArgument{
        
        //check for syntaxe
        if(!arguments.matches("[^\\{]*\\{")){
            throw new SyntaxError("Missing { at the opening of the block or incorrect syntax");
        }
        
        //remove opening { from the argument
        arguments = arguments.substring(0, arguments.length()-1);
        
        List<String> flowInstructions = Parser.getFlowBlock(this.parsedIntruction, index); //get the list of instruction inside the block
        index += flowInstructions.size(); //set current index to the next instruction after thr block
        flowInstructions.remove(0); //remove flow delcaration from the instruction block
        
        try{
            //get the methode execute from the flow sub class
            Method m = flowClass.getMethod("execute",DrawingTab.class, String.class, List.class, List.class );
            //run
            this.subFlow = (Interpreter)m.invoke(flowClass.newInstance(), this.parentTab, arguments, flowInstructions, this.variables); 
        }
        catch(NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InstantiationException e){
            //unknow error while accessing/trying to running the methode
            throw new SyntaxError("Flow exist but cannot be access");
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
     * run memory management
     * @param allocationClass Class of the memory management
     * @param arguments arguments of the instruction
     * @throws SyntaxError
     * @throws InvalidArgument
     */
    public void runMemory(Class<?> allocationClass, String arguments) throws SyntaxError,InvalidArgument{
        String name = "";
        String values = "";

        //get the value if their is one
        if(arguments.matches("[^=]*=.*")){
            name = arguments.split("=")[0];
            values = arguments.split("=")[1];
        }
        else{
            name = arguments;
        }
        
        try{
            //get the methode execute from the memory sub class
            Method m = allocationClass.getMethod("execute",List.class, String.class, String.class );
            //run
            m.invoke(allocationClass.newInstance(), this.variables, name, values); 
        }
        catch(NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InstantiationException e){
            //unknow error while accessing/trying to running the methode
            throw new SyntaxError("Aloocation exist but cannot be access");
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
     * Run all the instruction and wait a defined time between eahc visible change on the screen
     * @param millis
     * @throws SyntaxError
     * @throws InvalidArgument
     * @throws InterruptedException Thread sleep error
     */
    public void runAllInstructions(long millis) throws SyntaxError,InvalidArgument,InterruptedException{

        //check for parsedIntruction integrity before running
        if(!Parser.isValide(this.parsedIntruction)){
            throw new SyntaxError("Opening Bracket doesnt match closing one.");
        }
        
        int run = END_OF_SCRIPT;
        do{
            run = this.runNextInstruction();
            if(run == INSTRUCTION_EXECUTED){
                //wait only if a visible instrcution as been executed
                Thread.sleep(millis);
            }
        }while(run != 0);
    }
    
    public String toString(){
        return String.format("[Interpreter] CONFIGURATION: parsedIntruction.size = %d", this.parsedIntruction.size());
    }
}