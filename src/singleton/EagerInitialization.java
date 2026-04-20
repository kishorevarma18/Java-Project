package singleton;

class Singleton{
/*
A Singleton class must:

1. Restrict object creation (usually using private constructor)
2. Ensure only one instance exists
3. Provide a global access point (commonly via static method)
*/
/*
to execute Eager Initialization Singleton class in java we create.
1. static object creation.
2. private constructor.
3. public access  static method to return the object instance.
*/

    private static Singleton instance = new Singleton(); //Creates a single instance at class loading time (Eager Initialization). private ensures it cannot be accessed directly from outside the class
    // private constructor because no class can create a object for this class.
    private Singleton(){
        System.err.println("Singleton created");
    }
    // created this method so any class can access the only object instance created by this class.
    public static Singleton getInstance(){
        return instance;
    }
}

public class EagerInitialization {
    public static void main(String[] args) {
        //Singleton obj1 = new Singleton();  --> complilation error because the constructor is private so we can't access it.
        Singleton obj1 = Singleton.getInstance();
        Singleton obj2 = Singleton.getInstance();

        System.err.println(obj1==obj2);
        System.err.println(obj1.hashCode()+", "+obj1);
        System.err.println(obj2.hashCode()+", "+obj2);
    }
    
}