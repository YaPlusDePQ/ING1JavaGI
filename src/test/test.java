package test;

public class test {
    
    public static void main(String[] args) {
        B test;
        if(true){
            A a  = new A();
            test = a.test();
        }
        
        
        System.out.println(test.methode.apply(test));
        test.i = 3;
        System.out.println(test.methode.apply(test));
    }
}
