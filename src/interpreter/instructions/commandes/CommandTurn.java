package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingCursor;
import fx.DrawingTab;
import interpreter.variables.VariableNumber;
import variables.Variable;
import variables.VariableString;

public class CommandTurn implements command{
       
     public static void execute(DrawingTab tab, List<Variable> args) throws incorrectArgument{
    //check minimum number of argument required for the command
        if(args.size() != 1){
            throw new incorrectArgument("parametre(s) incorrect(s)");
        }

        double finalValue = 0;

        if(args.get(0) instanceof VariableNumber){ //if the argument is a integer get the value
            finalValue = (Double)(args.get(0).getValue()); //because getValue() return an object (No direct type) we need to cast it to an Integer to use it
        }
        else { // if its not an integer
            throw new incorrectArgument("parametre(s) incorrect(s)");
            }
        }  

        // after getting the finalValue correctly

        //getting all the cursors
        List<DrawingCursor> cursors = tab.getAllDrawingCursor();

        //move each active cursors by the value
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).setDirection(finalValue);
            }
        }

    }
} 
