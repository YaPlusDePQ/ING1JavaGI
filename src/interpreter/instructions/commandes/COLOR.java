package interpreter.instructions.commandes;

import java.util.List;
import javafx.scene.paint.Color;

import fx.DrawingTab;
import fx.DrawingCursor;

import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;
import interpreter.variables.VariableString;

import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;

/**
 * determines the color of the next plot in web format with 6 hexadecimal characters.
 */
public class COLOR extends Command{
    
    /**
    * execute the command
    *
    * @param  tab DrawingTab object to execute the command in
    * @param  args list of arguments send to the command
    */
    public static void execute(DrawingTab tab, List<Variable> args) throws SyntaxError, InvalidArgument{
                
        double valueRed = 0;
        double valueGreen = 0;
        double valueBlue = 0;
        
        List<DrawingCursor> cursors = tab.getAllDrawingCursor();
        
        //For the case of x,x,x as arguments (RGB)
        if(args.size() == 3){
            
            //check type
            if( !(args.get(0) instanceof VariableNumber) || !(args.get(1) instanceof VariableNumber) || !(args.get(2) instanceof VariableNumber)){
                throw new InvalidArgument("Argument must be 3 numbers between 0 and 1 [Integer/Value] or hexadecimale code (6 characters) [String]");
            }


            //getting values
            valueRed = ((VariableNumber)(args.get(0))).getValue(); 
            valueRed = valueRed*255;

            valueGreen = ((VariableNumber)(args.get(1))).getValue();
            valueGreen = valueGreen*255;

            valueBlue = ((VariableNumber)(args.get(2))).getValue();
            valueBlue = valueBlue*255;

            //checking if the value are correct IE: RGB goes from 0- to 255
            if(valueRed > 255 || valueRed < 0 || valueGreen > 255 || valueGreen < 0 || valueBlue > 255 || valueBlue < 0){
                throw new InvalidArgument("Argument must be 3 numbers between 0 and 1 [Integer/Value] or hexadecimal code (6 characters) [String]");
            }
            
            //apply the change
            for(int i=0; i<cursors.size(); i++){
                if(cursors.get(i).isActive()){
                    cursors.get(i).setColor(Color.rgb((int)valueRed, (int)valueGreen, (int)valueBlue));
                }
            }

        }
        else if(args.size() == 1){ //For the case of #xxxxxx  as argument (HEX)
            
            //check type
            if( !(args.get(0) instanceof VariableString) ){
                throw new InvalidArgument("Argument must be 3 numbers between 0 and 1 [Integer/Value] or 1 hexadecimal code (6 characters) [String]");
            }

            //checking if the value are correct IE: being a hex
            if( !( (VariableString)args.get(0) ).getValue().replaceAll(" *", "").matches("#([a-fA-F0-9]{6})") ){
                throw new InvalidArgument("Argument must be 3 numbers between 0 and 1 [Integer/Value] or 1 hexadecimal code (6 characters) [String]");
            }

            //apply the change
            for(int i=0; i<cursors.size(); i++){
                if(cursors.get(i).isActive()){
                    cursors.get(i).setColor(Color.web( ((VariableString)args.get(0)).getValue().replaceAll(" *", "") ));
                }
            }

        }
        else{
            throw new SyntaxError("Need 1 or 3 argument(s)");
        }

    }
}
