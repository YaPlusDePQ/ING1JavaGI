package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;
import fx.DrawingCursor;

import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;

import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;


/**
 * permet de définir l’épaisseur d’un trait avant de déplacer le curseur.
 */
public class THICK extends Command{

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

        List<DrawingCursor> cursors = tab.getAllDrawingCursor();
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

        //apply change
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).setThickness(finalValue);
            }
        }

    }
}
