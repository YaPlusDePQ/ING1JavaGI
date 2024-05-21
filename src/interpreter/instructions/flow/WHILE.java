package interpreter.instructions.flow;

import java.util.List;

import fx.DrawingTab;
import interpreter.Interpreter;
import interpreter.Parser;
import interpreter.variables.Variable;

import interpreter.Exceptions.SyntaxError;
import interpreter.Exceptions.InvalidArgument;

public class WHILE implements Flow{
    public Interpreter execute(DrawingTab parent, String argument, List<String> instructions, List<Variable> definedVariables) throws SyntaxError,InvalidArgument{

        try{
            if(Parser.eval(argument, definedVariables) == 0){
                return null;
            }
        }
        catch(NumberFormatException e){
            throw new SyntaxError(e.getMessage());
        }
        

        Interpreter subFlow = new Interpreter(parent, "", definedVariables);
        subFlow.setIntruction(instructions);
        
        subFlow.onEndOfSCript = (self) -> {
            if(Parser.eval(argument, self.getVariables()) != 0) {
                self.setIndex(0);
                return Interpreter.PROCESSE_DONE;
            }
            else{
                return Interpreter.END_OF_SCRIPT;
            }
        };

        return subFlow;
    }
}
