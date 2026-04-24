package multithread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairnessLock {
    /**
     * UNFAIR LOCK (Default):
     * 'new ReentrantLock()' is the same as 'new ReentrantLock(false)'.
     * In this mode, the lock allows "Barging." If a thread requests the lock 
     * at the exact moment it becomes free, it can grab it even if other 
     * threads have been waiting in line longer.
     */
    public final Lock unfairlock = new ReentrantLock();
    //public final Lock unfairlock = new ReentrantLock(true); --> to unlock fairness.

    public void outerAccessLock() {
        unfairlock.lock(); // Thread enters the "Owner" state
        try {
            System.out.println(Thread.currentThread().getName() + " Outer lock acquired");
            
            /**
             * REENTRANCY:
             * This thread already holds the lock. Because it is a ReentrantLock,
             * it is allowed to enter innerAccessLock() without getting stuck.
             * The "Hold Count" simply goes from 1 to 2.
             */
            innerAccessLock(); 
            
        } finally {
            System.out.println(Thread.currentThread().getName() + " Outer lock released");
            unfairlock.unlock(); // Hold count returns to 0, lock is now truly free
        }
    }

    public void innerAccessLock() {
        unfairlock.lock(); // Increments hold count
        try {
            System.out.println(Thread.currentThread().getName() + " Inner lock acquired");
        } finally {
            System.out.println(Thread.currentThread().getName() + " Inner lock released");
            unfairlock.unlock(); // Decrements hold count
        }
    }

    public static void main(String[] args) {
        FairnessLock lock = new FairnessLock();
        Runnable task = () -> lock.outerAccessLock();

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");
        Thread t3 = new Thread(task, "Thread-3");

        /**
         * THE UNFAIRNESS SCENARIO:
         * 1. T1 starts and holds the lock.
         * 2. T2 and T3 start and wait in a "virtual line" (Queue).
         * 3. T1 releases the lock.
         * 4. In UNFAIR mode, if a NEW Thread-4 started RIGHT NOW, it could
         *    "steal" the lock before the OS has time to wake up T2 or T3.
         * 5. In FAIR mode (new ReentrantLock(true)), the lock would force 
         *    Thread-4 to go to the back of the line.
         */
        t1.start();
        t2.start();
        t3.start();
    }
}


/*
Note-
Even in this situation the fairness won't work sometimes due to the order of threads are assigned by cpu. so it might be random sometimes.
*/