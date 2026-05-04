package executor.forkjoinpool;

import java.util.concurrent.RecursiveAction;

/**
 * RecursiveAction is a ForkJoinTask that does NOT return a value.
 * It is ideal for "in-place" updates like modifying an array.
 */
public class RecursiveActionClass extends RecursiveAction {
    private static final int THRESOLD = 10;
    private Double[] array;
    private int start, end;

    public RecursiveActionClass(Double[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        // BASE CASE: If the task is small enough, process it sequentially.
        // This prevents the overhead of creating too many threads for tiny tasks.
        if (end - start <= THRESOLD) {
            for (int i = start; i < end; i++) {
                array[i] = array[i] * 1.10; // Direct modification in memory
            }
        } 
        // RECURSIVE STEP: If the task is too large, split it (Divide and Conquer).
        else {
            int mid = (start + end) / 2;
            
            // Create two sub-tasks to handle the left and right halves of the current range.
            RecursiveActionClass left = new RecursiveActionClass(array, start, mid);
            RecursiveActionClass right = new RecursiveActionClass(array, mid, end);

            /*
             * invokeAll() is the core magic of ForkJoinPool:
             * 1. It 'forks' the tasks (submits them to the pool).
             * 2. It triggers the 'work-stealing' algorithm. If a thread is idle,
             *    it will "steal" one of these sub-tasks to keep the CPU busy.
             * 3. It blocks until both sub-tasks are complete.
             */
            invokeAll(left, right);
        }
    }
}
