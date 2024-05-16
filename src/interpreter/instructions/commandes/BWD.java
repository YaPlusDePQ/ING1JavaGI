package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;
import fx.DrawingCursor;

import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;
import interpreter.variables.VariableString;

import interpreter.Parser;

import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;

public class BWD extends Command{

    public static void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument{

        //check minimum number of argument required for the command
        if(args.size() != 1){
            throw new SyntaxError("Need 1 argument");
        }


        double finalValue = 0;

        if(args.get(0) instanceof VariableNumber){ //if the argument is a integer get the value
            finalValue = ((VariableNumber)args.get(0)).getValue(); //because getValue() return an object (No direct type) we need to cast it to an Integer to use it
        }
        else if(args.get(0) instanceof VariableString){ // if its a string

            if( ((VariableString)args.get(0)).getValue().matches("([0-9]*\\.?[0-9]*) *%")){ // check if its a %, if not throw an error

                finalValue = Parser.percentageToDouble(((VariableString)args.get(0)).getValue()); //convert the value from a string to a number

                finalValue = tab.getWidth() > tab.getHeight() ? (finalValue)*tab.getWidth() : (finalValue)* tab.getHeight(); // it as to be a pourcentage of the biggest value between widht and height
            }
            else{
                throw new InvalidArgument("Argument must be 1 pourcentage [String] or 1 number [Integer/Double]");
            }
        }
        else{
            throw new InvalidArgument("Argument must be 1 pourcentage [String] or 1 number [Integer/Double]");
        } 

        // after getting the finalValue correctly

        //getting all the cursors
        List<DrawingCursor> cursors = tab.getAllDrawingCursor();

        //move each active cursors by the value
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).move(-finalValue);
            }
        }

        tab.drawLine();
    }
}
