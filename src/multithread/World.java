package multithread;
//creating a thread using extends Thread class and override run() method to run the thread.
public class World extends Thread{

    @Override
    public void run() {
        for(int i=0;i<100;i++){
            System.err.println("world");
        }
    }

    
}