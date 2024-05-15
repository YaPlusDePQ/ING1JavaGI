package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;
import fx.DrawingCursor;

import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;

public class THICK extends Command{

    public static void execute(DrawingTab tab, List<Variable> args) throws IncorrectArgument{

        //check minimum number of argument required for the command
        if(args.size() != 1){
            throw new IncorrectArgument("parametre(s) incorrect(s)");
        }

        List<DrawingCursor> cursors = tab.getAllDrawingCursor();
        double finalValue = 0;

        if(args.get(0) instanceof VariableNumber){ //if the argument is a integer get the value
            finalValue = ((VariableNumber)args.get(0)).getValue(); //because getValue() return an object (No direct type) we need to cast it to an Integer to use it
        }
        // after getting the finalValue correctly

        if(finalValue < 0){
            throw new IncorrectArgument("parametre(s) incorrect(s)");
        }

        //move each active cursors by the value
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).setThickness(finalValue);
            }
        }

    }
}
