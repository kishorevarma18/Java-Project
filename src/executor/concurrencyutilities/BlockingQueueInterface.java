package executor.concurrencyutilities;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueInterface {
    public static void main(String[] args) {
        
        /*
        It is a thread-safe queue that allows one thread to put data in and another to take data out, automatically handling the waiting logic.
        It solves two classic problems without you needing to write wait() or notify() manually:
        When it's full: A thread trying to add an item will "block" (stay on hold) until there is space.
        When it's empty: A thread trying to take an item will "block" until something is added.
        */

        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(5);
        /* Common implementation-
        LinkedBlockingQueue- Typically used in Fixed Thread Pools.Can be "unbounded" (infinite size), but you should usually set a capacity to avoid OutOfMemoryError.
        ArrayBlockingQueue- Uses a fixed-size array.Slightly better performance than Linked but cannot be resized.
        SynchronousQueue- Capacity of zero.Every insert must wait for a corresponding take. This is what Executors.newCachedThreadPool() uses to hand off tasks directly to threads.
         */
        // PRODUCER THREAD: The "Patient" Submitter
        Thread producer = new Thread(() -> {
            try {
                for (int i =        1; i <= 10; i++) {
                    System.out.println("Producer : " + i);
                    
                    // When 'i' reaches 6, the queue is full (capacity 5).
                    // This thread will automatically "hang" here and wait.
                    // No manual wait() or notify() required!
                    queue.put(i); 
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(e.getMessage());
            }
        });

        // CONSUMER THREAD: The "Patient" Worker
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    // If the producer is slow and the queue is empty,
                    // this thread sleeps until an item is added.
                    // It uses ZERO CPU while waiting.
                    Integer value = queue.take(); 
                    
                    System.out.println("consumer : " + value);
                    
                    // Stopping the infinite loop
                    if (value == 10) break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(e.getMessage());
            }
        });
        /*
        1. THE "CRASH" WAY (Exceptions)
        Use if you want an immediate error if the queue isn't ready.
        queue.add(i);    Full? -> THROW ERROR
        queue.remove();  Empty? -> THROW ERROR

        2. THE "CHECK" WAY (Special Values)
        Use to "try" an action. If it fails, it just tells you 'false' or 'null'.
        queue.offer(i);  Full? -> RETURN FALSE (doesn't wait)
        queue.poll();    Empty? -> RETURN NULL (doesn't wait)

        3. THE "WAIT" WAY (Blocking) 
        Use for your actual logic. The thread pauses and sleeps until it can finish.
        queue.put(i);    Full? -> PAUSE thread until there is space
        queue.take();    Empty? -> PAUSE thread until an item arrives
        */
        producer.start();
        consumer.start();
    }
}
