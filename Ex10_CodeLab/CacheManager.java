package test;

import java.util.HashSet;

public class CacheManager {
    private final CacheReplacementPolicy crp;
    private final HashSet<String> cacheSet;
    private final int size;

    public CacheManager(int size, CacheReplacementPolicy crp) {
        this.size = size;
        this.crp = crp;
        this.cacheSet = new HashSet<>();
    }

    public boolean query(String word) {
        // Return true if the word is in the cache, false otherwise
        return cacheSet.contains(word);
    }

    public void add(String word) {
        // If the cache already contains the word, just update its status in the CRP
        // Otherwise, add the word to the cache and update the CRP
        if (!cacheSet.contains(word)) {
            if (cacheSet.size() >= size) {
                // If adding this word exceeds the cache size, remove one according to the CRP
                String removedWord = crp.remove();
                if (removedWord != null) {
                    cacheSet.remove(removedWord);
                }
            }
            cacheSet.add(word);
        }
        crp.add(word); // Update the CRP with the new word or its refreshed status
    }
}
