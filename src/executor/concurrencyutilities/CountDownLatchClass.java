package executor.concurrencyutilities;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchClass {
    public static void main(String[] args) {
        
        // 1. INITIALIZATION: Set the starting count to 2.
        // The "gate" will stay closed until countDown() is called exactly twice.
        CountDownLatch count = new CountDownLatch(2);

        new Thread(() -> {
            System.out.println("Database is starting...");
            try {
                Thread.sleep(500);
                System.out.println("Database is ready!");
            } catch (InterruptedException e) {
                System.out.println("Database error: " + e.getMessage());
                Thread.currentThread().interrupt();
            } finally {
                // 2. SIGNALING (Decrement): 
                // This reduces the count from 2 to 1.
                // It happens in 'finally' so the gate isn't stuck forever if an error occurs.
                count.countDown(); 
            }
        }).start();

        new Thread(() -> {
            System.out.println("Cache is starting...");
            try {
                Thread.sleep(500);
                System.out.println("Cache is ready!");
            } catch (InterruptedException e) {
                System.out.println("Cache error: " + e.getMessage());
                Thread.currentThread().interrupt();
            } finally {
                // 3. SIGNALING (Final Decrement): 
                // This reduces the count from 1 to 0.
                // Once it hits 0, any thread waiting at await() is immediately woken up.
                count.countDown(); 

                //A CountDownLatch cannot be reset. Once the count hits zero, it stays zero.
                //(If you need to reuse it, look at CyclicBarrier)
            }
        }).start();

        System.out.println("Main Thread is waiting for services to start...");

        try {
            // 4. THE GATEKEEPER (await):
            // The thread stops here. 
            // In your code, it only waits for 1 millisecond.
            // Since the workers need 500ms, 'completed' will be FALSE.
            boolean completed = count.await(1, TimeUnit.MILLISECONDS);
            /* 
            * IMPORTANT: This method has THREE possible outcomes:
            * 1. RETURNS 'true': All tasks finished before the timeout.
            * 2. RETURNS 'false': The timeout was reached but tasks are still running.
            *    (This is NOT an exception; it just means "time is up").
            * 3. THROWS InterruptedException: The thread waiting here was 
            *    interrupted by another thread (e.g., app shutdown).
            */
            if (completed) {
                System.out.println("Main Thread setup completed!!");
            } else {
                // 5. TIMEOUT BEHAVIOR:
                // If the time runs out before the count hits 0, 
                // the thread continues execution rather than waiting forever.
                System.out.println("Setup timed out. Not all services started.");
            }
        } catch (InterruptedException e) {
            // This triggers only if the Main Thread itself is interrupted while waiting.
            System.out.println("Main thread was interrupted while waiting!");
            Thread.currentThread().interrupt();
        }
    }
}
