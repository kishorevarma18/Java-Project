package methods;

import java.util.Scanner;
public class MethodEx {
    String name; 
    /* Instance variable
    located inside class (outside method), Each object gets its own copy and Stored in heap */
    private int age;// even though age has private modifier which cannot be access outside the MethodEx. we can still access it indirectly using checkEligibility method as it is a public method.
    private static Scanner sc = new Scanner(System.in); //we are using static here so it belongs to class instead of object. so by using static, each object have the same scanner.
    /*private Scanner sc = new Scanner(System.in)
    -without static each object has a separate Scanner for it. So, this creates lots of memory and it's hard to close the scanner after use.
    -if 1 object is using a scanner and if it is closed then another object can't use the scanner. as the once the scanner is close it won't reopen the system.in while running. 
    
    -Shared by all objects, Only one copy exists and Stored in method area.
    */
    
    void checkEligibility(){
        System.out.println("method is called");
        int eligibleAge = 25;
        /* local varibale-
        located inside method, No default value and Stored in stack.
         */
        if(age>eligibleAge)
            System.out.println(name+" is eligible to drink.");
        else
            System.out.println(name+" not eligible to drink.");
    }// method example.

    MethodEx(){
        System.out.println("Constructor called.");
        System.out.println("enter user name:");
        name = sc.nextLine();
        System.out.println("enter user age:");
        age = sc.nextInt();
        sc.nextLine(); // used this here to remove /n which was left from nextInt(). 
        //for explanation plz refer oneNote.
    }// constructor example.

    public static void main(String[] args) {
        MethodEx user1 = new MethodEx();
        MethodEx user2 = new MethodEx();
        user1.checkEligibility();
        user2.checkEligibility();
        sc.close();
        /*
        sc.close(); this shows error as shown below if the static is provide for scanner.
        Exception in thread "main" java.lang.Error: Unresolved compilation problem: 
        Cannot make a static reference to the non-static field sc
        */
    }
}
