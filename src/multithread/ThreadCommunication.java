package multithread;

/**
 * PRODUCER CLASS
 * Implements Runnable to allow execution in a separate thread.
 * Responsible for generating data and pushing it to the SharedResource.
 */
class Producer implements Runnable {
    private SharedResource resource;

    // Constructor to link the producer to the specific shared resource
    Producer(SharedResource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        // Loop 10 times to produce a sequence of integers
        for (int i = 0; i < 10; i++) {
            resource.producer(i); // Call synchronized method to store data
        }
    }
}

/**
 * CONSUMER CLASS
 * Implements Runnable to allow execution in a separate thread.
 * Responsible for pulling and processing data from the SharedResource.
 */
class Consumer implements Runnable {
    private SharedResource resource;

    // Constructor to link the consumer to the same shared resource as the producer
    Consumer(SharedResource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        // Loop 10 times to match the number of items produced
        for (int i = 0; i < 10; i++) {
            resource.consumer(); // Call synchronized method to retrieve data
        }
    }
}

/**
 * SHARED RESOURCE CLASS
 * The "Monitor" object that coordinates communication between threads.
 * Uses synchronized methods to ensure only one thread accesses the data at a time.
 */
class SharedResource {
    private int data = 0;           // The shared data item
    private boolean hasValue = false; // Flag: true if data is ready to be consumed

    /*
    NOTE-
    we can't use wait(), notify() and notifyAll() inside a method without the method being syncronized or we have to use syncronized blocks.
    */

    /**
     * Called by the Producer thread to store data.
     */
    public synchronized void producer(int value) {
        // While the buffer is full (hasValue is true), the Producer must wait
        while (hasValue) {
            try {
                // wait() releases the lock and pauses the thread
                wait(); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        this.data = value;      // Store the new value
        this.hasValue = true;   // Update flag to signal data is available
        notify();               // Wake up the waiting Consumer thread
        System.out.println("Produced value: " + data);
    }

    /**
     * Called by the Consumer thread to retrieve data.
     */
    public synchronized void consumer() {
        // While the buffer is empty (hasValue is false), the Consumer must wait
        while (!hasValue) {
            try {
                // wait() releases the lock so the Producer can enter
                wait(); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        this.hasValue = false;  // Reset flag to signal buffer is now empty
        notify();               // Wake up the waiting Producer thread
        System.out.println("Consumed value: " + data);
    }
}

/**
 * MAIN EXECUTION CLASS
 * Initializes the shared resource and starts the threads.
 */
public class ThreadCommunication {
    public static void main(String[] args) {
        // Step 1: Create the single shared object instance
        SharedResource resource = new SharedResource();

        // Step 2: Create Thread objects, passing the Runnable tasks and names
        Thread producerThread = new Thread(new Producer(resource), "Producer");
        Thread consumerThread = new Thread(new Consumer(resource), "Consumer");

        // Step 3: Start threads (moves them from NEW to RUNNABLE state)
        producerThread.start();
        consumerThread.start();
    }
}
