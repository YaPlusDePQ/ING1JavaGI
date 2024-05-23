package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;

/**
 * deletes an existing cursor. If theyre is only One cursor throw an error.
 */
public class REMOVE implements Command{

    public void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument{

        if(args.size() != 1){
            throw new SyntaxError("Need 1 argument");
        }

        int finalValue = 0;

        if(args.get(0) instanceof VariableNumber){ 
            finalValue = ((VariableNumber)args.get(0)).getValueInt(); 
        }
        else{
            throw new InvalidArgument("Argument must be 1 positive number [Integer]");
        }

        if(finalValue < 0){
            throw new InvalidArgument("Argument must be 1 positive number [Integer]");
        }
        
        if(tab.getDrawingCursor(finalValue) == null){
            throw new InvalidArgument("Cursor '"+finalValue+"' does not exist");
        }

        if(tab.getAllDrawingCursor().size() <= 1){
            throw new InvalidArgument("Impossible to remove the only cursor");
        }

        tab.removeCursor(finalValue);

    }
}
