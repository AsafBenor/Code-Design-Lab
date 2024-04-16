//Asaf Benor
package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Executes tasks asynchronously, leveraging a single-thread executor
public class Q1a<V> {
    
    // Defines a generic functional interface for task execution
    public interface FunctionalInterface<V> {
        V apply();
    }
    
    // ExecutorService to manage asynchronous task execution
    ExecutorService executorService;
    
    // Initializes the executor service with a single-thread executor
    public Q1a() {
        executorService = Executors.newSingleThreadExecutor();
    }

    // Cleans up executor service resources, stopping task execution
    public void close(){
        executorService.shutdown();
    }
    
    // Submits a user-defined task to the executor service, returning a Future representing the task's result
    public Future<V> threadIt(FunctionalInterface<V> task) {
        
        return executorService.submit(() -> {            
            return task.apply();        
        });
        
    }
    
}
