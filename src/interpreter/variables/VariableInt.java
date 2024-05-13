package interpreter.variables;

public class VariableInt extends Variable{
    
    private Integer value;

    public VariableInt(String name, Integer value){
        super(name);
        this.value = value;
    }

    public Integer getValue(){
        return value;
    }

    public void setValue(Integer newValue){
        this.value = newValue;
    }

    @Override
    public String toString(){
        return String.format("name:%s\n   value%d\n   type: int", this.getName(), this.value);
    }
}
