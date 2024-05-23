package interpreter.instructions.flow;

import java.util.List;

import fx.DrawingTab;
import interpreter.Interpreter;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;

/**
* interface that make the structure of any flow controle that used instruction encapsulated in {} (block), any flow that doesnt follow this interface will not be executed.
* All flow will create a new Interpreter loaded with the instructions inside of their block. They can also defined a function to run at the end of they're execution.
* A flow always have the generic syntaxe: " FLOWNAME argument{ ". This generic syntaxe is checked within the interpreter.
* A block act like a local space, so, all already defined variable are accessible and editable but any new variable defined within this block will not be accessible after the execution.
*/
public interface Flow  {
    /**
    * Create an interpreter and configured according to the need
    *
    * @param  parent DrawingTab object to execute the command in
    * @param  argument None parsed String containing the data between the Name of the flow and the {.
    * @param  instructions List of instruction inside the {} (block).
    * @param  definedVariables List of already defined variables.
    * @return an Interpreter configured or null
    * @throws SyntaxError if the syntax of the flow isnt respected. Each flow must check their own sub-syntaxe
    * @throws InvalidArgument if their is to much/not enought arguments and/or an argument isn't of the right type
    * @see Variable
    * @see Interpreter
    * @see DrawingTab
    */
    public Interpreter execute(DrawingTab parent, String argument, List<String> instructions, List<Variable> definedVariables) throws SyntaxError,InvalidArgument;
}
