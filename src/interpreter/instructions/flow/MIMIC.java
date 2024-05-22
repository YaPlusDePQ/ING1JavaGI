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

public class MIMIC implements Flow{
    public Interpreter execute(DrawingTab parent, String argument, List<String> instructions, List<Variable> definedVariables) throws SyntaxError,InvalidArgument{

        int id = 0;
        List<DrawingCursor> duplicatedCursors =  new ArrayList<DrawingCursor>();

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
        DrawingCursor buffer = null;
        for(int i=0; i<cursorMax; i++){
            if(cursors.get(i).isActive()){

                buffer = new DrawingCursor(id, cursors.get(i), parent.getDrawingCursor(id).isActive());
                System.out.println(buffer);
                duplicatedCursors.add(buffer);
                parent.getAllDrawingCursor().add(buffer);
            }
        }

        Interpreter subFlow = new Interpreter(parent, "", definedVariables);
        subFlow.setIntruction(instructions);
        
        subFlow.onEndOfSCript = (self) -> {

            for(DrawingCursor duplicated : duplicatedCursors){
                parent.getAllDrawingCursor().remove(duplicated);
            }

            return Interpreter.END_OF_SCRIPT;
        };

        return subFlow;
    }
}
