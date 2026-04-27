package multithread;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * THE READ-WRITE LOCK CONCEPT:
 * Standard locks (synchronized) are exclusive: only one thread enters at a time.
 * ReadWriteLock optimizes performance by allowing multiple "Readers" to access
 * data simultaneously, as long as no one is "Writing."
 */
public class ReadWriteLockClass {
    private int counter = 0;
    private int count =0;
    
    // Create the lock pair: one for reading, one for writing.
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    public void increment() {
        // WRITE LOCK: Exclusive access.
        // If a writer holds this, no readers or other writers can enter.
        rwLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " is WRITING...");
            counter++;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public int getCount() {
        // READ LOCK: Shared access.
        // Multiple reader threads can hold this lock at the exact same time.
        // This prevents "Thread Starvation" during heavy read operations.
        rwLock.readLock().lock();
        try {
            count++;
            System.out.println(Thread.currentThread().getName() + " is READING: " + counter+" Total count: "+count);
            return counter;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLockClass app = new ReadWriteLockClass();

        Runnable readTask = () -> {
            for (int i = 0; i < 10; i++) {
                app.getCount();
            }
        };

        Runnable writeTask = () -> app.increment();

        // Three readers can run in parallel without blocking each other.
        Thread t1 = new Thread(readTask, "Reader-1");
        Thread t2 = new Thread(readTask, "Reader-2");
        Thread t4 = new Thread(readTask, "Reader-3");
        
        // One writer will block all readers until it finishes its increment.
        Thread t3 = new Thread(writeTask, "Writer-1");

        t1.start();
        t2.start();
        t4.start();
        
        // Small delay to let readers start before the writer locks them out.
        // Thread.sleep(1); 
        
        t3.start();

        System.out.println("Main thread finished. Final count: " + app.getCount());
    }
}

/*
Concurrency Boost: Multiple readers run simultaneously.
Writer Priority: If a writer asks for the lock, new readers usually wait so the writer isn't "starved."
Safety: Even with many readers, the writer is guaranteed a "clean" state with no interference.

*/