/*
Use CompletableFuture over the standard Future when you need modern asynchronous programming features.
Unlike Future, which mainly allows submitting a task and blocking with get() to wait for the result, CompletableFuture supports non-blocking operations,
task chaining, built-in error handling, manual completion, and combining multiple asynchronous tasks together.
This makes it much more suitable for scalable and complex workflows where tasks depend on each other or need callbacks after completion.
In general, Future is acceptable for simple legacy background tasks, while CompletableFuture is the preferred choice for modern concurrent applications.
*/
package executor.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureSupplyAsync {
    public static void main(String[] args) {
        boolean state = false;

        // 1. supplyAsync() starts an asynchronous task that RETURNS a value (a String here).
        // It runs on a background thread from the ForkJoinPool by default.
        CompletableFuture<String> futuretask = CompletableFuture.supplyAsync(() -> {
            System.out.println("Establishing database connection...");
            
            // Simulate a delay (network/IO) using your utility method from the other class
            CompletableFutureRunAsync.stimulateSleep(1000L);
            
            if (state) {
                // If an exception occurs, the future completes exceptionally
                throw new RuntimeException("Database connection failed!");
            }
            return "User: Joe"; // This result is passed down the chain
        })
        // 2. .exceptionally() acts like a catch block for the pipeline.
        // If the supplier above fails, this recovers the chain by providing a fallback value.
        .exceptionally(ex -> {
            System.err.println("Handled locally: " + ex.getMessage());
            return "Fallback User"; 
        })
        // 3. .thenApply() transforms the result. 
        // It takes the String from above and returns a new modified String.
        .thenApply(user -> user + " Processed.");

        // 4. .thenAccept() consumes the final result (terminal operation).
        // It does not return a new value; it is usually used for printing or saving data.
        futuretask.thenAccept(result -> System.out.println("Final result: " + result));

        // 5. Non-blocking verification:
        // This will print immediately while the "Database connection" is still sleeping.
        System.out.println("Main Thread executing...");

        // Keep main thread alive. Since background tasks are daemon threads, 
        // they will be terminated if main finishes before they do.
        CompletableFutureRunAsync.stimulateSleep(3000L);
        System.out.println("Main thread exected!");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Something went wrong!");
        });
        try{
            System.out.println(future.get());
        }
        catch(InterruptedException e){
            System.err.println("caused by .get()-"+e.getMessage());
            Thread.currentThread().interrupt();
        }
        catch(ExecutionException e){
            Throwable cause = e.getCause();
            System.err.println("caused by .get()-"+cause.getMessage());
        }

        System.out.println("caused by .join()-"+future.join());
/*
Java introduced CompletableFuture in Java 8 to support modern asynchronous programming with Streams and Lambdas.
Unlike Future.get(), which throws checked exceptions and requires try-catch blocks, CompletableFuture.join() throws an unchecked exception, making it cleaner to use inside lambda expressions and Stream pipelines.
Also, Future is only a basic interface for retrieving results, while CompletableFuture is a full implementation designed for chaining, error handling, and flexible async workflows.
*/
    }
}


/*
for more methods please refer oneNote under multi-threading.
*/