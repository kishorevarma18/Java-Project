package executor.singlethreadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SingleThreadPool {
    public static void main(String[] args) {
        /*
         * SINGLE THREAD EXECUTOR:
         * Created via the Executors utility class.
         * 
         * HOW IT WORKS:
         * 1. It maintains exactly ONE thread for all tasks.
         * 2. Tasks are executed in a strict sequence (FIFO - First In, First Out).
         * 3. Task 1 must finish completely before Task 2 can even begin.
         * 4. This is ideal for tasks that must happen in a specific order or 
         *    where you want to avoid thread-safety issues (race conditions).
         */
        ExecutorService executor = Executors.newSingleThreadExecutor();

        for(int i = 0; i < 3; i++) {
            /*
             * EFFECTIVELY FINAL RULE:
             * Lambdas capture variables from the surrounding "parent" scope.
             * Java requires these variables to be 'final' or 'effectively final' 
             * (meaning their value never changes after being assigned).
             * 
             * We cannot use 'i' directly inside the lambda because 'i' changes 
             * with every loop (i++). By creating 'int id = i', we create a new 
             * constant-like snapshot for each specific task.
             */
            int id = i; 

            // The lambda () -> { ... } replaces the need for a separate Task class.
            executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Task " + id + " is running on " + threadName);
                
                try {
                    // Because there's only one thread, the total sleep time
                    // for 3 tasks will be exactly 3000ms (3 seconds).
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                
                System.out.println("Task " + id + " is completed on " + threadName);
            });
        }

        // Initiates an orderly shutdown (finishes the 3 tasks in queue).
        executor.shutdown();

        try {
            /*
             * Since tasks run one-by-one, we must ensure the timeout 
             * is long enough to cover the SUM of all task durations.
             * (3 tasks * 1s each = 3s total). 5s is sufficient.
             */
            if(!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Force stop if it takes too long
            }
        } catch(InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
