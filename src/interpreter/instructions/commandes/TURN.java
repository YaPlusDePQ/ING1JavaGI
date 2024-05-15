package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingCursor;
import fx.DrawingTab;
import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;


public class TURN extends Command{
       
     public static void execute(DrawingTab tab, List<Variable> args) throws IncorrectArgument{
    //check minimum number of argument required for the command
        if(args.size() != 1){
            throw new IncorrectArgument("parametre(s) incorrect(s)");
        }

        double finalValue = 0;

        if(args.get(0) instanceof VariableNumber){ //if the argument is a integer get the value
            finalValue = ((VariableNumber)args.get(0)).getValue(); //because getValue() return an object (No direct type) we need to cast it to an Integer to use it
        }
        else { // if its not an integer
            throw new IncorrectArgument("parametre(s) incorrect(s)");
        }  

        //getting all the cursors
        List<DrawingCursor> cursors = tab.getAllDrawingCursor();

        //move each active cursors by the value
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).setDirection(cursors.get(i).getDirection()+finalValue);
            }
        }

    }
} 
