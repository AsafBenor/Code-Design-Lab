package test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class IOSearcher {
    private AtomicBoolean stopRequested = new AtomicBoolean(false);

    // Searches the given files for the specified word
    public boolean search(String word, String... fileNames) {
        stopRequested.set(false); // Reset stop flag at the beginning of each search

        for (String fileName : fileNames) {
            if (stopRequested.get()) {
                //System.out.println("Search stopped.");
                return false; // Stop the search if stopRequested is true
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (stopRequested.get()) {
                        //System.out.println("Search stopped.");
                        return false; // Stop the search if stopRequested is true
                    }
                    if (line.contains(word)) {
                        //System.out.println("Word \"" + word + "\" found in " + fileName);
                        return true; // Word found
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading from file " + fileName + ": " + e.getMessage());
                // Optionally, you might want to continue searching other files even if one fails to open/read.
            }
        }

        //System.out.println("Word \"" + word + "\" not found in any provided file.");
        return false; // Word not found in any files
    }

    // Method to request stopping the search
    public void stop() {
        stopRequested.set(true);
    }
}

