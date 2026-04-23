package multithread;
 public class Counter {
    int count=0;
    public void increment(){
        synchronized(this){
            count++;
        }
    }
    /* without synchronized keyword in increment method.
    sometimes all threads access this method at a time and make changes which causes the race condition like the below example.
    or use syncronized blocks like above.
    */
    /*
    public synchronized void increment(){
        count++;
    }
    */
    public void getCount(){
        System.out.println(count);
    }
 }