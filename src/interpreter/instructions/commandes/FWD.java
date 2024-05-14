package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;
import fx.DrawingCursor;

import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;
import interpreter.variables.VariableString;

public class FWD extends command{

    public static void execute(DrawingTab tab, List<Variable> args) throws incorrectArgument{

        //check minimum number of argument required for the command
        if(args.size() != 1){
            throw new incorrectArgument("parametre(s) incorrect(s)");
        }


        double finalValue = 0;

        if(args.get(0) instanceof VariableNumber){ //if the argument is a integer get the value
            finalValue = (Double)args.get(0).getValue(); //because getValue() return an object (No direct type) we need to cast it to an Integer to use it
        }
        else if(args.get(0) instanceof VariableString){ // if its a string

            if( ((String)args.get(0).getValue()).matches("([0-9]*\\.?[0-9]*) *%")){ // check if its a %, if not throw an error

                String pourcentageCleanedUp = ((String)args.get(0).getValue()).replace(" *", "").replace("%", ""); // keep only the number and not the %
                finalValue = Double.valueOf(pourcentageCleanedUp); //convert the value from a string to a number

                finalValue = tab.getWidth() > tab.getHeight() ? (finalValue/100)*tab.getWidth() : (finalValue/100)* tab.getHeight(); // its as to be a pourcentage of the biggest value between widht and height
            }
            else{
                throw new incorrectArgument("parametre(s) incorrect(s)");
            }
        }
        else{
            throw new incorrectArgument("parametre(s) incorrect(s)");
        }  

        // after getting the finalValue correctly

        //getting all the cursors
        List<DrawingCursor> cursors = tab.getAllDrawingCursor();

        //move each active cursors by the value
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).move(finalValue);
            }
        }

    };
}
