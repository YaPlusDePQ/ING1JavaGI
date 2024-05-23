package interpreter.instructions.memory;

import java.util.List;

import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;

/**
* interface that make the structure of all memory management (variable), any memory management that doesnt follow this interface will not be executed.
* A command always have the generic syntaxe: " MEMORYNAME variable = value " or "  MEMORYNAME variable ". This generic syntaxe is checked within the interpreter.
*/
public interface Memory {
    /**
    * execute the command
    *
    * @param  definedVariables List of existing variables
    * @param  name name of the variable that will be affected
    * @param  value value that can be allocated to the variable. can be "" if nothing is allocated
    * @throws SyntaxError if the syntax of the command isnt respected. EAch command must check their own sub-syntaxe
    * @throws InvalidArgument if their is to much/not enought arguments and/or an argument isn't of the right type
    * @see Variable
    */
    public void execute(List<Variable> definedVariables, String name, String value) throws SyntaxError,InvalidArgument;
}
