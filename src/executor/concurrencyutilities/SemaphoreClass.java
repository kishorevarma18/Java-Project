package executor.concurrencyutilities;

import java.util.concurrent.Semaphore;
/*
a Semaphore is a synchronization tool that controls access to a shared resource by maintaining a set of permits.
Unlike a standard lock that allows only one thread at a time, a semaphore can allow a specific number of threads to access a resource concurrently.
How It Works-
Permit Counter: You initialize the semaphore with a fixed number of permits.
acquire(): A thread calls this to request a permit. If available, the count decreases, and the thread proceeds.
If the count is zero, the thread blocks until a permit is released.
release(): When a thread finishes using the resource, it calls this to return the permit, incrementing the count and potentially unblocking a waiting thread.
*/

/*
Semaphore(int permits): Constructor to set the initial number of permits.
Semaphore(int permits, boolean fair): If fair is true, the semaphore grants permits in the order they were requested (FIFO).
acquire(): Blocks the thread until a permit is available.
tryAcquire(): A non-blocking method that returns true if a permit was available and false otherwise.
availablePermits(): Returns the current number of free permits.
*/
public class SemaphoreClass {
    // 1. Initialize semaphore with 3 permits (3 printers)
    Semaphore printers = new Semaphore(3);

    class Employee extends Thread {
        private String name;

        Employee(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                System.out.println(name + " is waiting for a printer...");
                
                // 2. Request a permit
                printers.acquire(); 
                
                System.out.println(name + " starts printing.");
                Thread.sleep(2000); // Simulate printing time
                
                System.out.println(name + " is finished.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 3. Release permit so others can use it
                printers.release(); 
            }
        }
    }

    public static void main(String[] args) throws InterruptedException{
        // 4. Fire off 5 employees at once
        SemaphoreClass semaphoreClass = new SemaphoreClass();
        for (int i = 1; i <= 5; i++) {
            semaphoreClass.new Employee("Employee-" + i).start();
            Thread.sleep(500);
        }
    }
}
