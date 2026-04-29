package executor.fixedthreadpool;

/* 
 * RUNNABLE (The Task Definition):
 * This class implements Runnable, which represents the "What" to do.
 * In the ExecutorService model, we separate the logic (Task) 
 * from the execution mechanism (the Thread Pool).
 */
public class Task implements Runnable {
    private int id;

    public Task(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        /* 
         * Inside an ExecutorService, Thread.currentThread() refers to 
         * one of the reused threads managed by the pool, not a new thread 
         * created specifically for this instance.
         */
        String threadName = Thread.currentThread().getName();
        System.out.println("Task " + id + " is running on: " + threadName);

        try {
            // Simulating work
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            /* 
             * INTERRUPT PROPAGATION:
             * Very important! If the ExecutorService calls shutdownNow(), 
             * it sends an interrupt. We catch it here and re-interrupt 
             * the thread so the Executor knows this thread should stop.
             */
            Thread.currentThread().interrupt();
        }
        System.out.println("Task " + id + " is completed on: " + threadName);
    }
}
