package interpreter.variables;

public class VariableInt extends Variable{
    
    private Integer value;

    /**
    * Constructor
    *
    * @param  name name for the variable
    * @param  value value for the variable
    */
    public VariableInt(String name, Integer value){
        super(name);
        this.value = value;
    }

    /**
    * Getter for the value of the variable
    *
    * @return return the value as an Integer
    */
    public Integer getValue(){
        return value;
    }

    /**
    * set the value of the variable
    * @param  newValue new value
    *
    */
    public void setValue(Integer newValue){
        this.value = newValue;
    }

    @Override
    public String toString(){
        return String.format("[name: '%s'   value:%d   type: Integer]", this.getName(), this.value);
    }
}
