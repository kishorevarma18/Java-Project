package constructor;

public class ParameterizedConstructor {
    String name;
    int id;

    //Constructor overloading is an object-oriented programming technique where a class has multiple constructors with different parameter lists (type, number, or order). It allows flexible object initialization, enabling objects to be created with default values, specific custom values, or by copying other objects.
    ParameterizedConstructor(){
        System.out.println("default constructor used and default values are assigned.");
        name = "kishore";
        id = 1;
        // we can use default constructor to assign default value when there are no parameters while object creation.
    }

    ParameterizedConstructor(String name, int id) {
        this.name = name; // used this.name to refer to instance variable instead of using parameter like name and id.
        this.id = id;
    }

    public static void main(String[] args) {
        ParameterizedConstructor pc1 = new ParameterizedConstructor("john",120); //parameterized constructor example.
        ParameterizedConstructor pc2 = new ParameterizedConstructor("cena",420);
        ParameterizedConstructor pc3 = new ParameterizedConstructor(); //deafult constructor.
        System.out.println("object pc1 details - name:"+pc1.name+" ,id:"+pc1.id);
        System.out.println("object pc2 details - name:"+pc2.name+" ,id:"+pc2.id);
        System.out.println("object pc3 details - name:"+pc3.name+" ,id:"+pc3.id);
    }
}
