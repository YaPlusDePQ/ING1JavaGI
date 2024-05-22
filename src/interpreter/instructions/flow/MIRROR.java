package interpreter.instructions.flow;

import java.util.List;
import java.util.ArrayList;

import fx.DrawingCursor;
import fx.DrawingTab;
import interpreter.Interpreter;
import interpreter.Parser;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;
import interpreter.variables.VariableString;

public class MIRROR implements Flow{
        public Interpreter execute(DrawingTab parent, String argument, List<String> instructions, List<Variable> definedVariables) throws SyntaxError,InvalidArgument{

        List<Variable> finalArguments = Parser.getValueFromArgument(argument, definedVariables);
        List<DrawingCursor> duplicatedCursors =  new ArrayList<DrawingCursor>();
        double[] coords = {0,0,0,0};
        
        if(finalArguments.size() != 2 && finalArguments.size() != 4){
            throw new SyntaxError("Need 2 or 4 arguments");
        }

        int i=0;
        for(Variable arg : finalArguments){
            if( arg instanceof VariableString){
                if( !((VariableString)args.get(1)).getValue().matches("([0-9]*\\.?[0-9]*) *%") ){ // check if its a %, if not throw an error
                    throw new InvalidArgument("Arguments must be pourcentages [String] or numbers [Integer/Double]");
                }
                
                coords[i] = Parser.percentageToDouble((String)args.get(1).getValue()); //convert the value from a string to a number
                coords[i] = tab.getWidth() > tab.getHeight() ? (coords[i])*tab.getWidth() : (coords[i])* tab.getHeight(); // it as to be a pourcentage of the biggest value between widht and height
                
            }
            else if(arg instanceof VariableNumber) {
                coords[i] = ((VariableNumber)arg ) .getValue().doubleValue();
            }
            else{
                throw new InvalidArgument("Arguments must be pourcentages [String] or numbers [Integer/Double]");
            }
            i++;
        }
        

        if(finalArguments.size() == 2){
            
        }
        else if(finalArguments.size() == 4){
            
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
