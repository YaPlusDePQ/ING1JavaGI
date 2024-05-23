package interpreter.instructions.commandes;

import fx.DrawingTab;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;
import java.util.List;

/**
* interface that make the structure of any command, any command that doesnt follow this interface will not be executed.
* any change MSUST be apply (if not specified by the command) to ALL ACTIVE cursor.
* A command always have the generic syntaxe: " COMMANDNAME arg1,..,argn ". This generic syntaxe is checked within the interpreter.
*/
public interface Command {

    /**
    * execute the command
    *
    * @param  tab DrawingTab object to execute the command in
    * @param  args list of arguments send to the command. Argument will be encapsulate in a Variable sub class soi that they can be handle in function of the data in it.
    * @throws SyntaxError if the syntax of the command isnt respected. EAch command must check their own sub-syntaxe
    * @throws InvalidArgument if their is to much/not enought arguments and/or an argument is't of the right type
    * @see Variable
    * @see DrawingTab
    */
    public void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument;

}
