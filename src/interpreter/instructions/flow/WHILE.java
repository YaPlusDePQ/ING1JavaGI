package interpreter.instructions.flow;

import java.util.List;

import fx.DrawingTab;
import interpreter.Interpreter;
import interpreter.Parser;
import interpreter.variables.Variable;

import interpreter.Exceptions.SyntaxError;
import interpreter.Exceptions.InvalidArgument;

/**
 * WHILE condition: executes the statement block as long as the condition boolean is true.
 */
public class WHILE implements Flow{
    public Interpreter execute(DrawingTab parent, String argument, List<String> instructions, List<Variable> definedVariables) throws SyntaxError,InvalidArgument{

        try{
            //compute the condition
            if(Parser.eval(argument, definedVariables) == 0){
                //if the condition is false do nothing aka return null
                return null;
            }
        }
        catch(NumberFormatException e){
            throw new SyntaxError(e.getMessage());
        }
        
        //configure interpreter
        Interpreter subFlow = new Interpreter(parent, "", definedVariables);
        subFlow.setIntruction(instructions);
        
        //set function to run at the end of the block
        subFlow.onEndOfSCript = (self) -> {
            if(Parser.eval(argument, self.getVariables()) != 0) { //If the condition is true aka != 0
                self.setIndex(0); //set the index at the start of the block
                return Interpreter.PROCESSE_DONE;
            }
            else{
                return Interpreter.END_OF_SCRIPT;
            }
        };

        return subFlow;
    }
}
