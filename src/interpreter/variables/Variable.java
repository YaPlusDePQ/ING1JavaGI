package interpreter.variables;

/**
* This class create a representation of a variable (it must be used a superclass)
* 
* 
*/
public class Variable {
    
    private String name;
    
    /**
    * Constructor
    *
    * @param  name name for the variable
    */
    public Variable(String name){
        this.name = name;
    }
    
    /**
    * Getter for the name of the variable
    *
    * @return return the name
    */
    public String getName(){
        return this.name;
    }
    
    /**
    * set the name of the variable
    * @param  newName new name
    *
    */
    public void setName(String newName){
        this.name = newName;
    }
    
    /**
    * Getter for the value of the variable
    *
    * @return return the value as an object
    */
    public Object getValue(){
        return null;
    }
    
    /**
    * set the value of the variable
    * @param  newValue new value
    *
    */
    public void setValue(Object newValue){}
    
    /**
    * Override equals to make to variable the same if the have the same name.
    * This must prevent the coexistance of tow variable with the same name but 
    * different type within the same list.
    * @param  newValue new value
    *
    */
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
        return c.getName().equals(this.getName());
    }
    
    @Override
    public String toString(){
        return String.format("name:%s", this.name);
    }
}

