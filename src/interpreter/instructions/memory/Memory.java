package interpreter.instructions.memory;

import java.util.List;

import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;

public interface Memory {

    public void execute(List<Variable> definedVariables, String name, String value) throws SyntaxError,InvalidArgument;
}
