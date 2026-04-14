package methods;

import java.util.Scanner;
public class MethodEx {
    private String name; // even though name and age are private which cannot be access outside the MethodEx. we can still access it indirectly using checkEligibility method as it is a public method.
    private int age;

    void checkEligibility(){
        if(age>24)
            System.out.println(name+" is eligible to drink.");
        else
            System.out.println(name+" not eligible to drink.");
    }// method example.

    MethodEx(){
        Scanner sc = new Scanner(System.in);
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
    }
}
