package test;

public class A{
    private int a;
    public A(){
        a = 0;
    }
    
    public B test(){
        B subject = new B();
        int Target = 3;
        subject.methode = self -> {
            return self.i == Target;
        };
        return subject;
    }
}
