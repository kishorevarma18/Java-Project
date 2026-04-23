package multithread;

public class SyncThread extends Thread{
    Counter counter;
    SyncThread(Counter counter){
        this.counter=counter;
    }
    @Override
    public void run(){
        for(int i=0;i<10000;i++){
            counter.increment();
        }
    }
    public static void main(String[] args) {
        Counter count = new Counter();
        SyncThread t1 = new SyncThread(count);
        SyncThread t2 = new SyncThread(count);

        t1.start();
        t2.start();
        try{
            t1.join();
            t2.join();
        }
        catch(InterruptedException e){
            System.err.println(e);
        }
        /* since both the threads are accessing a single count object the output should be 20000.
        but when we run the value is random and not exepected one.

        The line count++ looks like one step, but the CPU actually does three:
        Read: Fetch the current value of count.
        Add: Increase that value by 1.
        Store: Write the new value back to memory.
        What goes wrong:
        t1 reads count (value is 10).
        t2 reads count (value is also 10).
        t1 increments to 11 and stores it.
        t2 increments to 11 and stores it.
        Result: Even though both threads worked, the count only went up by 1 instead of 2.
        */
        count.getCount();

        /*
        So inorder to solve this problem(due to Race condition)-
        we use syncronized keyword on the increment method in Counter class.
        which stops this race condition and only allows 1 thread at a time to access the method.
        */
    }
}