//Asaf Benor
package test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Q1b {
    
    private BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    
    private volatile boolean shouldContinue = true;
    
    public Q1b() {
        // Initiates a thread to process tasks from the queue
        new Thread(() -> {
            while(shouldContinue) {
                try {
                    // Executes the next Runnable, waits if none are available
                    taskQueue.take().run();
                } catch (InterruptedException ignored) {
                    // Handling InterruptedException (e.g., thread interruption during wait)
                }           
            }
            
        }).start();
    }
    
    // Adds a task to the queue for execution
    public void push(Runnable task){
        taskQueue.add(task);
    }
    
    // Signals the processing thread to terminate after current tasks are completed
    public void close(){
        // Adds a special task to the queue that sets the flag to stop processing
        push(() -> shouldContinue = false);
    }
}
