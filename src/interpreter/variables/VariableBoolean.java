package interpreter.variables;

/**
 * Store a Boolean value
 */
public class VariableBoolean extends Variable{
    private Boolean value;

    /**
    * Constructor
    *
    * @param  name name for the variable
    * @param  value value for the variable
    */
    public VariableBoolean(String name, Boolean value){
        super(name);
        this.value = value;
    }

    /**
    * Constructor
    *
    * @param  value value for the variable
    */
    public VariableBoolean(Boolean value){
        super("");
        this.value = value;
    }

    /**
    * Getter for the value of the variable
    *
    * @return return the value as a Boolean
    */
    public Boolean getValue(){
        return value;
    }

    /**
    * set the value of the variable
    * @param  newValue new value
    *
    */
    public void setValue(Boolean newValue){
        this.value = newValue;
    }

    @Override
    public String toString(){
        return String.format("[name: '%s'   value: %d   type: Boolean]", this.getName(), this.value);
    }
}
