package interpreter.instructions.memory;

import java.util.List;

import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;
import interpreter.variables.VariableString;

/**
 * created a string variable. If no value is provided, an empty string is created.
 */
public class STR implements Memory{
    public void execute(List<Variable> definedVariables, String name, String value) throws SyntaxError,InvalidArgument {
        
        //check for incorrect or reserved variable name
        if( name.matches("true|false|FROM|TO|STEP") || !name.matches("([A-Z]|[a-z])[A-Za-z0-9]*")){
            throw new InvalidArgument("Variables name ('"+name+"') must at least start with a letter (a-z) and not be a reserved word");
        }
        
        
        VariableString var = null;
        Variable bufferCheckAvailability = new Variable(name); //buffer use to check the existance of a data 
        String parsedValue = "";

        // in case a values as been passed
        if(definedVariables.contains( new Variable(value))){
            //if whats as been passed is a variable get the value of it
            parsedValue = definedVariables.get( definedVariables.indexOf(new Variable(value)) ).getValue().toString();
        }
        else{
            //treat everything alse as a string, if no quotes has been used, spaces have been removed
            StringBuilder sb = new StringBuilder(value);

            //remove quotes if needed
            if(value.matches("((\"[^\"]*\"*[^\"]*\")|(\"[^\"]*\"*[^\"]*\"))|(('[^']*'*[^']*')|('[^']*'*[^']*'))")){
                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(0);
            }

            parsedValue = sb.toString();
        }

        
        //check for already existing variable
        if(definedVariables.contains(bufferCheckAvailability)){
            //if the variable existe and is of the right type update its value
            if( !(definedVariables.get( definedVariables.indexOf(bufferCheckAvailability) ) instanceof VariableString) ){
                throw new InvalidArgument("'"+name+"' is already defined and isn't of type 'String'");
            }
            
            var = (VariableString)definedVariables.get(definedVariables.indexOf(bufferCheckAvailability));
            var.setValue(parsedValue);
        }
        else{
            //create the variable
            var =  new VariableString(name, parsedValue);
            definedVariables.add(var);
        }
    }
}
