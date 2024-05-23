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

/**
* position the cursor on the screen (in pixels or %).
*/
public class POS implements Command{
    
    public void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument{
        
        if(args.size() != 2){
            throw new SyntaxError("Need 2 arguments");
            
        }
        
        double valueX = 0;
        double valueY = 0;
        
        // getting the data for valueX
        if(args.get(0) instanceof VariableNumber && args.get(1) instanceof VariableNumber){ 
            valueX = ((VariableNumber)args.get(0)).getValue();
        }
        else if(args.get(0) instanceof VariableString){ 
            
            if( !((VariableString)args.get(0)).getValue().matches("([0-9]*\\.?[0-9]*) *%") ){ // check if its a %, if not throw an error
                throw new InvalidArgument("Arguments must be pourcentages [String] or numbers [Integer/Double]");
            }
            
            valueX = Parser.percentageToDouble((String)args.get(0).getValue()); //convert the value from a string to a number
            valueX = tab.getWidth() > tab.getHeight() ? (valueX)*tab.getWidth() : (valueX)* tab.getHeight(); // it as to be a pourcentage of the biggest value between widht and height
            
        }
        else{
            throw new InvalidArgument("Arguments must be pourcentages [String] or numbers [Integer/Double]");
        }
        
        
        // getting the data for valueY
        if(args.get(1) instanceof VariableNumber){
            valueY = ((VariableNumber)args.get(1)).getValue();
        }
        else if(args.get(1) instanceof VariableString){ 
            
            if( !((VariableString)args.get(1)).getValue().matches("([0-9]*\\.?[0-9]*) *%") ){ // check if its a %, if not throw an error
                throw new InvalidArgument("Arguments must be pourcentages [String] or numbers [Integer/Double]");
            }
            
            valueY = Parser.percentageToDouble((String)args.get(1).getValue()); //convert the value from a string to a number
            valueY = tab.getWidth() > tab.getHeight() ? (valueY)*tab.getWidth() : (valueY)* tab.getHeight(); // it as to be a pourcentage of the biggest value between widht and height
            
        }
        else{
            throw new InvalidArgument("Arguments must be pourcentages [String] or numbers [Integer/Double]");
        }
        
        
        List<DrawingCursor> cursors = tab.getAllDrawingCursor();
        
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).setXY(valueX, valueY);
            }
        }
    }
}
