package interpreter.instructions.flow;

import java.util.List;
import java.util.ArrayList;

import fx.DrawingCursor;
import fx.DrawingTab;
import interpreter.Interpreter;
import interpreter.Parser;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;
import interpreter.variables.VariableString;

public class MIRROR implements Flow{
        public Interpreter execute(DrawingTab parent, String argument, List<String> instructions, List<Variable> definedVariables) throws SyntaxError,InvalidArgument{

        List<Variable> finalArguments = Parser.getValueFromArgument(argument, definedVariables);
        List<DrawingCursor> duplicatedCursors =  new ArrayList<DrawingCursor>();
        double[] coords = {0,0,0,0};
        int mirrorMode = 0;
        
        if(finalArguments.size() != 2 && finalArguments.size() != 4){
            throw new SyntaxError("Need 2 or 4 arguments");
        }

        int i=0;
        for(Variable arg : finalArguments){
            if( arg instanceof VariableString){
                if( !((VariableString)arg).getValue().matches("([0-9]*\\.?[0-9]*) *%") ){ // check if its a %, if not throw an error
                    throw new InvalidArgument("Arguments must be pourcentages [String] or numbers [Integer/Double]");
                }
                
                coords[i] = Parser.percentageToDouble((String)arg.getValue()); //convert the value from a string to a number
                coords[i] = parent.getWidth() > parent.getHeight() ? (coords[i])*parent.getWidth() : (coords[i])* parent.getHeight(); // it as to be a pourcentage of the biggest value between widht and height
                
            }
            else if(arg instanceof VariableNumber) {
                coords[i] = ((VariableNumber)arg ) .getValue().doubleValue();
            }
            else{
                throw new InvalidArgument("Arguments must be pourcentages [String] or numbers [Integer/Double]");
            }
            i++;
        }


        if(finalArguments.size() == 2){
            mirrorMode = DrawingCursor.MIRRORED_POINT;
        }
        else if(finalArguments.size() == 4){
            mirrorMode = DrawingCursor.MIRRORED_AXIS;
        }

        List<DrawingCursor> cursors = parent.getAllDrawingCursor();
        int cursorMax = cursors.size();
        DrawingCursor buffer = null;
        for(i=0; i<cursorMax; i++){
            if(cursors.get(i).isActive()){

                System.out.println(cursors.get(i));

                buffer = new DrawingCursor(cursors.get(i).getID(), cursors.get(i), true);
                buffer.linkToOtherCursor(mirrorMode, cursors.get(i), coords);
                duplicatedCursors.add(buffer);

                System.out.println(buffer);

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
