package methods;
/*
we can create n number of instance and static blocks.
but the execution order is from top to bottom for both instance and static blocks.

Static blocks are used for class-level setup and execute only once when the class is loaded—prior to the main method, giving them access only to static members. 
nstance blocks(non-static blocks) provide common setup for every object and execute each time an instance is created, running after super() but before the constructor code with access to both static and non-static members.
 */
public class Blocks {
    static int a=10;
    int b=20;
    Blocks(){
        System.out.println("constructor executed.");
    }
    static{
        System.out.println("static block 2 executed.");
        
        /*
        System.out.println(b);
        Static cannot access non-static directly. since b variable is non-static we can't access directly but we can access it using object creation.
        */
       System.out.println(new Blocks().b);
    }
    {
        System.out.println("instance block 1 executed.");
        System.out.println(a+b);
        // non-static blocks/members can access both static or non-static(instance) variables.
    }
    static{
        System.out.println("static block 1 executed.");
    }
    {
        System.out.println("instance block 2 executed.");
        
    }

    

    void display(){
        System.out.println("method executed.");
    }

    public static void main(String[] args) {
        System.out.println("main method executed.");
        Blocks b = new Blocks();
        b.display();
        System.out.println(a);
        //static varibales can be accessed directly without any object creation.
        System.out.println(b.b);
    }
}
