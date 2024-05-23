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
    * @param instruction source
    * @return true if it match
    */
    public static Boolean isValide(String instruction){
        String[] line = instruction.split("\n");
        List<String> tmp = new ArrayList<String>(Arrays.asList(line));

        long open = 0;
        long close = 0;

        for (String str : tmp) {
            open += str.contains("{") ? 1 : 0;
            close += str.contains("}") ? 1 : 0;
        }

        if (close == open){
            return true;
        }
        else{
            return false;
        }
    } 

    /**
    * Check if the source number of { match the one of }
    *
    * @param parsedInstruction source
    * @return true if it match
    */
    public static Boolean isValide(List<String> parsedInstruction){
        long open = 0;
        long close = 0;

        for (String str : parsedInstruction) {
            open += str.contains("{") ? 1 : 0;
            close += str.contains("}") ? 1 : 0;
        }
        
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
    * @param instruction Instruction
    * @return cleaned up Instruction
    */
    public static String cleanUpInstruction(String instruction){

        instruction = instruction.trim();
        
        //add space to complete the instruction patern
        if(instruction.matches("[A-Z]+")){
            return instruction+" ";
        }
        
        //match all existing expression of litteral string
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("((\"[^\"]*\"*[^\"]*\" *,)|(\"[^\"]*\"*[^\"]*\"))|(('[^']*'*[^']*' *,)|('[^']*'*[^']*'))").matcher(instruction); //this regex can understant " inside " "
        while (m.find()) {
            allMatches.add(m.group());
        }
        
        //protection against trick from someone who want to break the code
        instruction = instruction.replace("§", "\\§");
        //replace all string by a tag to protected theire space from being remove
        for(int i=0; i<allMatches.size(); i++){
            instruction = instruction.replaceFirst(allMatches.get(i), "§"+i+"§");
            
        }
        
        //remove excessive space
        String[] parts = instruction.split(" ", 2);
        instruction = parts[0] + " " + parts[1].replaceAll(" ", "");
        

        //put original String back on
        for(int i=0; i<allMatches.size(); i++){
            instruction = instruction.replace("§"+i+"§", allMatches.get(i));
        }
        
        //remove protection
        instruction = instruction.replace("\\§", "§");
        return instruction;
    }
    
    /**
    * Calculate a mathematique and/or boolean expression. (Must only containt number and operator)
    * List of all operator: +,-,*,/,==,!=,<=,>=,!,<,>,&&,||
    * <p>
    * PS: This function is extremely un optimised BUT is extremely resilient to incorrect syntax detection and always give the good result soooooo...
    * @param expression
    * @return resultat in Double
    * @throws NumberFormatException something in the expression can't be calculated of the syntax is incorrect
    */
    private static Double eval(String expression) throws NumberFormatException{

        if(expression == null){
            return 0.0;
        }

        
        expression = expression.replaceAll(" *", ""); //replace all sapce

        //replace boolean by theyre numerical value
        expression = expression.replaceAll("true", "1"); 
        expression = expression.replaceAll("false", "0");

        Double v1Buffer = 0.0; 
        Double v2Buffer = 0.0;
        Pattern pattern;
        Matcher matcher;
        String subEvalBuffer; //buffer that will store a simple calcul between 2 values
        
        
        /*
         * execute all the calcule inside bracket
         */
        pattern = Pattern.compile("\\([^\\)\\(]*\\)");
        matcher = pattern.matcher(expression);
        
        while(matcher.find()){
            subEvalBuffer = matcher.group(0);
            //replace the bracket in the expression by its value
            expression = expression.replaceFirst(subEvalBuffer.replace("(", "\\(").replace(")", "\\)").replace("*", "\\*").replace("+", "\\+"), Parser.eval(subEvalBuffer.substring(1, subEvalBuffer.length()-1)).toString());
            matcher = pattern.matcher(expression);
        }
        
        // ! operator
        pattern = Pattern.compile("!-?\\d+(\\.\\d+)?");
        matcher = pattern.matcher(expression);
        
        while(matcher.find()){
            subEvalBuffer = matcher.group(0);
            v1Buffer = Double.valueOf(subEvalBuffer.split("!")[1]);
            
            expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer == 0 ? 1.0 : 0.0)).toString());
            
            matcher = pattern.matcher(expression);
        }
        
        /*
         * execute * and /
         */
        pattern = Pattern.compile("(-?\\d+(\\.\\d+)?\\*-?\\d+(\\.\\d+)?)|(-?\\d+(\\.\\d+)?/-?\\d+(\\.\\d+)?)"); //getting all calcul
        matcher = pattern.matcher(expression);
        
        while(matcher.find()){
            subEvalBuffer = matcher.group(0); //get the calcule
            
            if(subEvalBuffer.split("\\*").length == 2){
                v1Buffer = Double.valueOf(subEvalBuffer.split("\\*")[0]); //get value on the left side of the operator
                v2Buffer = Double.valueOf(subEvalBuffer.split("\\*")[1]); //get value on the right side of the operator
                expression = expression.replaceFirst(subEvalBuffer.replace("*", "\\*"), ((Double)(v1Buffer*v2Buffer)).toString()); //calulate adn replace the original calcul by its result
            }
            else{
                v1Buffer = Double.valueOf(subEvalBuffer.split("/")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("/")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer/v2Buffer)).toString());
                
            }
            
            matcher = pattern.matcher(expression);
            
        }
        
        /*
         * execute + and -
         */
        pattern = Pattern.compile("(-?\\d+(\\.\\d+)?\\+-?\\d+(\\.\\d+)?)|(-?\\d+(\\.\\d+)?--?\\d+(\\.\\d+)?)");
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
        
        /*
         * execute ==, !=, <=, >=, <, >
         */
        pattern = Pattern.compile("(-?\\d+(\\.\\d+)?!=-?\\d+(\\.\\d+)?)|(-?\\d+(\\.\\d+)?==-?\\d+(\\.\\d+)?)|(-?\\d+(\\.\\d+)?>=-?\\d+(\\.\\d+)?)|(-?\\d+(\\.\\d+)?<=-?\\d+(\\.\\d+)?)|(-?\\d+(\\.\\d+)?>-?\\d+(\\.\\d+)?)|(-?\\d+(\\.\\d+)?<-?\\d+(\\.\\d+)?)");
        matcher = pattern.matcher(expression);
        
        while(matcher.find()){
            subEvalBuffer = matcher.group(0);
            
            if(subEvalBuffer.split("==").length == 2){
                v1Buffer = Double.valueOf(subEvalBuffer.split("==")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("==")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer.doubleValue()==v2Buffer.doubleValue() ? 1.0 : 0.0)).toString());
            }
            else if(subEvalBuffer.split("!=").length == 2){
                v1Buffer = Double.valueOf(subEvalBuffer.split("!=")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("!=")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer.doubleValue()!=v2Buffer.doubleValue() ? 1.0 : 0.0)).toString());
            }
            else if(subEvalBuffer.split(">=").length == 2){
                v1Buffer = Double.valueOf(subEvalBuffer.split(">=")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split(">=")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer.doubleValue()>=v2Buffer.doubleValue() ? 1.0 : 0.0)).toString());
            }
            else if(subEvalBuffer.split("<=").length == 2){
                v1Buffer = Double.valueOf(subEvalBuffer.split("<=")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("<=")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer.doubleValue()<=v2Buffer.doubleValue() ? 1.0 : 0.0)).toString());
            }
            else if(subEvalBuffer.split(">").length == 2){
                v1Buffer = Double.valueOf(subEvalBuffer.split(">")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split(">")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer.doubleValue()>v2Buffer.doubleValue() ? 1.0 : 0.0)).toString());
            }
            else {
                v1Buffer = Double.valueOf(subEvalBuffer.split("<")[0]);
                v2Buffer = Double.valueOf(subEvalBuffer.split("<")[1]);
                expression = expression.replaceFirst(subEvalBuffer, ((Double)(v1Buffer.doubleValue()<v2Buffer.doubleValue() ? 1.0 : 0.0)).toString());
            }
            
            matcher = pattern.matcher(expression);
            
        }
        
        /*
         * execute && and ||
         */
        pattern = Pattern.compile("(-?(\\d+(\\.\\d+)?)\\|\\|-?\\d+(\\.\\d+)?)|(-?\\d+(\\.\\d+)?&&-?\\d+(\\.\\d+)?)");
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
     * Calculate a mathematique and/or boolean expression.
     * List of all operator: +,-,*,/,==,!=,<=,>=,!,<,>,&&,||
     * @param expression
     * @param definedVariables list of variable that can be inside the expression
     * @return result
     * @throws NumberFormatException something in the expression can't be calculated of the syntax is incorrect
     */
    public static Double eval(String expression, List<Variable> definedVariables) throws NumberFormatException{
        if(expression == null){
            return 0.0;
        }
        
        Matcher variableMatcher;
        variableMatcher = Pattern.compile("([A-Z]|[a-z])[A-Za-z0-9]*").matcher(expression);
        int indexOfVariable = 0;

        //replace every variable by their respective value
        while (variableMatcher.find()) {

            //ignore true and false
            if(variableMatcher.group(0).matches("true|false")){
                continue;
            }

            if( definedVariables.contains( new Variable(variableMatcher.group(0)))){
                
                indexOfVariable = definedVariables.indexOf(new Variable(variableMatcher.group(0))); 
                
                //no operation is permitted on string
                if(definedVariables.get(indexOfVariable) instanceof VariableString){
                    throw new NumberFormatException("Evaluation on String is prohibited");
                }
                
                //replace the var by its value
                expression = expression.replaceFirst(definedVariables.get(indexOfVariable).getName(), definedVariables.get(indexOfVariable).getValue().toString());
                
            }
            else{
                throw new NumberFormatException("Unknown variable: '"+variableMatcher.group(0)+"'");
            }

            variableMatcher = Pattern.compile("([A-Z]|[a-z])[A-Za-z0-9]*").matcher(expression);

        }

        return eval(expression);
    }
    
    /**
    * Get the values of arguments pass to a command and encapsulated them inside Variable sub-class
    *
    * @param rawArguments argument as a String separated by a coma
    * @param definedVariables List of all variables that are defined
    * @return List of all the value passed stored inside Variable sub class of the right type
    * @throws SyntaxError an argument can't be calculated of the syntax is incorrect
    */
    public static List<Variable> getValueFromArgument(String rawArguments, List<Variable> definedVariables) throws SyntaxError{

        

        List<Variable> argumentList = new ArrayList<Variable>(); //list of all final arguments

        //no argument
        if(rawArguments.length() == 0){
            return argumentList;
        }

        

        List<String> allMatches = new ArrayList<String>(); //store all the litteral strings

        //protection against trick from someone who want to break the code
        rawArguments = rawArguments.replace("§", "\\§");
        Matcher stringMatcher = Pattern.compile("((\"[^\"]*\"*[^\"]*\" *,)|(\"[^\"]*\"*[^\"]*\"))|(('[^']*'*[^']*' *,)|('[^']*'*[^']*'))|(#([a-fA-F0-9]{6}))|([0-9]*.?[0-9]*)%").matcher(rawArguments);

        //match all existing expression of litteral string
        while (stringMatcher.find()) {
            allMatches.add(stringMatcher.group());
        }
        
        /*
         *  replace all string by so theyre values arent modified when variables while be replace by theyre value and
         *  it doesnt interfer for when rawArguments whill be split at every , 
         */
       
        for(int i=0; i<allMatches.size(); i++){
            
            if( allMatches.get(i).charAt( allMatches.get(i).length()-1 ) == ','){
                //regex overflow on the , so it is added back and remove from the lietteral string
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

        //replace every variable by their respective value
        while (variableMatcher.find()) {
            
             //ignore true and false
            if(variableMatcher.group(0).matches("true|false")){
                continue;
            }
    
            if( definedVariables.contains( new Variable(variableMatcher.group(0)))){
                
                indexOfVariable = definedVariables.indexOf(new Variable(variableMatcher.group(0)));
                
                
                if(definedVariables.get(indexOfVariable) instanceof VariableString){
                    //string value recieve the same traitemetn as litteral string
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

        String[] rawArgumentsSplited = rawArguments.split(","); //split at every , to get each argument separetly

        for(int i=0; i<rawArgumentsSplited.length; i++){

            
            if(rawArgumentsSplited[i].matches("-?\\d+(\\.\\d+)?")){
                //store literal number
                argumentList.add( new VariableNumber(Double.parseDouble(rawArgumentsSplited[i])) );
            }
            else if(rawArgumentsSplited[i].matches("true|false")){
                //store literal boolean
                argumentList.add( new VariableBoolean(rawArgumentsSplited[i].equals("true")) );
            }
            else if(rawArgumentsSplited[i].matches("§\\d+§")){  //String are still a tag
                //store literal string

                int value = Integer.valueOf(rawArgumentsSplited[i].substring(1, rawArgumentsSplited[i].length() - 1));
                rawArgumentsSplited[i] = rawArgumentsSplited[i].replace("§"+value+"§", allMatches.get(value)).replace("\\§", "§"); //get the actual value of the string from its tag

                StringBuilder sb = new StringBuilder(rawArgumentsSplited[i]);

                if(!rawArgumentsSplited[i].matches("(#([a-fA-F0-9]{6}))|([0-9]*.?[0-9]*)%")){
                    //remove quotes if needed
                    sb.deleteCharAt(rawArgumentsSplited[i].length() - 1);
                    sb.deleteCharAt(0);
                }
                
                argumentList.add( new VariableString(sb.toString()));
            }
            else{
                //if not literal value, compute the expresionn to get the value
                rawArgumentsSplited[i] = rawArgumentsSplited[i].replace("\\§", "§"); //remove protection
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

    /**
     * Get all instruction inside a block
     * @param instructions list of all instructions
     * @param startIndex current instruction index
     * @return List of all instruction inside a block, this list containt the instruction that open the block
     */
    public static List<String> getFlowBlock(List<String> instructions, int startIndex){
        List<String> flowInstruction = new ArrayList<String>();
        int openedFlow = 0;
        int i = startIndex;

        //count opening bracket so that a blocks inside the main block are keept
        do{
            if(instructions.get(i).matches("[^\\{]*\\{")){
                openedFlow++;
            }
            if(instructions.get(i).matches("[^\\}]*\\}")){
                openedFlow--;
            }
            flowInstruction.add(instructions.get(i));
            i++;
        }while(openedFlow > 0);


        String lastInstruction = new StringBuilder( flowInstruction.get(flowInstruction.size()-1) ).reverse().toString();
        lastInstruction = lastInstruction.replaceFirst("\\}", ""); //remove final closing bracket
        lastInstruction = new StringBuilder( lastInstruction ).reverse().toString();

        flowInstruction.set(flowInstruction.size()-1, lastInstruction);
        return flowInstruction;
    }
    
}
