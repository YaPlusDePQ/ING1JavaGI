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

public class PRESS extends Command{

    public static void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument{

        //check minimum number of argument required for the command
        if(args.size() != 1){
            throw new SyntaxError("Need 1 argument");
        }

        List<DrawingCursor> cursors = tab.getAllDrawingCursor();
        double finalValue = 0;

        if(args.get(0) instanceof VariableNumber){ //if the argument is a integer get the value
            finalValue = ((VariableNumber)args.get(0)).getValue(); //because getValue() return an object (No direct type) we need to cast it to an Integer to use it
        }
        else if(args.get(0) instanceof VariableString){ // if its a string

            if( !((VariableString)args.get(0)).getValue().matches("([0-9]*\\.?[0-9]*) *%")){ // check if its a %, if not throw an error
                throw new InvalidArgument("Argument must be 1 pourcentage [String] or 1 number [Integer/Double]");
            }

            finalValue = Parser.percentageToDouble(((VariableString)args.get(0)).getValue()); //convert the value from a string to a number
        }
        else{
            throw new InvalidArgument("Argument must be 1 pourcentage [String] or 1 number [Integer/Double]");
        }  

        // after getting the finalValue correctly
        if(finalValue > 1 || finalValue < 0){
            throw new InvalidArgument("Argument must be 1 pourcentage [String] or 1 number [Integer/Double]");
        }

        //move each active cursors by the value
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).setOpacity(finalValue);
            }
        }

    }
}
