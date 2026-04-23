package multithread;
// demonstrating how threads and multithreading works.
public class MainThread {

    public static void main(String[] args) {
        //to start the thread which was created using extends Thread class.
        World w = new World();
        w.start();
        //to start the thread which was created using implements Runnable class.
        Hello h= new Hello();
        Thread t= new Thread(h);
        t.start();
        //starting the main thread by default. anything which is inside main method is run by main thread.
        for(int i=0; i<100;i++){
            System.err.println("Main");
            System.err.println(Thread.currentThread().getName());
        }
    }
}