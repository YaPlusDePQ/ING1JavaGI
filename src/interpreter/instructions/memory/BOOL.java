package interpreter.instructions.memory;

import java.util.List;

import interpreter.Parser;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;
import interpreter.variables.VariableBoolean;

/**
 * creates a Boolean type variable. If the value is omitted, the value equivalent to false will be used.
 */
public class BOOL implements Memory{

    public void execute(List<Variable> definedVariables, String name, String value) throws SyntaxError,InvalidArgument {

        //check for incorrect or reserved variable name
        if( name.matches("true|false|FROM|TO|STEP") || !name.matches("([A-Z]|[a-z])[A-Za-z0-9]*")){
            throw new InvalidArgument("Variables name ('"+name+"') must at least start with a letter (a-z) and not be a reserved word");
        }


        VariableBoolean var = null;
        Variable bufferCheckAvailability = new Variable(name); //buffer use to check the existance of a data 
        double parsedValue = 0;

        // in case a values as been passed
        if(!value.equals("")){
            try{
                parsedValue = Parser.eval(value, definedVariables);
            }
            catch(NumberFormatException e){
                throw new SyntaxError("'"+value+"' doesnt compute to any value of type 'Number' or 'Boolean'");
            }
        }

        //check for already existing variable
        if(definedVariables.contains(bufferCheckAvailability)){
            //if the variable existe and is of the right type update its value
            if( !(definedVariables.get( definedVariables.indexOf(bufferCheckAvailability) ) instanceof VariableBoolean) ){
                throw new InvalidArgument("'"+name+"' is already defined and isn't of type 'Boolean'");
            }
            
            var = (VariableBoolean)definedVariables.get(definedVariables.indexOf(bufferCheckAvailability));
            var.setValue(parsedValue != 0);
        }
        else{
            //create the variable
            var =  new VariableBoolean(name, parsedValue != 0);
            definedVariables.add(var);
        }
    }
}
