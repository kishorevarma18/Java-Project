package Oops;

class A{
    A(){
        System.err.println("class A");
    }
    A(String name){
        System.err.println("name of the student of class A:"+name);
    }
}

class B extends A{
    B(){
        //super(); is implicitly called by java.
        System.err.println("class B");
    }
    B(String name){
        //super(); is hidden or implicitly created by java and calls A(){} by default.
        
        // we cannot call 2 constructors in 1 constructor. so we can't use super() and this() in one constructor.
        System.err.println("name of the student of class B:"+name);
        this(); // this calls B(){}

        //Note- for detailed explaination check onenote.
    }
}

class C{
    C(String name){
        System.err.println("name of the student of class C:"+name);
    }
}

class D extends C{
    D(String name){
        /*java by default create super() to call the parent class.
        since in the parent class default constructor is not present as we created a parameterized constructor.
        the super() cannot call the deafult constructor. this will cause an error.
        inorder to handle the error, we have to explicitly mention super(name) to call the parent constructor.
        */
        System.err.println("name of the student of class D:"+name);
        super(name);
    }
}
public class Inheritance{
    public static void main(String[] args) {
      new B("kishore");
      new D("varma");
      new B();
    }
}