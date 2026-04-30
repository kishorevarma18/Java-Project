package executor.cachedthreadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import executor.Task;

public class CachedThreadPool {
    public static void main(String[] args) throws InterruptedException {
        /*
         * CACHED THREAD POOL:
         * Created via Executors utility. Unlike a Fixed pool, this has no 
         * upper limit. It creates new threads as needed.
         * 
         * HOW IT WORKS:
         * 1. If a task is submitted and a thread is free, it reuses it.
         * 2. If no threads are free, it creates a BRAND NEW thread.
         * 3. If a thread is idle for 60 seconds, it is terminated and removed.
         */
        ExecutorService executor = Executors.newCachedThreadPool();

        /*
         * These 5 tasks are submitted almost simultaneously. 
         * Since they all start at the same time and the pool is empty, 
         * the CachedThreadPool will likely create 5 distinct threads immediately.
         */
        for(int i = 0; i < 5; i++){
            executor.submit(new Task(i, i * 1000L));
        }

        /*
         * We pause the main thread for 2 seconds.
         * During this time:
         * - Task 0 (0ms) and Task 1 (1000ms) will definitely finish.
         * - Their threads now become "IDLE" and are available for reuse.
         */
        Thread.sleep(2000);

        /*
         * REUSE DEMONSTRATION:
         * When we submit Task 6 here, the pool will look for an idle thread.
         * Since threads from Task 0 or 1 are now free, the pool will likely
         * reuse one of them instead of creating a 6th thread.
         */
        executor.submit(new Task(6));

        // Standard shutdown procedure
        executor.shutdown();

        try {
            /*
             * Note: Task 4 takes 4 seconds and Task 6 takes 1 second (default).
             * Because they run in parallel on separate threads, the 5-second 
             * wait here should be plenty of time for all tasks to finish.
             */
            /* 
            * IMPORTANT: awaitTermination method has THREE possible outcomes:
            * 1. RETURNS 'true': All tasks finished before the timeout.
            * 2. RETURNS 'false': The timeout was reached but tasks are still running.
            *    (This is NOT an exception; it just means "time is up").
            * 3. THROWS InterruptedException: The thread waiting here was 
            *    interrupted by another thread (e.g., app shutdown).
            */
            if(!executor.awaitTermination(5, TimeUnit.SECONDS)){
                executor.shutdownNow();
            }
        } catch(InterruptedException e){
            executor.shutdownNow();
        }
    }
}
