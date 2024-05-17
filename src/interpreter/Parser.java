package interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import interpreter.Exceptions.SyntaxError;
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
        
        //store each string so they arent impacted by the cleaning 
        
        //match all existing string 
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("((\"[^\"]*\"*[^\"]*\" *,)|(\"[^\"]*\"*[^\"]*\"))|(('[^']*'*[^']*' *,)|('[^']*'*[^']*'))").matcher(text);
        while (m.find()) {
            allMatches.add(m.group());
        }
        
        text = text.replace("§", "\\§");
        //replace
        for(int i=0; i<allMatches.size(); i++){
            text = text.replaceFirst(allMatches.get(i), "§"+i+"§");
            
        }
        
        //cleaning up phase
        text = text.trim();
        String[] parts = text.split(" ", 2);
        text = parts[0] + " " + parts[1].replaceAll(" ", "");
        
        //put original value back on
        for(int i=0; i<allMatches.size(); i++){
            text = text.replace("§"+i+"§", allMatches.get(i));
        }
        
        text = text.replace("\\§", "§");
        return text;
    }
    
    /**
    * Calculate a mathematique and/or boolean expression. (Must only containt number and operator)
    * List of all operator: +,-,*,/,==,!=,<=,>=,!,<,>,&&,||
    *
    * @param expression Instruction
    * @return resultat en Double
    */
    public static Double eval(String expression) throws NumberFormatException{
        Double v1Buffer = 0.0;
        Double v2Buffer = 0.0;
        Pattern pattern;
        Matcher matcher;
        String subEvalBuffer;
        
        
        //bracket first
        pattern = Pattern.compile("\\([^\\)\\(]*\\)");
        matcher = pattern.matcher(expression);
        
        while(matcher.find()){
            subEvalBuffer = matcher.group(0);
            expression = expression.replaceFirst(subEvalBuffer.replace("(", "\\(").replace(")", "\\)").replace("*", "\\*").replace("+", "\\+"), Parser.eval(subEvalBuffer.substring(1, subEvalBuffer.length()-1)).toString());
            matcher = pattern.matcher(expression);
        }
        
        // ! operator
        pattern = Pattern.compile("!-{0,1}(\\d{1,}(\\.\\d{1,}){0,1})");
        matcher = pattern.matcher(expression);
        
        while(matcher.find()){
            subEvalBuffer = matcher.group(0);
            v1Buffer = Double.valueOf(subEvalBuffer.split("!")[1]);
            
            expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer == 0 ? 1.0 : 0.0)).toString());
            
            matcher = pattern.matcher(expression);
        }
        
        // * and /
        pattern = Pattern.compile("(-{0,1}(\\d{1,}(\\.\\d{1,}){0,1})\\*-{0,1}(\\d{1,}(\\.\\d{1,}){0,1}))|(-{0,1}(\\d{1,}(\\.\\d{1,}){0,1})/-{0,1}(\\d{1,}(\\.\\d{1,}){0,1}))");
        matcher = pattern.matcher(expression);
        
        while(matcher.find()){
            subEvalBuffer = matcher.group(0);
            
            if(subEvalBuffer.split("\\*").length == 2){
                v1Buffer = Double.valueOf(subEvalBuffer.split("\\*")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("\\*")[1]);
                expression = expression.replaceFirst(subEvalBuffer.replace("*", "\\*"), ((Double)(v1Buffer*v2Buffer)).toString());
            }
            else{
                v1Buffer = Double.valueOf(subEvalBuffer.split("/")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("/")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer/v2Buffer)).toString());
                
            }
            
            matcher = pattern.matcher(expression);
            
        }
        
        // + and -
        pattern = Pattern.compile("(-{0,1}(\\d+(\\.\\d+){0,1})\\+-{0,1}(\\d+(\\.\\d+){0,1}))|(-{0,1}(\\d+(\\.\\d+){0,1})--{0,1}(\\d+(\\.\\d+){0,1}))");
        matcher = pattern.matcher(expression);
        
        while(matcher.find()){
            
            subEvalBuffer = matcher.group(0);
            
            if(subEvalBuffer.split("\\+").length == 2){
                v1Buffer = Double.valueOf(subEvalBuffer.split("\\+")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("\\+")[1]);
                expression = expression.replaceFirst(subEvalBuffer.replace("+", "\\+"), ((Double)(v1Buffer+v2Buffer)).toString());
            }
            else{
                v1Buffer = Double.valueOf(subEvalBuffer.split("-")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("-")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer-v2Buffer)).toString());
                
            }
            
            matcher = pattern.matcher(expression);
            
        }
        
        // ==, !=, <=, >=, <, >
        pattern = Pattern.compile("(-{0,1}(\\d+(\\.\\d+){0,1})!=-{0,1}(\\d+(\\.\\d+){0,1}))|(-{0,1}(\\d+(\\.\\d+){0,1})==-{0,1}(\\d+(\\.\\d+){0,1}))|(-{0,1}(\\d+(\\.\\d+){0,1})>=-{0,1}(\\d+(\\.\\d+){0,1}))|(-{0,1}(\\d+(\\.\\d+){0,1})<=-{0,1}(\\d+(\\.\\d+){0,1}))|(-{0,1}(\\d+(\\.\\d+){0,1})>-{0,1}(\\d+(\\.\\d+){0,1}))|(-{0,1}(\\d+(\\.\\d+){0,1})<-{0,1}(\\d+(\\.\\d+){0,1}))");
        matcher = pattern.matcher(expression);
        
        while(matcher.find()){
            subEvalBuffer = matcher.group(0);
            
            if(subEvalBuffer.split("==").length == 2){
                v1Buffer = Double.valueOf(subEvalBuffer.split("==")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("==")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer==v2Buffer ? 1.0 : 0.0)).toString());
            }
            else if(subEvalBuffer.split("!=").length == 2){
                v1Buffer = Double.valueOf(subEvalBuffer.split("!=")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("!=")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer!=v2Buffer ? 1.0 : 0.0)).toString());
            }
            else if(subEvalBuffer.split(">=").length == 2){
                v1Buffer = Double.valueOf(subEvalBuffer.split(">=")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split(">=")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer>=v2Buffer ? 1.0 : 0.0)).toString());
            }
            else if(subEvalBuffer.split("<=").length == 2){
                v1Buffer = Double.valueOf(subEvalBuffer.split("<=")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("<=")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer<=v2Buffer ? 1.0 : 0.0)).toString());
            }
            else if(subEvalBuffer.split(">").length == 2){
                v1Buffer = Double.valueOf(subEvalBuffer.split(">")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split(">")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer>v2Buffer ? 1.0 : 0.0)).toString());
            }
            else {
                v1Buffer = Double.valueOf(subEvalBuffer.split("<")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("<")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer<v2Buffer ? 1.0 : 0.0)).toString());
            }
            
            matcher = pattern.matcher(expression);
            
        }
        
        // && and ||
        pattern = Pattern.compile("(-{0,1}(\\d+(\\.\\d+){0,1})\\|\\|-{0,1}(\\d+(\\.\\d+){0,1}))|(-{0,1}(\\d+(\\.\\d+){0,1})&&-{0,1}(\\d+(\\.\\d+){0,1}))");
        matcher = pattern.matcher(expression);
        while(matcher.find()){
            
            subEvalBuffer = matcher.group(0);
            
            if(subEvalBuffer.split("\\|\\|").length == 2){
                v1Buffer = Double.valueOf(subEvalBuffer.split("\\|\\|")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("\\|\\|")[1]);
                expression = expression.replaceFirst(subEvalBuffer.replace("||", "\\|\\|"), ((Double)((v1Buffer != 0) || (v2Buffer != 0) ? 1.0 : 0.0)).toString() ) ;
            }
            else{
                v1Buffer = Double.valueOf(subEvalBuffer.split("&&")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("&&")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)((v1Buffer != 0) && (v2Buffer != 0) ? 1.0 : 0.0)).toString());
                
            }
            
            matcher = pattern.matcher(expression);
            
        }
        
        return Double.valueOf(expression);
    }
    
    /**
    * Get the values of arguments pass to a command
    *
    * @param rawArguments argument as a String separated by a coma
    * @param definedVariables List of all variables that are defined
    * @return List of all the value passed stored inside Variable sub class of the right type
    */
    public static List<Variable> getValueFromArgument(String rawArguments, List<Variable> definedVariables) throws SyntaxError{
        List<Variable> argumentList = new ArrayList<Variable>();
        
        List<String> allMatches = new ArrayList<String>();
        
        rawArguments = rawArguments.replace("§", "\\§");

        Matcher m = Pattern.compile("((\"[^\"]*\"*[^\"]*\" *,)|(\"[^\"]*\"*[^\"]*\"))|(('[^']*'*[^']*' *,)|('[^']*'*[^']*'))|(#([a-fA-F0-9]{6}))|([0-9]*.?[0-9]*)%").matcher(rawArguments);

        while (m.find()) {
            allMatches.add(m.group());
        }
        
        //replace
        for(int i=0; i<allMatches.size(); i++){
            if( allMatches.get(i).charAt( allMatches.get(i).length()-1 ) == ','){
                rawArguments = rawArguments.replaceFirst(allMatches.get(i), "§"+i+"§,");
                allMatches.set(i, allMatches.get(i).substring(0, allMatches.get(i).length()-1));
            }
            else{
                rawArguments = rawArguments.replaceFirst(allMatches.get(i), "§"+i+"§");
            }
            
        }

        Matcher variableMatcher;
        variableMatcher = Pattern.compile("([A-Z]|[a-z])[A-Za-z0-9]*").matcher(rawArguments);
        int indexOfVariable = 0;
        while (variableMatcher.find()) {

            if(definedVariables.contains( new Variable(variableMatcher.group(0)) )){
                
                indexOfVariable = definedVariables.indexOf(new Variable(variableMatcher.group(0)));
                

                if(definedVariables.get(indexOfVariable) instanceof VariableString){
                    
                    rawArguments = rawArguments.replaceFirst(definedVariables.get(indexOfVariable).getName(), "§"+allMatches.size()+"§");
                    allMatches.add("\""+((VariableString)definedVariables.get(indexOfVariable)).getValue()+"\"");
                }
                else{
                    rawArguments = rawArguments.replaceFirst(definedVariables.get(indexOfVariable).getName(), definedVariables.get(indexOfVariable).getValue().toString());
                }
            }
            else{
                throw new SyntaxError("Unknown variable: '"+variableMatcher.group(0)+"'");
            }

            variableMatcher = Pattern.compile("([A-Z]|[a-z])[A-Za-z0-9]*").matcher(rawArguments);

        }

        String[] rawArgumentsSplited = rawArguments.split(",");

        for(int i=0; i<rawArgumentsSplited.length; i++){
            
            if(rawArgumentsSplited[i].matches("-?\\d+(\\.\\d+)?")){
                argumentList.add( new VariableNumber(Double.parseDouble(rawArgumentsSplited[i])) );
            }
            //boolean
            else if(rawArgumentsSplited[i].matches("true|false")){
                argumentList.add( new VariableBoolean(rawArgumentsSplited[i] == "true") );
            }
            //string, hex and %
            else if(rawArgumentsSplited[i].matches("§\\d+§")){ 
                
                
                int value = Integer.valueOf(rawArgumentsSplited[i].substring(1, rawArgumentsSplited[i].length() - 1));
                rawArgumentsSplited[i] = rawArgumentsSplited[i].replace("§"+value+"§", allMatches.get(value)).replace("\\§", "§");

                StringBuilder sb = new StringBuilder(rawArgumentsSplited[i]);
                //remove quotes
                if(!rawArgumentsSplited[i].matches("(#([a-fA-F0-9]{6}))|([0-9]*.?[0-9]*)%")){
                    sb.deleteCharAt(rawArgumentsSplited[i].length() - 1);
                    sb.deleteCharAt(0);
                }
                
                argumentList.add( new VariableString(sb.toString()));
            }
            else{
                rawArgumentsSplited[i] = rawArgumentsSplited[i].replace("\\§", "§");
                try{
                    argumentList.add( new VariableNumber(Parser.eval(rawArgumentsSplited[i])) );
                }
                catch(Exception e){
                    throw new SyntaxError("Expression can't be evaluate. Please check for syntaxe error(s): '"+rawArgumentsSplited[i]+"'");
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
