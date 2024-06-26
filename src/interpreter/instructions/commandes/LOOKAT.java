package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingCursor;
import fx.DrawingTab;
import interpreter.Parser;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;
import interpreter.variables.VariableString;


/**
* the current cursor look at a cursor or a position 
*/
public class LOOKAT implements Command{
    
    public void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument{
        
        double valueX = 0;
        double valueY = 0;
        double tempX = 0;
        double tempY = 0;
        double modul = 0;
        double angle = 0;
        int cursorId = 0;
        
        List<DrawingCursor> cursors = tab.getAllDrawingCursor();
        
        //For the case of x, y
        if(args.size() == 2){
            
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
            
            if(args.get(1) instanceof VariableNumber){ 
                valueY = ((VariableNumber)args.get(1)).getValue();
            }
            else if(args.get(1) instanceof VariableString){ 
                
                if( !((VariableString)args.get(1)).getValue().matches("([0-9]*\\.?[0-9]*) *%") ){ // check if its a %, if not throw an error
                    throw new InvalidArgument("Arguments must be pourcentages [String] or numbers [Integer/Double]");
                }
                
                valueY = Parser.percentageToDouble((String)args.get(1).getValue()); //convert the value from a string to a number
                valueY = tab.getWidth() > tab.getHeight() ? (valueY)*tab.getWidth() : (valueY)* tab.getHeight(); // it has to be a pourcentage of the biggest value between widht and height
                
            }
            else{
                throw new InvalidArgument("Arguments must be pourcentages [String] or numbers [Integer/Double]");
            }
        }
        
        else if(args.size() == 1){
            
            //check type
            if (args.get(0) instanceof VariableNumber){
                //getting the value
                cursorId = ((VariableNumber)args.get(0)).getValueInt();  
                valueX = tab.getDrawingCursor(cursorId).getCurrentX();
                valueY = tab.getDrawingCursor(cursorId).getCurrentY();
            } 
            
            else{
                throw new InvalidArgument("Argument must be 1 number [Integer/Double]");
            }
            
            
        }        
        else {
            throw new SyntaxError("Need 1 or 2 argument");
            
        }
        
        //apply the change
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                tempX = valueX - cursors.get(i).getCurrentX(); //putting valueX in a new coordinate system with the current cursor as the origin
                tempY = valueY - cursors.get(i).getCurrentY(); //putting valueY in a new coordinate system with the current cursor as the origin
                modul = Math.sqrt(tempX * tempX + tempY * tempY);
                angle = Math.toDegrees(Math.atan2(tempX/modul, tempY/modul)); //it is better to use arctan in order to consider the sign of x and y as arccos and arcsin can't handle every values
                cursors.get(i).setDirection(Math.toRadians(angle));
            }
        }
    }
}