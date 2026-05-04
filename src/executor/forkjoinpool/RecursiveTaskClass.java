package executor.forkjoinpool;

import java.util.concurrent.RecursiveTask;

/**
 * RecursiveTask<Double> is used when your task NEEDS to return a result.
 * Here, we are returning the 'Double' sum of a portion of the array.
 */
public class RecursiveTaskClass extends RecursiveTask<Double> {
    private static final int THRESOLD = 10;
    private Double[] array;
    private int start, end;

    public RecursiveTaskClass(Double[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Double compute() {
        // SEQUENTIAL THRESHOLD:
        // If the array segment is small (<= 10), avoid the overhead of further splitting.
        // We calculate the sum directly using a standard loop.
        if (end - start <= THRESOLD) {
            Double sum = 0.0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum; // Return the local sum to the parent task
        } 
        // DIVIDE AND CONQUER:
        else {
            // Find the midpoint to split the work in half
            int mid = (start + end) / 2;
            
            // Create two sub-tasks
            RecursiveTaskClass left = new RecursiveTaskClass(array, start, mid);
            RecursiveTaskClass right = new RecursiveTaskClass(array, mid, end);

            // 1. fork(): Asynchronously push the 'left' task into the pool's queue.
            // A different thread may "steal" this task to run it.
            left.fork();

            // 2. compute(): Execute the 'right' task in the CURRENT thread.
            // This is an optimization; instead of forking both, we use the current thread for one.
            Double rightResult = right.compute();

            // 3. join(): Wait for the 'left' task to finish and grab its result.
            // If it's not done yet, the current thread may help with other tasks (work-stealing).
            Double leftResult = left.join();

            // AGGREGATION: Combine the results from both halves and return them upward.
            return rightResult + leftResult;
        }
    }
}
