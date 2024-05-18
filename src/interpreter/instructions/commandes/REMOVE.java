package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;

public class REMOVE {
    /**
    * execute the command
    *
    * @param  tab DrawingTab object to execute the command in
    * @param  args list of arguments send to the command
    */
    public static void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument{

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

        // after getting the finalValue correctly

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
