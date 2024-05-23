package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;
import fx.DrawingCursor;

import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;

import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;


/**
 * allows you to define the thickness of a line before moving the cursor.
 */
public class THICK implements Command{

    public void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument{

        if(args.size() != 1){
            throw new SyntaxError("Need 1 argument");
        }

        List<DrawingCursor> cursors = tab.getAllDrawingCursor();
        double finalValue = 0;

        if(args.get(0) instanceof VariableNumber){ 
            finalValue = ((VariableNumber)args.get(0)).getValue(); 
        }
        else{
            throw new InvalidArgument("Argument must be 1 positive number [Integer/Double]");
        }



        if(finalValue < 0){
            throw new InvalidArgument("Argument must be 1 positive number [Integer/Double]");
        }

        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).setThickness(finalValue);
            }
        }

    }
}
