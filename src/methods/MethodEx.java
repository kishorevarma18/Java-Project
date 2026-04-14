package methods;

import java.util.Scanner;
public class MethodEx {
    private String name; // even though name and age are private which cannot be access outside the MethodEx. we can still access it indirectly using checkEligibility method as it is a public method.
    private int age;
    private static Scanner sc = new Scanner(System.in); //we are using static here so it belongs to class instead of object. so by using static, each object have the same scanner.
    //private Scanner sc = new Scanner(System.in)
    //without static each object has a separate Scanner for it. So, this creates lots of memory and it's hard to close the scanner after use.
    //if 1 object is using a scanner and if it is closed then another object can't use the scanner. as the once the scanner is close it won't reopen the system.in while running. 
    void checkEligibility(){
        if(age>24)
            System.out.println(name+" is eligible to drink.");
        else
            System.out.println(name+" not eligible to drink.");
    }// method example.

    MethodEx(){
        System.out.println("enter user name:");
        name = sc.nextLine();
        System.out.println("enter user age:");
        age = sc.nextInt();
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
