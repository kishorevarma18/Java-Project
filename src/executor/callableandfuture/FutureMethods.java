package executor.callableandfuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureMethods {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // submit() returns a Future object immediately as a "promise"
        Future<String> searchFuture = executor.submit(() -> {
            Thread.sleep(1000);
            return "secret_file.txt";
        });

        /*
         * 1. isDone():
         * Returns true if the task completed, failed, or was cancelled.
         * We use it here to keep the main thread busy while the background task works.
         */
        while (!searchFuture.isDone()) {
            System.out.println("Still searching the file in other folders..");
            try {
                Thread.sleep(500); // Slows loop to prevent instant cancellation
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            /*
             * 2. cancel(boolean mayInterruptIfRunning):
             * Attempts to stop the task. 
             * 'true' sends an interrupt signal to the thread (stopping Thread.sleep).
             * Once cancelled, the task is considered "done".
             */
            if (System.currentTimeMillis() % 2 == 0) {
                searchFuture.cancel(true);
                break;
            }
        }

        try {
            /*
             * 3. isCancelled():
             * Checks if the task was terminated via the cancel() method.
             * It is safe to check this before calling get() to avoid exceptions.
             */
            if (!searchFuture.isCancelled()) {
                /*
                 * 4. get(long timeout, TimeUnit unit):
                 * Waits for the result but only for a specific time limit.
                 * If the time expires before the result is ready, it throws TimeoutException.
                 * (Standard get() without parameters would wait forever).
                 */
                String result = searchFuture.get(1, TimeUnit.SECONDS);
                System.out.println("found file: " + result);
            } else {
                System.out.println("Search cancelled by user");
            }
        } catch (TimeoutException | ExecutionException e) {
            // ExecutionException triggers if the code inside the lambda crashed.
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            // Triggers if the main thread is interrupted while waiting at get().
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }
    }
}
