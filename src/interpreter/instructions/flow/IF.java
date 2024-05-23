package interpreter.instructions.flow;

import java.util.List;

import fx.DrawingTab;
import interpreter.Interpreter;
import interpreter.Parser;
import interpreter.variables.Variable;

import interpreter.Exceptions.SyntaxError;
import interpreter.Exceptions.InvalidArgument;

/**
 * IF condition: executes the block of instructions if the boolean condition passed is true.
 */
public class IF implements Flow{
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
        
        //do nothing at the end
        subFlow.onEndOfSCript = null;

        return subFlow;
    }
}
