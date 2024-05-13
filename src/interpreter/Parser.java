package interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creazt a list of instruction by reading a text and transform it in a list.
 * 
 */

public class Parser {


    public static List<String> getInstruction(String text){
        List<String> tmp = new ArrayList<String>();

        String[] line = text.split("\n");
        for (String word : line){
            tmp.add(word);
        }
        return tmp;
    }
    
}
