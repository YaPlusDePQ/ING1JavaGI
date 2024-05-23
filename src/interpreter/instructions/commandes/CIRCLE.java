package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;

import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;

import interpreter.variables.Variable;
import interpreter.variables.VariableBoolean;
import interpreter.variables.VariableNumber;

/**
 * Draw a cricle from the cursor on the screen.
 */
public class CIRCLE implements Command{
    
    public void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument{

        if(args.size() != 2){
            throw new SyntaxError("Need 2 arguments");
        }

        double radius = 0;
        boolean fill = false;

        if(args.get(0) instanceof VariableNumber){ 
            radius = ((VariableNumber)args.get(0)).getValue(); 
        }
        else{
            throw new InvalidArgument("Argument must be 1 positive number [Integer/Double] and 1 Boolean [Boolean]");
        }

        if(args.get(1) instanceof VariableBoolean){ 
            fill = ((VariableBoolean)args.get(1)).getValue(); 
        }
        else{
            throw new InvalidArgument("Argument must be 1 positive number [Integer/Double] and 1 Boolean [Boolean]");
        }


        if(radius < 0){
            throw new InvalidArgument("Argument must be 1 positive number [Integer/Double] and 1 Boolean [Boolean]");
        }

        tab.drawCricle(radius, fill);
    }
}
