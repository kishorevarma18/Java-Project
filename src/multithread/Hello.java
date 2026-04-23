package multithread;
//creating a thread using implements Runnable and override run() method to run the thread.
public class Hello implements Runnable{

    @Override
    public void run() {
        for(int i=0;i<100;i++){
            System.err.println("Hello");
        }
        
    }
    
}