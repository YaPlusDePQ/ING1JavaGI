package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingCursor;
import fx.DrawingTab;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;

/**
* hides the cursor on the screen.
*/
public class HIDE implements Command{

    public void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument{
        
        if(args.size() != 0){
            throw new SyntaxError("Need 0 argument");
        }
        
        List<DrawingCursor> cursors = tab.getAllDrawingCursor();
        
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).setVisible(false);
            }
        }
        
        tab.drawCursor(); 
        
    }
}
