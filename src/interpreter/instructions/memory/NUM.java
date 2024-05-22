package interpreter.instructions.memory;

import interpreter.Parser;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;
import interpreter.variables.VariableNumber;
import java.util.List;


public class NUM implements Memory{
    public void execute(List<Variable> definedVariables, String name, String value) throws SyntaxError,InvalidArgument {

        if( name.matches("true|false|FROM|TO|STEP") || !name.matches("([A-Z]|[a-z])[A-Za-z0-9]*")){
            throw new InvalidArgument("Variables name ('"+name+"') must at least start with a letter (a-z) and not be a reserved word");
        }


        VariableNumber var = null;
        Variable bufferCheckAvailability = new Variable(name);
        double parsedValue = 0;

        if(!value.equals("")){
            try{
                parsedValue = Parser.eval(value, definedVariables);
            }
            catch(NumberFormatException e){
                throw new SyntaxError("'"+value+"' doesnt compute to any value of type 'Number'");
            }
        }
        

        if(definedVariables.contains(bufferCheckAvailability)){
            if( !(definedVariables.get( definedVariables.indexOf(bufferCheckAvailability) ) instanceof VariableNumber) ){
                throw new InvalidArgument("'"+name+"' is already defined and isn't of type 'Number'");
            }
            
            var = (VariableNumber)definedVariables.get(definedVariables.indexOf(bufferCheckAvailability));
            var.setValue(parsedValue);
        }
        else{
            var =  new VariableNumber(name, parsedValue);
            definedVariables.add(var);
        }
    }
}
