package interpreter.variables;

/**
 * Store a Double value, can be converted to int
 */
public class VariableNumber extends Variable{
    
    private Double value;

    /**
    * Constructor
    *
    * @param  name name for the variable
    * @param  value value for the variable
    */
    public VariableNumber(String name, double value){
        super(name);
        this.value = value;
    }

    /**
    * Constructor
    *
    * @param  value value for the variable
    */
    public VariableNumber(double value){
        super("");
        this.value = value;
    }

    /**
    * Getter for the value of the variable
    *
    * @return return the value as an Integer
    */
    public Double getValue(){
        return this.value;
    }

    
    /**
    * Getter for the value of the variable
    *
    * @return return the value as an Integer
    */
    public Integer getValueInt(){
        return Integer.valueOf((int)Math.round(this.value));
    }


    /**
    * set the value of the variable
    * @param  newValue new value
    *
    */
    public void setValue(Double newValue){
        this.value = newValue;
    }

    @Override
    public String toString(){
        if(this.getName() != ""){
            return String.format("[name: '%s'   value: %f   type: Number]", this.getName(), this.value);

        }else{
            return String.format("[value: %f   type: Number]", this.value);
        }
    }
}
