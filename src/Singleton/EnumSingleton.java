package Singleton;

/*
INSTANCE is the only object
No constructor needed (handled internally)
Thread-safe by default
Same object is reused everywhere
*/

enum Test{
    INSTANCE;
    /*
    Enum Declaration -
    enum Test {
        INSTANCE;
    }
    A single object named INSTANCE, JVM guarantees only one instance exists.
    */
    public void showMessage(){
        System.err.println("enum Singleton created");
    }
}


public class EnumSingleton {
    public static void main(String[] args) {
        Test obj1 = Test.INSTANCE;
        /*
        No new keyword, Direct access using enum constant - Test.INSTANCE
        */
        Test obj2 = Test.INSTANCE;
        obj1.showMessage();
        System.err.println(obj1==obj2);
        System.err.println(obj1.hashCode()+", "+obj1);
        System.err.println(obj2.hashCode()+", "+obj2);
    }
}