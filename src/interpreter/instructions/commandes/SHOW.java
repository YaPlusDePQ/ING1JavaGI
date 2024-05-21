package interpreter.instructions.commandes;

import java.util.List;

import fx.DrawingCursor;
import fx.DrawingTab;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;

public class SHOW implements Command{
    /**
    * execute the command
    *
    * @param  tab DrawingTab object to execute the command in
    * @param  args list of arguments send to the command
    */
    public void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument{

        if(args.size() != 0){
            throw new SyntaxError("Need 0 argument");
        }

        List<DrawingCursor> cursors = tab.getAllDrawingCursor();

        //apply change
        for(int i=0; i<cursors.size(); i++){
            if(cursors.get(i).isActive()){
                cursors.get(i).setVisible(true);
            }
        }

        tab.drawCursor();

    }
}
