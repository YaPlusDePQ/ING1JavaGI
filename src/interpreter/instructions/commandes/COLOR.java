package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;
import fx.DrawingCursor;
import javafx.scene.paint.Color;

import interpreter.variables.Variable;
import interpreter.variables.VariableInt;
import interpreter.variables.VariableString;
import variables.VariableNumber;

public class COLOR extends command{

    public static void execute(DrawingTab tab, List<Variable> args) throws incorrectArgument{

        //check minimum number of argument required for the command
        if(args.size() != 3){
            throw new incorrectArgument("parametre(s) incorrect(s)");
        }

        double valueRed = 0;
        double valueGreen = 0;
        double valueBlue = 0;

        if(args.get(0) instanceof VariableNumber){ //if the argument is a integer get the value
            valueRed = (Double)(args.get(0).getValue()); //because getValue() return an object (No direct type) we need to cast it to an Integer to use it
        }
        else if(args.get(0) instanceof VariableString){ // if its a string

            if( ((String)args.get(0).getValue()).matches("([0-2]*\\.?[0-5]*\\.?[0-5]*)")){ // check if its a number between 0 and 255 if not throw an error
                valueRed = (int)(args.get(0).getValue());
            }
            else{
                throw new incorrectArgument("parametre(s) incorrect(s)");
            }
        }
        if(args.get(1) instanceof VariableNumber){ //if the argument is a integer get the value
            valueGreen = (Double)(args.get(1).getValue()); //because getValue() return an object (No direct type) we need to cast it to an Integer to use it
        }
        else if(args.get(1) instanceof VariableString){ // if its a string

            if( ((String)args.get(1).getValue()).matches("([0-2]*\\.?[0-5]*\\.?[0-5]*)")){ // check if its a number between 0 and 255 if not throw an error
                valueGreen = (int)(args.get(1).getValue());
            }
            else{
                throw new incorrectArgument("parametre(s) incorrect(s)");
            }
        }
        if(args.get(2) instanceof VariableNumber){ //if the argument is a integer get the value
            valueBlue = (Double)(args.get(2).getValue()); //because getValue() return an object (No direct type) we need to cast it to an Integer to use it
        }
        else if(args.get(2) instanceof VariableString){ // if its a string

            if( ((String)args.get(2).getValue()).matches("([0-2]*\\.?[0-5]*\\.?[0-5]*)")){ // check if its a number between 0 and 255 if not throw an error
                valueBlue = (int)(args.get(2).getValue());
            }
            else{
                throw new incorrectArgument("parametre(s) incorrect(s)");
            }
        }

        // after getting the values correctly

        //getting all the cursors
        List<DrawingCursor> cursors = tab.getAllDrawingCursor();

        //move each active cursors by the value
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).setColor(valueRed, valueGreen, valueBlue);
            }
        }

    }
}
