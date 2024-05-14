package interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import interpreter.variables.*;

/**
 * This class creazt a list of instruction by reading a text and transform it in a list.
 * 
 */

public class Parser {


    public static List<String> getInstruction(String text){
        String[] line = text.split("\n");
        List<String> tmp = new ArrayList<String>(Arrays.asList(line));
        return tmp;
    }

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

    public static String cleanUpInstruction(String text){

        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("\"[^\"]*\"|'[^']*'").matcher(text);
        while (m.find()) {
            allMatches.add(m.group());
        }

        for(int i=0; i<allMatches.size(); i++){
            text = text.replace(allMatches.get(i), "$"+i+"$");
        }

        text = text.trim();
        text = text.replaceAll(" {1,}", " ");
        text = text.replaceAll(" *, *", ",");

        for(int i=0; i<allMatches.size(); i++){
            text = text.replace("$"+i+"$", allMatches.get(i));
        }

        return text;
    }

    public static List<Variable> getValueFromArgument(String rawArguments, List<Variable> definedVariables) throws InstructionSyntaxError{
        List<Variable> argumentList = new ArrayList<Variable>();

        String[] rawArgumentsSplited = rawArguments.split(",");

        for(int i=0; i<rawArgumentsSplited.length; i++){
            
            if( definedVariables.contains( new Variable(rawArgumentsSplited[i]) ) ){
                argumentList.add( definedVariables.get( definedVariables.indexOf( new Variable(rawArgumentsSplited[i])  ) ) );
            }
            else{
                if(rawArgumentsSplited[i].matches("-?\\d+(\\.\\d+)?")){
                    argumentList.add( new VariableNumber("",  Double.parseDouble(rawArgumentsSplited[i])) );
                }
                else if(rawArgumentsSplited[i].matches("true|false")){
                    argumentList.add( new VariableBoolean("", rawArgumentsSplited[i] == "true") );
                }
                else{ 
                    StringBuilder sb = new StringBuilder(rawArgumentsSplited[i]);
                    if(rawArgumentsSplited[i].matches("\"[^\"]*\"|'[^']*'")){
                        sb.deleteCharAt(rawArgumentsSplited[i].length() - 1);
                        sb.deleteCharAt(0);
                    }
                    argumentList.add( new VariableString("", sb.toString()));
                }
            }

        }

        return argumentList;
    }

    public static double percentageToDouble(String pourcentage){
        return Double.valueOf(pourcentage.replace(" *", "").replace("%", ""));
    }

}
