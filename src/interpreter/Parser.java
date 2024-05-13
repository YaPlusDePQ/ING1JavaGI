package interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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




}
