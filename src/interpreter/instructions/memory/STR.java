package interpreter.instructions.memory;

import java.util.List;

import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;
import interpreter.variables.VariableString;

public class STR implements Memory{
    public void execute(List<Variable> definedVariables, String name, String value) throws SyntaxError,InvalidArgument {
        
        if( name.matches("true|false|FROM|TO|STEP") || !name.matches("([A-Z]|[a-z])[A-Za-z0-9]*")){
            throw new InvalidArgument("Variables name ('"+name+"') must at least start with a letter (a-z) and not be a reserved word");
        }
        
        
        VariableString var = null;
        Variable bufferCheckAvailability = new Variable(name);
        String parsedValue = "";

        if(definedVariables.contains( new Variable(value))){
            parsedValue = definedVariables.get( definedVariables.indexOf(new Variable(value)) ).getValue().toString();
        }
        else{
            StringBuilder sb = new StringBuilder(value);
            //remove quotes

            if(value.matches("((\"[^\"]*\"*[^\"]*\")|(\"[^\"]*\"*[^\"]*\"))|(('[^']*'*[^']*')|('[^']*'*[^']*'))")){
                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(0);
            }

            parsedValue = sb.toString();
        }

        
        
        if(definedVariables.contains(bufferCheckAvailability)){
            if( !(definedVariables.get( definedVariables.indexOf(bufferCheckAvailability) ) instanceof VariableString) ){
                throw new InvalidArgument("'"+name+"' is already defined and isn't of type 'String'");
            }
            
            var = (VariableString)definedVariables.get(definedVariables.indexOf(bufferCheckAvailability));
            var.setValue(parsedValue);
        }
        else{
            var =  new VariableString(name, parsedValue);
            definedVariables.add(var);
        }
    }
}
