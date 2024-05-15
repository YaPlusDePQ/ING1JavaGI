package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;
import fx.DrawingCursor;

import interpreter.Parser;
import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;
import interpreter.variables.VariableString;

public class CommandMov extends command{
  
    public static void execute(DrawingTab tab, List<Variable> args) throws incorrectArgument{

        //check minimum number of argument required for the command
        if(args.size() != 2){
            throw new incorrectArgument("parametre(s) incorrect(s)");
        }

        double valueX = 0;
        double valueY = 0;

        if(args.get(0) instanceof VariableNumber){ //if the argument is an integer get the value
            valueX = (Double)(args.get(0).getValue()); //because getValue() return an object (No direct type) we need to cast it to an Integer to use it
        }
        else if(args.get(0) instanceof VariableString){ // if it's a string

            if( ((String)args.get(0).getValue()).matches("([0-9]*\\.?[0-9]*) *%")){ // check if it's a %, if not throw an error

                valueX = Parser.percentageToDouble((String)args.get(0).getValue()); //convert the value from a string to a number !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                valueX = (valueX)* tab.getWidth(); //convert the value from % to pixel
            }
            else{
                throw new incorrectArgument("parametre(s) incorrect(s)");
            }
        }
        
        if(args.get(1) instanceof VariableNumber){ //if the argument is an integer get the value
            valueY = (Double)(args.get(1).getValue()); //because getValue() return an object (No direct type) we need to cast it to an Integer to use it
        }
        else if(args.get(1) instanceof VariableString){ // if it's a string

            if( ((String)args.get(1).getValue()).matches("([0-9]*\\.?[0-9]*) *%")){ // check if it's a %, if not throw an error

                valueY = Parser.percentageToDouble((String)args.get(1).getValue()); //convert the value from a string to a number !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                valueY = (valueY)* tab.getHeight(); //convert the value from % to pixel 
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
                cursors.get(i).setXY(valueX, valueY);
                tab.drawLine();
            }
        }
    }
}
