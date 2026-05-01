package executor.threadpoolexecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import executor.threadfactory.MyThreadFactory;

/**
 * ThreadPoolExecutor Demonstration
 * Logic for 10 tasks:
 * 1. Task 1: Assigned to Core Thread (1).
 * 2. Tasks 2-6: Placed in ArrayBlockingQueue (5 capacity).
 * 3. Tasks 7-10: Queue is full, so 4 "Extra" threads are created (Max Pool reached).
 */
public class ThreadPoolExecutorClass {
    public static void main(String[] args) {
        
        // Using try-with-resources (Java 19+) ensures the executor closes automatically.
        // If on older Java, ignore the try-with-resources and call shutdown() manually.
        try (ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1,                                // corePoolSize: Permanent worker
            5,                                // maximumPoolSize: Max workers allowed
            1,                                // keepAliveTime: Idle time before extra threads die
            TimeUnit.SECONDS,                 // unit: Seconds
            new ArrayBlockingQueue<>(5),      // workQueue: Waiting room for 5 tasks
            new MyThreadFactory("TaskExecutioner") // customFactory: Names our threads
        )) {

            AtomicInteger taskCount = new AtomicInteger(1);
            System.out.println("Main thread execution started..");

            for (int i = 0; i < 10; i++) {
                // We increment the ID here so they are numbered 1-10 in order of submission
                int currentId = taskCount.getAndIncrement();

                executor.execute(() -> {
                    String threadName = Thread.currentThread().getName();
                    System.out.println(threadName + " started executing task-" + currentId);
                    
                    try {
                        // Simulate work (2 seconds)
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    // Check how many tasks are still waiting in the queue
                    int waiting = executor.getQueue().size();
                    System.out.println("Completed task-" + currentId + " | Tasks still in queue: " + waiting);
                });
            }

            System.out.println("Main thread has submitted all tasks.");
            
            // shutdown() tells the pool to finish existing tasks but accept no new ones
            executor.shutdown();
        } 
        // Auto-close happens here: program waits for tasks to finish before exiting main
    }
}
/*
If you want to see what happens when the pool breaks, change the loop to i < 11.
You will see an AbortPolicy exception because you only have room for 10 tasks (5 threads + 5 queue slots).
*/