package multithread;
//creating a thread using implements Runnable and override run() method to run the thread.
public class Hello implements Runnable{
// this Runnable class is helpful in situations like when A class extends B class and i want to create a thread for class A --> it is not possible.
// EX- public class A extends B, Thread ---> leads to error!
// solution- public class A extends B implements Runnable   ---> correct way.
    @Override
    public void run() {
        for(int i=0;i<100;i++){
            System.out.println("Hello");
        }
        
    }
    
}