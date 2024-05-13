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
        int open = 0;
        int close = 0;
        String[] line = text.split("\n");
        List<String> tmp = new ArrayList<String>(Arrays.asList(line));
        for(int i=0; i<tmp.size(); i++){
            if (tmp.get(i) == "{"){
                open++;
            }else if (tmp.get(i) == "{"){
                close++;
            }        
        }
        if (close == open){
            return true;
        }
        else{
            return false;
        }
    }

    


}
