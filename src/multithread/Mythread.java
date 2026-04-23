package multithread;

public class Mythread extends Thread{
    

    @Override
    public void run() {
        System.out.println("RUNNING");
        try{Thread.sleep(5000);}// thread-0 wait for 5 secs.
        catch(InterruptedException e){System.out.println(e.getStackTrace());}
    }

    public static void main(String[] args) throws InterruptedException {
        Mythread t1 = new Mythread();
        System.out.println(t1.getState()); //NEW
        t1.start();
        System.out.println(t1.getState()); //RUNNABLE
        Thread.sleep(3000);// main thread will wait for 3 secs.
        System.out.println(t1.getState()); //TIMED_WAITING
        t1.join();// main thread is waiting untill the thread-0 is completed.
        System.out.println(t1.getState()); //TERMINATED
    }
}