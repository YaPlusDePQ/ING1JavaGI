package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;
import fx.DrawingCursor;

import interpreter.Parser;

import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;
import interpreter.variables.VariableString;

import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;

/**
* advances the cursor relatively expressed in pixels
* or percentage of the largest dimension of the drawing area. In
* in both cases the value is a real value.
*/
public class FWD implements Command{
    
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
        
        double finalValue = 0;
        
        if(args.get(0) instanceof VariableNumber){ 
            finalValue = ((VariableNumber)args.get(0)).getValue(); 
        }
        else if(args.get(0) instanceof VariableString){ 
            
            if( ((VariableString)args.get(0)).getValue().matches("([0-9]*\\.?[0-9]*) *%")){ // check if its a %, if not throw an error
                
                finalValue = Parser.percentageToDouble((String)args.get(0).getValue()); //convert the value from a string to a number
                
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

        List<DrawingCursor> cursors = tab.getAllDrawingCursor();
        
        //move each active cursors by the value
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).move(finalValue);
            }
        }
        
        tab.drawLine();
    }
}
