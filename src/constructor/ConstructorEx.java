package constructor;

public class ConstructorEx {
    String name;
    int id;

    // constructor
    ConstructorEx() {
        System.out.println("constructor is executed");
        name = "John";
        id = 100;
    }
    public static void main(String[] args) {
        ConstructorEx c = new ConstructorEx(); // constructor is called automatically when an object is created.
        System.out.println(c.name);
    }
}