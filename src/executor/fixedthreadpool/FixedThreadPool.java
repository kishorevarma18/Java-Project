package executor.fixedthreadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FixedThreadPool {
    public static void main(String[] args) {
        /* 
         * EXECUTORS (The Factory Utility):
         * Executors is a utility class that provides factory methods to create 
         * different types of thread pools. Instead of manually configuring 
         * complex parameters, we use this class to get a pre-configured pool.
         * 
         * FIXED THREAD POOL (The Strategy):
         * .newFixedThreadPool(2) creates a pool with exactly 2 threads. 
         * If more tasks are submitted than available threads, they are 
         * placed in a queue. This prevents the system from being overwhelmed 
         * by too many active threads.
         */
        ExecutorService executor = Executors.newFixedThreadPool(2);

        /* 
         * EXECUTORSERVICE (The Manager Interface):
         * executor is an instance of ExecutorService. This interface defines 
         * how we interact with the pool. It decouples task submission 
         * from how the tasks actually run.
         */
        executor.submit(new Task(1));
        executor.submit(new Task(2));
        executor.submit(new Task(3));
        executor.submit(new Task(4));
        executor.submit(new Task(5));

        /* 
         * SHUTDOWN:
         * Tells the executor to stop accepting new tasks. The service 
         * remains active until all previously submitted tasks (1-5) finish.
         */
        executor.shutdown();

        try {
            /* 
             * AWAIT TERMINATION:
             * Blocks the main thread for up to 5 seconds, giving background 
             * tasks time to finish. Since 5 tasks run on 2 threads for 1s each, 
             * 3 seconds are needed. 5 seconds is a safe buffer.
             */
            if (!executor.awaitTermination(25, TimeUnit.SECONDS)) {
                // If time runs out, force-stop everything
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            // If the main thread is interrupted while waiting, force-stop
            executor.shutdownNow();
        }
    }
}
