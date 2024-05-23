package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;

/**
 * selects an existing cursor.
 */
public class SELECT implements Command{

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

        tab.setActiveCursor(finalValue);

    }
}
