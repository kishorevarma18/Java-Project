package generics;

public class GenericsEx {

    public static void main(String[] args) {
        // Creating a Printer object that holds String type data
        Printer<String> strData = new Printer<>("kishore");
        // Creating a Printer object that holds Integer type data
        Printer<Integer> intData = new Printer<>(20);
        printAnyType(strData);
        printAnyType(intData);
        strData.printData();
        intData.printData();
    }
    // Method that can accept Printer of ANY type using wildcard (?)
    public static void printAnyType(Printer<?> input){
        input.printData();
    }
}

class Printer<T>{
    T data;
    Printer(T data){
        this.data = data;
    }
    void printData(){
        System.err.println(data);
    }
}