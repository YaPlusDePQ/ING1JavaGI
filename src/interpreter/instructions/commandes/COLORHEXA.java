package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingTab;
import fx.DrawingCursor;
import javafx.scene.paint.Color;

import interpreter.variables.Variable;
import interpreter.variables.VariableInt;
import interpreter.variables.VariableString;
import variables.VariableNumber;

public class COLORHEXA extends command{

    public static void execute(DrawingTab tab, List<Variable> args) throws incorrectArgument{

        //check minimum number of argument required for the command
        if(args.size() != 1){
            throw new incorrectArgument("parametre(s) incorrect(s)");
        }

        double valueHexa = 0;

        if( ((String)args.get(0).getValue()).matches("([0-F]*\\.?[0-F]*\\.?[0-F]*\\[0-F]*\\.?[0-F]*\\.?[0-F]*)")){ // check if its an adress type #rrggbb if not throw an error
            valueHexa = (int)(args.get(0).getValue());
        }
        else{
            throw new incorrectArgument("parametre(s) incorrect(s)");
        }

        // after getting the value correctly

        //getting all the cursors
        List<DrawingCursor> cursors = tab.getAllDrawingCursor();

        //move each active cursors by the value
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).setColorHexa(valueHexa);
            }
        }

    }
}