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

        if(Parser.eval(argument, definedVariables) == 0){
            return null;
        }

        Interpreter subFlow = new Interpreter(parent, "", definedVariables);
        subFlow.setIntruction(instructions);
        
        return subFlow;
    }
}
