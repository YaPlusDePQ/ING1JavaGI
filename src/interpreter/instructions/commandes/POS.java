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

public class POS extends Command{

    public static void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument{

        //check minimum number of argument required for the command
        if(args.size() != 2){
                        throw new SyntaxError("Need 2 arguments");

        }

        double valueX = 0;
        double valueY = 0;

        if(args.get(0) instanceof VariableNumber && args.get(1) instanceof VariableNumber){ //if the argument is an integer get the value
            valueX = ((VariableNumber)args.get(0)).getValue(); //because getValue() return an object (No direct type) we need to cast it to an Integer to use it
        }
        else if(args.get(0) instanceof VariableString){ // if it's a string

            if( !((VariableString)args.get(0)).getValue().matches("([0-9]*\\.?[0-9]*) *%") ){ // check if its a %, if not throw an error
                throw new InvalidArgument("Arguments must be pourcentages [String] or numbers [Integer/Double]");
            }

            valueX = Parser.percentageToDouble((String)args.get(0).getValue()); //convert the value from a string to a number
            valueX = tab.getWidth() > tab.getHeight() ? (valueX)*tab.getWidth() : (valueX)* tab.getHeight(); // it as to be a pourcentage of the biggest value between widht and height
        
        }
        else{
            throw new InvalidArgument("Arguments must be pourcentages [String] or numbers [Integer/Double]");
        }
        
        if(args.get(1) instanceof VariableNumber){ //if the argument is an integer get the value
            valueY = ((VariableNumber)args.get(1)).getValue(); //because getValue() return an object (No direct type) we need to cast it to an Integer to use it
        }
        else if(args.get(1) instanceof VariableString){ // if it's a string

            if( !((VariableString)args.get(1)).getValue().matches("([0-9]*\\.?[0-9]*) *%") ){ // check if its a %, if not throw an error
                throw new InvalidArgument("Arguments must be pourcentages [String] or numbers [Integer/Double]");
            }

            valueY = Parser.percentageToDouble((String)args.get(1).getValue()); //convert the value from a string to a number
            valueY = tab.getWidth() > tab.getHeight() ? (valueY)*tab.getWidth() : (valueY)* tab.getHeight(); // it as to be a pourcentage of the biggest value between widht and height

        }
        else{
            throw new InvalidArgument("Arguments must be pourcentages [String] or numbers [Integer/Double]");
        }

        // after getting the values correctly

        //getting all the cursors
        List<DrawingCursor> cursors = tab.getAllDrawingCursor();

        //move each active cursors by the value
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).setXY(valueX, valueY);
            }
        }

    }
}
