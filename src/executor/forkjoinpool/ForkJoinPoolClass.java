package executor.forkjoinpool;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinPoolClass {
    public static void main(String[] args) {
        // Initialize an array with 100 elements, each set to 100.0
        Double[] array = new Double[100];
        for(int i=0; i<array.length; i++){
            array[i] = 100.0;
        }

        // Create a custom pool. This allocates worker threads based on your CPU cores.
        ForkJoinPool pool = new ForkJoinPool();

        // 1. EXECUTE ACTION: This modifies the array in-place (100.0 -> 110.0)
        // pool.invoke() is a synchronous call that waits for the task to finish.
        pool.invoke(new RecursiveActionClass(array, 0, array.length));

        // Verify the modification (should print 110.0)
        System.out.println(array[0]);

        // 2. EXECUTE TASK: This sums the modified values and returns the total
        // The result is returned directly from the pool's execution.
        System.out.println(pool.invoke(new RecursiveTaskClass(array, 0, array.length)));

        // MANUALLY SHUTDOWN: Explicitly stop the threads to free up system resources.
        pool.close(); 

        // try with resource to automatically close the pool without using .close() 
        /* 
        try(ForkJoinPool pool = new ForkJoinPool()){
            // Inside this block, the 'pool' is active.
            pool.invoke(new RecursiveActionClass(array,0,array.length));
            System.out.println(array[0]);
            System.out.println(pool.invoke(new RecursiveTaskClass(array, 0, array.length)));
        } 
        // Once the curly brace is reached, pool.close() is called automatically by Java.
        */
    }
}
