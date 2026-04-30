package executor.callableandfuture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceMethods {
    public static void main(String[] args) {
        // Creates a pool with 3 fixed threads to handle tasks in parallel.
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Using ArrayList to store a collection of Callable tasks.
        List<Callable<String>> tasks = new ArrayList<>();
        /*List<Callable<String>> tasks = new ArrayList<Callable<String>>();
        There is no need to explicitly mention the type in RHS. Java can automatically assign the type by looking at LHS type. 
        */
        tasks.add(() -> {
            Thread.sleep(1000);
            return "Result from Task1 (Slowest)";
        });
        tasks.add(() -> {
            Thread.sleep(750);
            return "Result from Task2 (Medium)";
        });
        tasks.add(() -> {
            Thread.sleep(500);
            return "Result from Task3 (Fastest)";
        });

        try {
            /*
             * INVOKE ANY:
             * 1. Logic: Executes all tasks but returns only the result of the FIRST one to finish successfully.
             * 2. Efficiency: Once Task3 (500ms) finishes, the executor automatically cancels Task1 and Task2.
             * 3. Blocking: The main thread blocks here until at least one task completes.
             */
            System.out.println("Testing invokeAny() --");
            String fastest = executor.invokeAny(tasks);
            System.out.println(fastest);

            /*
             * INVOKE ALL:
             * 1. Logic: Executes all tasks and waits for EVERY task to finish.
             * 2. Blocking: This is a synchronous call; the main thread will stay here until 
             *    the slowest task (Task1 - 1000ms) is 100% complete.
             * 3. Return: It returns a List of Future objects in the same order as the input task list.
             */
            List<Future<String>> results = executor.invokeAll(tasks);

            System.out.println("Testing invokeAll() --");

            // Reversing the list of Futures to process/print results from Task3 to Task1.
            Collections.reverse(results);

            for (Future<String> result : results) {
                /*
                 * result.get(): 
                 * Retrieves the result. Since invokeAll() already blocked until all were done,
                 * these .get() calls will return the values immediately without further waiting.
                 */
                System.out.println(result.get());
            }

        } catch (InterruptedException e) {
            // Thrown if the main thread is interrupted while waiting for the results.
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            // Thrown if the task execution itself failed with an exception.
            System.out.println(e.getMessage());
        } finally {
            /*
             * executor.shutdown():
             * Essential cleanup. It initiates an orderly shutdown where previously 
             * submitted tasks are executed, but no new tasks will be accepted.
             */
            executor.shutdown();
        }
    }
}
