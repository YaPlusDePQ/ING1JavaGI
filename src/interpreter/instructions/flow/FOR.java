package interpreter.instructions.flow;

import java.util.List;

import fx.DrawingTab;
import interpreter.Interpreter;
import interpreter.Parser;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;

/**
 * FOR name [FROM v1] TO v2 [STEP v3]: loop for with the declaration of a
 * variable name name and terminals v1 and v2 (v2 included or excluded for you to
 * define it) and by step v3. The FROM and STEP fields are optional and the
 * Default values ​​are 0 and 1 respectively. name should not be the name
 * of an existing variable.
 */
public class FOR implements Flow {
    public Interpreter execute(DrawingTab parent, String argument, List<String> instructions, List<Variable> definedVariables) throws SyntaxError,InvalidArgument{

        //check if argument respect the syntaxe of a FOR LOOP
        if(!argument.matches("[a-zA-Z][a-zA-Z0-9]*(FROM([a-zA-Z][a-zA-Z0-9]*|-?(\\d+(\\.\\d+)?)))?TO([a-zA-Z][a-zA-Z0-9]*|-?(\\d+(\\.\\d+)?))(STEP([a-zA-Z][a-zA-Z0-9]*|-?(\\d+(\\.\\d+)?)))?")){
            throw new SyntaxError("Incorrect syntaxe for FOR loop:FOR var [FROM v1] TO v2 [STEP v3]");
        }
        
        String[] argumentSplited = argument.replaceAll("FROM|TO|STEP", ",").split(","); //split so each value can be access easly
        String varName = "";
        double from = 0;
        double to = 0;
        double step = 1;
        
        VariableNumber forVar;


        //get data for each different syntaxe possible

        //syntaxe  name FROM v1 TO v2 STEP v3
        if(argument.matches("[a-zA-Z][a-zA-Z0-9]*(FROM([a-zA-Z][a-zA-Z0-9]*|-?(\\d+(\\.\\d+)?)))TO([a-zA-Z][a-zA-Z0-9]*|-?(\\d+(\\.\\d+)?))(STEP([a-zA-Z][a-zA-Z0-9]*|-?(\\d+(\\.\\d+)?)))") && argumentSplited.length == 4){
            varName = argumentSplited[0];

            //get v1
            if(argumentSplited[1].matches("-?(\\d+(\\.\\d+)?)")){ //check if number
                from = Double.valueOf(argumentSplited[1]);
            }
            else{
                try{
                    from = Parser.eval(argumentSplited[1], definedVariables); //compute the value if its a variable or an expression
                }
                catch(Exception e){
                    throw new InvalidArgument("'"+argumentSplited[1]+"' isnt a numerical value or a variable");
                }
            }

            //get v2
            if(argumentSplited[2].matches("-?(\\d+(\\.\\d+)?)")){
                to = Double.valueOf(argumentSplited[2]);
            }
            else{
                try{
                    to = Parser.eval(argumentSplited[2], definedVariables);
                }
                catch(Exception e){
                    throw new InvalidArgument("'"+argumentSplited[2]+"' isnt a numerical value or a variable");
                }
            }

            //get v3
            if(argumentSplited[3].matches("-?(\\d+(\\.\\d+)?)")){
                step = Double.valueOf(argumentSplited[3]);
            }
            else{
                try{
                    step = Parser.eval(argumentSplited[3], definedVariables);
                }
                catch(Exception e){
                    throw new InvalidArgument("'"+argumentSplited[3]+"' isnt a numerical value or a variable");
                }
            }
        }
        //syntaxe  name FROM v1 TO v2
        else if(argument.matches("[a-zA-Z][a-zA-Z0-9]*(FROM([a-zA-Z][a-zA-Z0-9]*|-?(\\d+(\\.\\d+)?)))TO([a-zA-Z][a-zA-Z0-9]*|-?(\\d+(\\.\\d+)?))") && argumentSplited.length == 3){
            varName = argumentSplited[0];

            //get v1
            if(argumentSplited[1].matches("-?(\\d+(\\.\\d+)?)")){
                from = Double.valueOf(argumentSplited[1]);
            }
            else{
                try{
                    from = Parser.eval(argumentSplited[1], definedVariables);
                }
                catch(Exception e){
                    throw new InvalidArgument("'"+argumentSplited[1]+"' isnt a numerical value or a variable");
                }
            }

            //get v2
            if(argumentSplited[2].matches("-?(\\d+(\\.\\d+)?)")){
                to = Double.valueOf(argumentSplited[2]);
            }
            else{
                try{
                    to = Parser.eval(argumentSplited[2], definedVariables);
                }
                catch(Exception e){
                    throw new InvalidArgument("'"+argumentSplited[2]+"' isnt a numerical value or a variable");
                }
            }

        }
        //syntaxe name TO v2 STEP v3
        else if(argument.matches("[a-zA-Z][a-zA-Z0-9]*TO([a-zA-Z][a-zA-Z0-9]*|-?(\\d+(\\.\\d+)?))(STEP([a-zA-Z][a-zA-Z0-9]*|-?(\\d+(\\.\\d+)?)))") && argumentSplited.length == 3){
            varName = argumentSplited[0];

            //get v2
            if(argumentSplited[1].matches("-?(\\d+(\\.\\d+)?)")){
                to = Double.valueOf(argumentSplited[1]);
            }
            else{
                try{
                    to = Parser.eval(argumentSplited[1], definedVariables);
                }
                catch(Exception e){
                    throw new InvalidArgument("'"+argumentSplited[1]+"' isnt a numerical value or a variable");
                }
            }

            //get v3
            if(argumentSplited[2].matches("-?(\\d+(\\.\\d+)?)")){
                step = Double.valueOf(argumentSplited[2]);
            }
            else{
                try{
                    step = Parser.eval(argumentSplited[2], definedVariables);
                }
                catch(Exception e){
                    throw new InvalidArgument("'"+argumentSplited[2]+"' isnt a numerical value or a variable");
                }
            }

        }
        //syntaxe  name TO v2
        else{
            varName = argumentSplited[0];

            //get v2
            if(argumentSplited[1].matches("-?(\\d+(\\.\\d+)?)")){
                to = Double.valueOf(argumentSplited[1]);
            }
            else{
                try{
                    to = Parser.eval(argumentSplited[1], definedVariables);
                }
                catch(Exception e){
                    throw new InvalidArgument("'"+argumentSplited[1]+"' isnt a numerical value or a variable");
                }
            }
        }


        if(definedVariables.contains(new Variable(varName))){
            throw new InvalidArgument("'"+varName+"' already existe as a variable");
        }

        final double finalFrom = from;
        final double finalTo = to;
        final double finalStep = step;

        //setting up the interpreter
        Interpreter subFlow = new Interpreter(parent, "", definedVariables);
        subFlow.setIntruction(instructions);

        forVar = new VariableNumber(varName, finalFrom);
        subFlow.getVariables().add(forVar);
        
        //function to run when the flow reach its end
        subFlow.onEndOfSCript = (self) -> {
            if(forVar.getValue() != finalTo){ //if the variable is different of the objective value (to)
                self.setIndex(0); //reset the index
                forVar.setValue(forVar.getValue()+finalStep); // add step
                return Interpreter.PROCESSE_DONE;
            }
            else{
                return Interpreter.END_OF_SCRIPT;
            }
        };

        return subFlow;
    }
}
