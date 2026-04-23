package multithread;

/*
commonly used thread methods- run(), start(), sleep(), join(), interrupt(), setPriority(), setDaemon(), yield() etc*/
public class ThreadMethods extends Thread{
    // this constructor is used for assigning a name to the thread.
    ThreadMethods(String name){
        super(name);
    }
    @Override
    public void run(){
        System.out.println("running");
        for(int i=0;i<10;i++){
            System.out.println(Thread.currentThread().getName()+" "+i);
            Thread.yield(); // this yield method is willing to give up its current use of a processor to another threads.
        }
        try{Thread.sleep(1000);}
        catch(InterruptedException e){
            System.out.println("Interrupted :" +e);
        }
    }
    public static void main(String[] args) throws InterruptedException{
        ThreadMethods t1 = new ThreadMethods("kira");
        t1.setPriority(MAX_PRIORITY); //sets priority to 10-max, 5-nor and 1-low
        t1.start();
        for(int i=0;i<10;i++){
            System.out.println(Thread.currentThread().getName()+" "+i);
        }
        Thread.sleep(10);
        System.out.println(t1.getName());
        t1.interrupt(); //this method give a hint to stop. but if the thread is doing nothing then the method is ignored.
        System.out.println(t1.getState());
        ThreadMethods t2 = new ThreadMethods("light");
        t2.setDaemon(true);// sets this thread as Daemon, which is a background thread. JVM will ignore this daemon threads when the main thread and user threads are completed their tasks.
        t2.start();
    }
}