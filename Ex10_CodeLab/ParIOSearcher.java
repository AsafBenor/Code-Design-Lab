package test;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParIOSearcher {
    private ExecutorService threadPool;
    private AtomicBoolean searchCompleted = new AtomicBoolean(false);

    public ParIOSearcher() {
         threadPool = Executors.newCachedThreadPool();
    }

    public boolean search(String word, String... fileNames) {
        searchCompleted.set(false);
        AtomicBoolean found = new AtomicBoolean(false);

        for (String fileName : fileNames) {
            threadPool.submit(() -> {
                IOSearcher searcher = new IOSearcher();
                if (searcher.search(word, fileName)) {
                    found.set(true);
                    // Optionally signal to stop other searches if word is found
                    stop();
                }
            });
        }

        threadPool.shutdown(); // Disallow new tasks
        while (!threadPool.isTerminated()) {
            // Wait for all tasks to complete or be cancelled
        }
        return found.get();
    }

    public void stop() {
        threadPool.shutdownNow(); // Attempt to stop all actively executing tasks
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (!threadPool.isShutdown()) {
                threadPool.shutdownNow();
            }
        } finally {
            super.finalize();
        }
    }
}
