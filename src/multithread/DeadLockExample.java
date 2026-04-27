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
