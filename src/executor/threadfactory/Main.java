package executor.threadfactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ExecutorService;

public class Main {

    public static void main(String[] args) {
        ThreadFactory factory = new MyThreadFactory("DatabaseWorker");

        ExecutorService executor = Executors.newFixedThreadPool(3,factory);
        AtomicInteger taskCount = new AtomicInteger(1);

        for(int i=0;i<10;i++){
            executor.submit(()->{
                System.out.println(Thread.currentThread().getName()+" started task-"+taskCount.getAndIncrement());
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName()+" finished task-"+taskCount.getAndIncrement());
                return 10;
            });
        }
        System.err.println("Main Thread started.");

        executor.shutdown();
    }
}