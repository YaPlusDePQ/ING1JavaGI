package interpreter.instructions.flow;

import java.util.ArrayList;
import java.util.List;

import fx.DrawingCursor;
import fx.DrawingTab;
import interpreter.Interpreter;
import interpreter.Parser;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;

/**
 * MIMIC cursorID: Creates a temporary cursor at the current cursor to perform
 * exactly the same actions as another cursor that has been passed
 * the identifier.
 * <p>
 * PS: If a MIRROR/MIMIC instruction is used in another block
 * of MIRROR/MIMIC instructions, then the original cursor and the 1st cursor
 * duplicated will be duplicated again and this will create a display
 * 4 simultaneous cursors, ect.
 */
public class MIMIC implements Flow{
    public Interpreter execute(DrawingTab parent, String argument, List<String> instructions, List<Variable> definedVariables) throws SyntaxError,InvalidArgument{

        int id = 0;
        List<DrawingCursor> duplicatedCursors =  new ArrayList<DrawingCursor>(); //list that will containt all cursor duplicated

        try{
            id = (int)Parser.eval(argument, definedVariables).doubleValue();
        }
        catch(NumberFormatException e){
            throw new InvalidArgument("Argument must be 1 positive number [Integer]");
        }

        if(id < 0){
            throw new InvalidArgument("Argument must be 1 positive number [Integer]");
        }
        
        if(parent.getDrawingCursor(id) == null){
            throw new InvalidArgument("Cursor '"+id+"' does not exist");
        }
        
        List<DrawingCursor> cursors = parent.getAllDrawingCursor();
        int cursorMax = cursors.size();
        DrawingCursor buffer = null; //store the cursor freshly created before it is added to the the list so it can be configured

        //for each active cursor create a cursor with it current configuration but link it at the asked cursor id
        for(int i=0; i<cursorMax; i++){
            if(cursors.get(i).isActive()){

                buffer = new DrawingCursor(id, cursors.get(i), parent.getDrawingCursor(id).isActive()); //set the id at the value of cursor it as to copy the actions + set its active state at the same of the target cursor

                duplicatedCursors.add(buffer);
                parent.getAllDrawingCursor().add(buffer);
            }
        }

        //setting up the interpreter
        Interpreter subFlow = new Interpreter(parent, "", definedVariables);
        subFlow.setIntruction(instructions);
        
        //set function to run at the end of the block
        subFlow.onEndOfSCript = (self) -> {
            //remove all duplacted cursor
            for(DrawingCursor duplicated : duplicatedCursors){
                parent.getAllDrawingCursor().remove(duplicated);
            }

            return Interpreter.END_OF_SCRIPT;
        };

        return subFlow;
    }
}
