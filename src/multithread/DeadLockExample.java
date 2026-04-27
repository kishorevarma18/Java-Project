package multithread;

class Pen {
    // Student 1 locks the 'pen' object here
    public synchronized void writeWithPenAndPaper(Paper paper){ 
        System.out.println(Thread.currentThread().getName()+" is holding pen and trying to access paper!");
        
        // Student 1 tries to lock the 'paper' object while still holding the 'pen'
        // This causes a deadlock if Student 2 already has the 'paper' lock
        paper.accessPaper(); 
    }

    public synchronized void accessPen(){
        System.out.println(Thread.currentThread().getName()+" accessed pen and completed the");
    }
}

class Paper {
    // Student 2 locks the 'paper' object here
    public synchronized void writeWithPaperAndPen(Pen pen){
        System.out.println(Thread.currentThread().getName()+" is holding paper and trying to access pen!");
        
        // Student 2 tries to lock the 'pen' object while still holding the 'paper'
        // This causes a deadlock if Student 1 already has the 'pen' lock
        pen.accessPen(); 
    }

    public synchronized void accessPaper(){
        System.out.println(Thread.currentThread().getName()+" accessed paper and completed the task.");
    }
}

class Student1 implements Runnable {
    private Pen pen;
    private Paper paper;

    public Student1(Pen pen, Paper paper){
        this.pen = pen;
        this.paper = paper;
    }

    @Override
    public void run(){
        // Order: Pen -> Paper
        pen.writeWithPenAndPaper(paper);
    }
}

class Student2 implements Runnable {
    private Pen pen;
    private Paper paper;

    public Student2(Pen pen, Paper paper){
        this.pen = pen;
        this.paper = paper;
    }

    @Override
    public void run(){
        // Order: Paper -> Pen (Opposite of Student 1!)
        paper.writeWithPaperAndPen(pen);
        /*
        //SOLUTION-
        // --Inside Student2 run() method:
        @Override 
        public void run() {
            // --Force Student 2 to lock 'pen' first, even if they call 'paper' methods
            synchronized(pen) {
                paper.writeWithPaperAndPen(pen);
            }
        }
        */
    }
}

public class DeadLockExample {
    public static void main(String[] args) {
        Paper paper = new Paper();
        Pen pen = new Pen();

        /* 
         * DEADLOCK EXPLANATION:
         * 1. Student1 starts and calls synchronized writeWithPenAndPaper (Locks 'pen').
         * 2. Student2 starts and calls synchronized writeWithPaperAndPen (Locks 'paper').
         * 3. Student1 wants 'paper' (held by Student2) to finish.
         * 4. Student2 wants 'pen' (held by Student1) to finish.
         * 
         * Neither can proceed, and neither will release their lock. 
         * The program freezes forever.
         */
        Thread task1 = new Thread(new Student1(pen, paper), "Student1");
        Thread task2 = new Thread(new Student2(pen, paper), "Student2");

        task1.start();
        task2.start();
    }
}
/*
how to resolve deadlocks--

To resolve deadlocks, you essentially need to break one of the four Coffman conditions (Mutual Exclusion, Hold and Wait, No Preemption, and Circular Wait).
In a programming context like Java, here are the most effective strategies:
1. Fixed Lock Ordering (Best for Simple Cases)
Ensure every thread acquires locks in the exact same order.
Rule: If you need Resource A and Resource B, always lock A then B.
Result: This eliminates the Circular Wait condition. One thread will wait for the first lock, preventing it from ever "half-holding" resources.

2. Timed Locking (Using tryLock)
Instead of using synchronized, use ReentrantLock with a timeout.
Logic: If a thread cannot get all required locks within 500ms, it gives up and releases any locks it currently holds.
Result: This breaks the Hold and Wait condition. The thread "backs off" so others can finish.

3. Minimize Lock Scope
Reduce the amount of code inside synchronized blocks.
Logic: Only lock the specific line of code that updates data.
Result: This reduces the "window of opportunity" for a deadlock to occur.

4. Use Higher-Level Concurrency Utilities
Avoid manual locking by using thread-safe classes from java.util.concurrent:
ConcurrentHashMap: Handles its own internal locking.
AtomicInteger: Uses lock-free hardware instructions (Compare-and-Swap).
Semaphore: Limits the number of threads accessing a group of resources.

Strategy||Condition Broken||Java Tool
Lock Ordering||Circular Wait||Standard synchronized
TryLock||Hold and Wait||ReentrantLock.tryLock()
Preemption||No Preemption||Thread.interrupt()
Immutability||Mutual Exclusion||final keyword / Immutable objects
*/