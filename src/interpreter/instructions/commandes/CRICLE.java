package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;

import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;

import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;

/**
 * Draw a cricle from the cursor on the screen.
 */
public class CRICLE extends Command{
    
    public static void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument{

        if(args.size() != 1){
            throw new SyntaxError("Need 1 argument");
        }

        double finalValue = 0;

        if(args.get(0) instanceof VariableNumber){ 
            finalValue = ((VariableNumber)args.get(0)).getValue(); 
        }
        else{
            throw new InvalidArgument("Argument must be 1 positive number [Integer/Double]");
        }

        // after getting the finalValue correctly

        if(finalValue < 0){
            throw new InvalidArgument("Argument must be 1 positive number [Integer/Double]");
        }

        tab.drawCricle(finalValue);
    }
}
