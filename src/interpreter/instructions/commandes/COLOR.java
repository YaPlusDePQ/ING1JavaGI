package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;
import fx.DrawingCursor;
import javafx.scene.paint.Color;

import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;
import interpreter.variables.VariableString;

public class COLOR extends Command{
    
    public static void execute(DrawingTab tab, List<Variable> args) throws IncorrectArgument{
                
        double valueRed = 0;
        double valueGreen = 0;
        double valueBlue = 0;
        
        List<DrawingCursor> cursors = tab.getAllDrawingCursor();
        
        //For the case of x,x,x as arguments (RGB)
        if(args.size() == 3){
            
            //check type
            if( !(args.get(0) instanceof VariableNumber) || !(args.get(1) instanceof VariableNumber) || !(args.get(2) instanceof VariableNumber)){
                throw new IncorrectArgument("parametre(s) incorrect(s)");
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
                throw new IncorrectArgument("parametre(s) incorrect(s)");
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
                throw new IncorrectArgument("parametre(s) incorrect(s)");
            }

            //checking if the value are correct IE: being a hex
            if( !( (VariableString)args.get(0) ).getValue().matches("#([a-fA-F0-9]{6})") ){
                throw new IncorrectArgument("parametre(s) incorrect(s)");
            }

            //apply the change
            for(int i=0; i<cursors.size(); i++){
                if(cursors.get(i).isActive()){
                    cursors.get(i).setColor(Color.web( ((VariableString)args.get(0)).getValue() ));
                }
            }

        }
        else{
            throw new IncorrectArgument("parametre(s) incorrect(s)");
        }

    }
}
