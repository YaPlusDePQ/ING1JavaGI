package interpreter.instructions.memory;

import java.util.List;

import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;

public class DEL implements Memory{
    public void execute(List<Variable> definedVariables, String name, String value) throws SyntaxError,InvalidArgument {
        
        //check for incorrect or reserved variable name
        if( name.matches("true|false|FROM|TO|STEP") || !name.matches("([A-Z]|[a-z])[A-Za-z0-9]*")){
            throw new InvalidArgument("Variables name ('"+name+"') must at least start with a letter (a-z) and not be a reserved word");
        }
        
        Variable bufferCheckAvailability = new Variable(name); //buffer use to check the existance of a data 
        
        //if a value as been passed beacause someone is trying to break the code and understand how it work
        if(!value.equals("")){
            throw new SyntaxError("Why ? Why are you trying something like this ? You want to break the code ? Try again.");
        }
        
        //remove if the var existe
        if(definedVariables.contains(bufferCheckAvailability)){
            definedVariables.remove(definedVariables.indexOf(bufferCheckAvailability));
        }
        else{
            throw new InvalidArgument("Unknown variable:'"+name+"'");
        }
    }
}
