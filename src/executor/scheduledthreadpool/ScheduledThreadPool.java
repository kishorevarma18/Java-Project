package executor.scheduledthreadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * EXECUTOR SCHEDULER:
 * Unlike standard executors that run tasks immediately, a ScheduledExecutorService
 * uses an internal timer and a delay-queue to manage tasks based on time.
 * It is the modern, thread-safe replacement for the old 'java.util.Timer'.
 */
public class ScheduledThreadPool {
    public static void main(String[] args) throws InterruptedException {
        // Creates a thread pool that can schedule commands to run after a delay or periodically.
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        /*
         * WHY ATOMICINTEGER FOR LAMBDAS? (The "Effectively Final" Solution):
         * 1. Restriction: Lambdas can only capture local variables that are 'final' or never change.
         * 2. The Problem: A primitive 'int' cannot be incremented (count++) because that changes its value.
         * 3. The Solution: AtomicInteger is an OBJECT. The variable 'count' (the reference) never changes,
         *    so it stays "effectively final." However, the VALUE INSIDE the object can be changed safely.
         * 4. Bonus: It is thread-safe, meaning multiple threads can increment it without errors.
         */
        AtomicInteger count = new AtomicInteger(1);
        AtomicInteger fixedRateCount = new AtomicInteger(1);
        AtomicInteger fixedDelayCount = new AtomicInteger(1);

        /*
         * TYPE 1: One-Shot Schedule
         * Execution: Runs exactly once after the specified delay (3 seconds).
         * Best for: Delayed alerts, timeouts, or single background cleanup tasks.
         */
        executor.schedule(() -> {
            int currentCount = count.getAndIncrement();
            System.out.println("one-shot task executed: " + currentCount + " times");
        }, 3, TimeUnit.SECONDS);

        /*
         * TYPE 2: scheduleAtFixedRate
         * Execution: Starts at the interval (every 1s) regardless of when the previous task finished.
         * Timing: Calculated from the START of task A to the START of task B.
         * Best for: Constant heartbeats or clocks where strict timing is required.
         */
        executor.scheduleAtFixedRate(() -> {
            int currentCount = fixedRateCount.getAndIncrement();
            System.out.println("fixed-rate task executed: " + currentCount + " times");
        }, 1, 1, TimeUnit.SECONDS);

        /*
         * TYPE 3: scheduleWithFixedDelay
         * Execution: Waits for the delay period (3s) AFTER the previous task has completed.
         * Timing: Calculated from the END of task A to the START of task B.
         * Best for: Heavy maintenance tasks where you don't want two tasks running at the same time.
         */
        executor.scheduleWithFixedDelay(() -> {
            System.out.println("cleaning started...");
            int currentCount = fixedDelayCount.getAndIncrement();
            System.out.println("cleaning finished: " + currentCount + " times");
        }, 0, 3, TimeUnit.SECONDS);

        // Keep the main thread alive to allow the schedules to repeat
        Thread.sleep(10000);

        // shutdown() stops the scheduler from creating NEW repetitions of the tasks.
        executor.shutdown();

        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
