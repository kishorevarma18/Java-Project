package executor.threadfactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


public class MyThreadFactory implements ThreadFactory {

    private final String prefix;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    
    public MyThreadFactory(String prefix){
        this.prefix =prefix;
    }
    @Override
    public Thread newThread(Runnable r){
        Thread t = new Thread(r);
        t.setName(prefix+" - "+threadNumber.getAndIncrement());
        t.setDaemon(false);
        t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}