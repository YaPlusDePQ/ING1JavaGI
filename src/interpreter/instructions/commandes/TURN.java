package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingCursor;
import fx.DrawingTab;

import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;

import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;

/**
 * rotates the cursor relatively expressed in degrees.
 */
public class TURN implements Command{
    
    public void execute(DrawingTab tab, List<Variable> args)  throws SyntaxError,InvalidArgument{

        if(args.size() != 1){
            throw new SyntaxError("Need 1 argument");
        }
        
        double finalValue = 0;
        
        if(args.get(0) instanceof VariableNumber){ 
            finalValue = ((VariableNumber)args.get(0)).getValue(); 
        }
        else { 
            throw new InvalidArgument("Argument must be 1 number [Integer/Double]");
        }  
        

        List<DrawingCursor> cursors = tab.getAllDrawingCursor();
        
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).turn(finalValue);
            }
        }
        
    }
} 
