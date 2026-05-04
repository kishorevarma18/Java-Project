package executor.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureRunAsync {
    public static void main(String[] args) {
        // CompletableFuture.runAsync() triggers a task that returns no result (Void).
        // It runs in the background using the ForkJoinPool.commonPool() by default.
        CompletableFuture<Void> futureTask = CompletableFuture.runAsync(() -> {
            System.out.println("Disk Cleaning started..." + Thread.currentThread().getName());
            stimulateSleep(1000L);
            System.out.println("Disk cleaning finished...");
        });

        // .thenRun() is a callback that executes ONLY after the futureTask completes successfully.
        // It takes a Runnable, meaning it doesn't see the result of the previous task 
        // (which is fine here since runAsync returns Void anyway).
        futureTask.thenRun(() -> {
            System.out.println("Informed administration that disk is cleaned!");
        });

        // This line proves the non-blocking nature: Main thread continues immediately 
        // while the "Disk Cleaning" task is still sleeping/working.
        System.out.println("Main thread executing..." + Thread.currentThread().getName());
        
        // We must sleep the main thread here because CompletableFuture uses daemon threads.
        // If the main thread ends, the background tasks are killed instantly.
        stimulateSleep(2000L);
        System.out.println("Main thread executed!");
    }

    static void stimulateSleep(long time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
            /* The primary difference lies in readability and input safety, as both methods 
               functionally perform the same operation by pausing the current thread and 
               requiring an InterruptedException to be caught. While Thread.sleep() is 
               restricted to milliseconds or nanoseconds, TimeUnit.MILLISECONDS.sleep() 
               belongs to a more expressive API that allows you to specify durations in 
               seconds, minutes, or hours without manual math. Additionally, TimeUnit 
               is safer because it ignores negative time values, whereas Thread.sleep() 
               will throw an IllegalArgumentException if passed a value less than zero. */
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
