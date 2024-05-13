package interpreter.variables;

public class variableBoolean extends Variable{
    private Boolean value;

    /**
    * Constructor
    *
    * @param  name name for the variable
    * @param  value value for the variable
    */
    public variableBoolean(String name, Boolean value){
        super(name);
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
        return String.format("name:%s\n   value%d\n   type: Boolean", this.getName(), this.value);
    }
}