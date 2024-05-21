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
* indicates how much pressure the cursor draws the shape with.
*  A real value between 0 and 1 is required except with the % sign then a
*  real value between 0 and 100 is requested. For the value 0% there is nothing
* drawn, 100% the color is opaque. Any value in between draws the
* form with transparency proportional to the value.
*/
public class PRESS implements Command{
    
    /**
    * execute the command
    *
    * @param  tab DrawingTab object to execute the command in
    * @param  args list of arguments send to the command
    */
    public void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument{
        
        if(args.size() != 1){
            throw new SyntaxError("Need 1 argument");
        }
        
        List<DrawingCursor> cursors = tab.getAllDrawingCursor();
        double finalValue = 0;
        
        if(args.get(0) instanceof VariableNumber){ 
            finalValue = ((VariableNumber)args.get(0)).getValue(); 
        }
        else if(args.get(0) instanceof VariableString){ 
            
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
        
        //apply the change
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).setOpacity(finalValue);
            }
        }
        
    }
}
