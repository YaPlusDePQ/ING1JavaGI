package interpreter.instructions.commandes;

import fx.DrawingTab;
import interpreter.Exceptions.InvalidArgument;
import interpreter.Exceptions.SyntaxError;
import interpreter.variables.Variable;
import java.util.List;

/**
* Class that make the structure of any command
*/
public class Command {

    /**
    * execute the command
    *
    * @param  tab DrawingTab object to execute the command in
    * @param  args list of arguments send to the command
    */
    public static void execute(DrawingTab tab, List<Variable> args) throws SyntaxError,InvalidArgument{
        return;
    };
}
