package interpreter.variables;

public class Variable {
    
    private String name;

    public Variable(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public Object getValue(){
        return null;
    }

    public void setValue(Object newValue){}

    @Override
    public boolean equals(Object o){
         if (o == this) {
            return true;
        }
 
        if (!(o instanceof Variable)) {
            return false;
        }
         
        Variable c = (Variable) o;
         
        // Compare the data members and return accordingly 
        return c.getName() == this.getName();
    }

    @Override
    public String toString(){
        return String.format("name:%s", this.name);
    }
}

