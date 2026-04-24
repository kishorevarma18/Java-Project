package multithread;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class ReentrantLockClass {
    private int Balance = 1000;
    
    /**
     * REENTRANTLOCK: A manual synchronization tool.
     * Unlike 'synchronized' blocks, you control exactly where the lock starts and ends.
     * It's "Reentrant" because the current owner thread can lock it again without blocking itself.
     */
    public final Lock lock = new ReentrantLock();

    public void deposit(int amount) {
        /**
         * lock(): The "Patience" method.
         * The thread will stop here and wait forever until it gets the lock.
         * Use this for mandatory operations that MUST happen.
         */
        lock.lock(); 
        try {
            if (amount >= 0) {
                Balance += amount;
                System.out.println(Thread.currentThread().getName() + ": amount " + amount + " added. Total: " + Balance);
            } else {
                System.out.println(Thread.currentThread().getName() + ": Negative deposit rejected!");
            }
        } finally {
            /**
             * unlock(): Releases the lock.
             * ALWAYS put this in a 'finally' block. 
             * If the code crashes and you don't call this, the lock stays held forever (Deadlock).
             */
            lock.unlock(); 
        }
    }

    public void withdraw(int amount) {
        try {
            /**
             * tryLock(time, unit): The "Conditional" method.
             * Returns 'true' if it gets the lock within 20ms, 'false' if the time expires.
             * This prevents threads from hanging if the system is too busy.
             */
            if (lock.tryLock(20, TimeUnit.MILLISECONDS)) {
                try {
                    if (amount <= Balance) {
                        Thread.sleep(90); // If enabled, 90ms > 20ms timeout, causing others to fail.
                        Balance -= amount;
                        System.out.println(Thread.currentThread().getName() + ": withdrew " + amount + ". Total: " + Balance);
                    } else {
                        System.out.println(Thread.currentThread().getName() + ": insufficient balance!");
                    }
                } 
                
                finally {
                    /**
                     * Nested unlock(): We only unlock if tryLock successfully returned TRUE.
                     */
                    lock.unlock(); 
                }
            } else {
                /**
                 * Executed only if tryLock() fails.
                 */
                System.err.println(Thread.currentThread().getName() + ": Thread Blocked (Wait timeout expired)!!");
            }
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
            /**
            * WHY INTERRUPT HERE?
            * 1. When InterruptedException is caught, Java clears the 'interrupted' flag.
            * 2. Other parts of the code or a ThreadPool might check this flag to stop safely.
            * 3. By calling interrupt(), we "re-flag" the thread so its status is correct.
            */
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        ReentrantLockClass account = new ReentrantLockClass();

        // Lambda style (Compact)
        Thread t0 = new Thread(() -> account.deposit(500), "Thread-0");

        // Runnable object style (Reusable)
        Runnable task = new Runnable() {
            @Override
            public void run() {
                account.withdraw(500);
            }
        };

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");

        t0.start();
        t1.start();
        t2.start();
    }
}
