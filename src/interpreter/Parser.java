package interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        text = text.replaceAll(" {1,}", " ");
        text = text.replaceAll(" *, *", ",");

        if(text.charAt(0) == ' '){
            text = text.replaceFirst(" ", "");
        }

        for(int i=0; i<allMatches.size(); i++){
            text = text.replace("$"+i+"$", allMatches.get(i));
        }

        return text;
    }

}
