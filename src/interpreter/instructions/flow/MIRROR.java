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

/**
 * MIRROR x1[%], y1[%], x2[%], y2[%]: temporarily duplicates the cursor
 * current and carries out all instructions with axial symmetry. 2 points
 * of the axis of symmetry are provided as a parameter to this instruction. THE
 * values ​​are absolute real values ​​or in percentages as for
 * simple commands.
 * <p>
 * MIRROR x1[%], y1[%]: temporarily duplicates the current cursor and
 * performs all instructions in the block with central symmetry. Point
 * of symmetry is given as a parameter of this instruction.
 * <p>
 * PS: If a MIRROR/MIMIC instruction is used in another block
 * of MIRROR/MIMIC instructions, then the original cursor and the 1st cursor
 * duplicated will be duplicated again and this will create a display
 * 4 simultaneous cursors, ect.
 */
public class MIRROR implements Flow{
        public Interpreter execute(DrawingTab parent, String argument, List<String> instructions, List<Variable> definedVariables) throws SyntaxError,InvalidArgument{
        
        
        List<Variable> finalArguments = Parser.getValueFromArgument(argument, definedVariables); //getting the argument (syntax is the same asa  command)

        List<DrawingCursor> duplicatedCursors =  new ArrayList<DrawingCursor>(); //list of all duplacted cursor created
        double[] coords = {0,0,0,0}; // array to store the futures coordinates of point used for each symmetrie
        int mirrorMode = 0; //get the direction mode use for 
        
        if(finalArguments.size() != 2 && finalArguments.size() != 4){
            throw new SyntaxError("Need 2 or 4 arguments");
        }

        int i=0;
        //get the value of each argument passed and add it to coords
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

        //selecte the corrected mode 
        if(finalArguments.size() == 2){
            mirrorMode = DrawingCursor.MIRRORED_POINT;
        }
        else if(finalArguments.size() == 4){
            mirrorMode = DrawingCursor.MIRRORED_AXIS;
        }

        List<DrawingCursor> cursors = parent.getAllDrawingCursor();
        int cursorMax = cursors.size();
        DrawingCursor buffer = null; //store the cursor freshly created before it is added to the the list so it can be configured

        //for each cursor currently active create a duplicated cursor that will behave as the mirrorMode intented it to
        for(i=0; i<cursorMax; i++){
            if(cursors.get(i).isActive()){

                buffer = new DrawingCursor(cursors.get(i).getID(), cursors.get(i), true); //create a cursor with the same configuration, it as the same id as the one it copy from
                buffer.linkToOtherCursor(mirrorMode, cursors.get(i), coords); //update it configuration so its it the one intented by the mirrorMode selected

                duplicatedCursors.add(buffer);
                parent.getAllDrawingCursor().add(buffer);
                
            }
        }

        //configure interpreter
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
