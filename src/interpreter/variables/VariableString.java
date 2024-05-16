package interpreter.variables;

/**
 * Store a String
 */
public class VariableString extends Variable{
    private String value;

    /**
    * Constructor
    *
    * @param  name name for the variable
    * @param  value value for the variable
    */
    public VariableString(String name, String value){
        super(name);
        this.value = value;
    }

    /**
    * Constructor
    *
    * @param  value value for the variable
    */
    public VariableString(String value){
        super("");
        this.value = value;
    }


    /**
    * Getter for the value of the variable
    *
    * @return return the value as a String
    */
    public String getValue(){
        return value;
    }

    /**
    * set the value of the variable
    * @param  newValue new value
    *
    */
    public void setValue(String newValue){
        this.value = newValue;
    }

    @Override
    public String toString(){
        if(this.getName() != ""){
            return String.format("[name: '%s'   value: %s   type: String]", this.getName(), this.value);

        }else{
            return String.format("[value: %s   type: String]", this.value);
        }
    }
}
