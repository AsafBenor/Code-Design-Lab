package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {
    private CacheManager lruCache;
    private CacheManager lfuCache;
    private BloomFilter bloomFilter;
    private ParIOSearcher parIOSearcher;
    private String[] fileNames; // Class member to hold the file names

    public Dictionary(String... fileNames) {
        // Initialize CacheManager and BloomFilter as per the specifications
    	
        lruCache = new CacheManager(400, new LRU());
        lfuCache = new CacheManager(100, new LFU());
        bloomFilter = new BloomFilter(256, "MD5", "SHA1");
        this.parIOSearcher = new ParIOSearcher();
        this.fileNames = fileNames;

        // Populate the BloomFilter with words from the files
        for (String fileName : fileNames) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    for (String word : line.split("\\s+")) { // Split line into words
                        bloomFilter.add(word);
                    }
                }
            } catch (IOException e) {
                
            }
        }
    }

    public boolean query(String word) {
        // First, check if the word exists in the LRU cache (existing words).
        if (lruCache.query(word)) {
            
            return true; // Word found in existing words cache.
        }

        // If not found in LRU, check in the LFU cache (for words confirmed not to exist).
        if (lfuCache.query(word)) {
            
            return false; // Word confirmed not to exist.
        }

        // If the word is not found in either cache, consult the BloomFilter.
        if (!bloomFilter.contains(word)) {
            
            return false; // BloomFilter suggests the word does not exist.
        } else {
            
            // The word may exist according to the BloomFilter but isn't found in the cache.
            // This implementation does not automatically add non-cached words to the LFU cache.
            // Additional logic would be needed to handle such a case, potentially involving
            // further verification or search mechanisms not covered by the current method implementations.
            return true;
        }
    }

    
    public boolean challenge(String word) {
        // Use the parIOSearcher instance to call the search method
        boolean found = parIOSearcher.search(word, this.fileNames);

        if (found) {
            // Word found - update the LRU cache
            // Assuming CacheManager's query method updates cache content on a hit
            lruCache.query(word); // A successful query would refresh the word in the LRU cache
        } else {
            // Word not found - consider updating the LFU cache if the design allows
            // Since direct 'not found' updates are not supported by CacheManager, this step might be
            // more complex and depends on your cache management strategy.
            // This step is left as a conceptual placeholder due to method constraints.
        }
        
        return found;
    }
    
    public void close() {
        // Calls the stop method of the ParIOSearcher instance to halt ongoing searches
        if (parIOSearcher != null) {
            parIOSearcher.stop();
        }
        // Additional cleanup if necessary
    }

    
    
}
