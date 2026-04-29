package executor.callableandfuture;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import executor.Task;

public class CallableAndFuture {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        /*
         * CALLABLE:
         * An upgraded version of Runnable. It can return a result (Generics)
         * and is allowed to throw checked exceptions without a try-catch inside.
         */
        Callable<Integer> callableTask = () -> {
            System.out.println("callableTask started...");
            Thread.sleep(1000);
            //Callable class itself throws checked exception internally so we don't have to handle.
            return 10 + 20; // Returns a value
        };

        Runnable runnableTask = () -> {
            System.out.println("runnableTask started...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //we have to handle exception manually because it is checked exception.
        };

        System.out.println("submitting the callabletask");
        /*
         * FUTURE:
         * A placeholder for a result that will eventually be available. 
         * submit() returns a Future instantly, allowing the main thread to continue.
         */
        Future<Integer> callableFutureResult = executor.submit(callableTask);

        System.out.println("main Thread is free to do any tasks..");

        System.out.println("submitting the runnabletask");
        /*
         * PASSING RETURN USING RUNNABLE:
         * submit(Runnable, T result) allows a Runnable to return a predefined value.
         * Since Runnable's run() method is void, it cannot calculate a result, 
         * but the Future will return the "Completed" object once the task finishes.
         */
        Future<String> runnableFutureResult = executor.submit(runnableTask, "Completed");

        try {
            /*
             * FUTURE METHODS:
             * .get() - A BLOCKING call. It pauses the main thread until the task is done.
             * .isDone() - (Not used here) Returns true if task finished/failed/cancelled.
             * .cancel() - (Not used here) Attempts to stop a task before it finishes.
             */
            System.out.println("The Result from the callabletask is: " + callableFutureResult.get());
            System.out.println("The Result from the runnabletask is: " + runnableFutureResult.get());

            /*
             * EXECUTE() vs SUBMIT():
             * execute() - Fire and forget. No Future is returned. Best for void tasks.
             * submit() - Returns a Future. Best when you need to track the task or get a result.
             */
            System.out.println("executing a runnableTask1 without any future return.");
            executor.execute(new Task(1, 2000L));

        } catch (InterruptedException e) {
            /*
             * InterruptedException: Thrown if the main thread is interrupted while 
             * waiting at .get().
             */
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            /*
             * ExecutionException: Thrown if the task itself crashed (e.g. 1/0).
             * The actual cause is wrapped inside this exception.
             */
            System.out.println(e.getMessage());
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
