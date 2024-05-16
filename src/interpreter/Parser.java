package interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import interpreter.variables.*;

/**
* String parser for the Interpreter and related instructions
*/
public class Parser {
    
    /**
    * Split a String at each \n to get a list of individual instruction
    *
    * @param text source
    * @return List of individual instructions
    */
    public static List<String> getInstruction(String text){
        String[] line = text.split("\n");
        List<String> tmp = new ArrayList<String>(Arrays.asList(line));
        return tmp;
    }
    
    /**
    * Check if the source number of { match the one of }
    *
    * @param text source
    * @return true if it match
    */
    public static Boolean isValide(String text){
        String[] line = text.split("\n");
        List<String> tmp = new ArrayList<String>(Arrays.asList(line));
        long open = tmp.stream().filter(x->x.equals("{")).count();
        long close = tmp.stream().filter(x->x.equals("}")).count();
        
        if (close == open){
            return true;
        }
        else{
            return false;
        }
    } 
    
    /**
    * Clean up an instruction from excessive space while keeping the integrity of String inside
    *
    * @param text Instruction
    * @return cleaned up Instruction
    */
    public static String cleanUpInstruction(String text){
        
        //store each string so they arent impacted by the cleaning (store at the same time any element that will could interfere with the replacement token use)

        //match all existing string (and any element that will could interfere with the replacement token used)
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("\"[^\"]*\"|'[^']*'|\\$[0-9]{1,}\\$").matcher(text);
        while (m.find()) {
            allMatches.add(m.group());
        }
        
        //replace
        for(int i=0; i<allMatches.size(); i++){
            text = text.replace(allMatches.get(i), "$"+i+"$");
        }
        
        //cleaning up phase
        text = text.trim();
        text = text.replaceAll(" {1,}", " ");
        text = text.replaceAll(" *, *", ",");
        
        //put original value back on
        for(int i=0; i<allMatches.size(); i++){
            text = text.replace("$"+i+"$", allMatches.get(i));
        }
        
        return text;
    }
    
    /**
    * Get the values of arguments pass to a command
    *
    * @param rawArguments argument as a String separated by a coma
    * @param definedVariables List of all variables that are defined
    * @return List of all the value passed stored inside Variable sub class of the right type
    */
    public static List<Variable> getValueFromArgument(String rawArguments, List<Variable> definedVariables){
        List<Variable> argumentList = new ArrayList<Variable>();
        
        String[] rawArgumentsSplited = rawArguments.split(",");
        
        for(int i=0; i<rawArgumentsSplited.length; i++){
            
            if( definedVariables.contains( new Variable(rawArgumentsSplited[i]) ) ){
                //get the value if the argument is the name of a variable 
                argumentList.add( definedVariables.get( definedVariables.indexOf( new Variable(rawArgumentsSplited[i])  ) ) );
            }
            else{
                //number 
                if(rawArgumentsSplited[i].matches("-?\\d+(\\.\\d+)?")){
                    argumentList.add( new VariableNumber(Double.parseDouble(rawArgumentsSplited[i])) );
                }
                //boolean
                else if(rawArgumentsSplited[i].matches("true|false")){
                    argumentList.add( new VariableBoolean(rawArgumentsSplited[i] == "true") );
                }
                //else while be treated as a String
                else{ 
                    StringBuilder sb = new StringBuilder(rawArgumentsSplited[i]);
                    //remove quotes if necessary
                    if(rawArgumentsSplited[i].matches("\"[^\"]*\"|'[^']*'")){
                        sb.deleteCharAt(rawArgumentsSplited[i].length() - 1);
                        sb.deleteCharAt(0);
                    }
                    argumentList.add( new VariableString(sb.toString()));
                }
            }
            
        }
        
        return argumentList;
    }
    
    /**
     * Converte a string representing a % to the corresponding double.
     * @param pourcentage
     * @return value of the pourcentage
     */
    public static double percentageToDouble(String pourcentage){
        return Double.valueOf(pourcentage.replace(" *", "").replace("%", "")).doubleValue()/100;
    }
    
}
