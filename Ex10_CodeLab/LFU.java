package test;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;

public class LFU implements CacheReplacementPolicy {
    private final Map<String, Integer> frequencyMap;
    private final PriorityQueue<Map.Entry<String, Integer>> frequencyQueue;
    
    public LFU() {
        this.frequencyMap = new HashMap<>();
        this.frequencyQueue = new PriorityQueue<>(
            Comparator.<Map.Entry<String, Integer>, Integer>comparing(Map.Entry::getValue)
                      .thenComparing(Map.Entry::getKey)
        );
    }

    @Override
    public void add(String word) {
        // Update frequency
        frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        // Rebuild the queue to reflect the updated frequencies
        rebuildFrequencyQueue();
    }

    @Override
    public String remove() {
        // Remove the least frequently used item
        while (!frequencyQueue.isEmpty()) {
            Map.Entry<String, Integer> leastFrequentEntry = frequencyQueue.poll();
            String leastFrequentWord = leastFrequentEntry.getKey();
            // Double-check to handle concurrent modifications
            if (frequencyMap.containsKey(leastFrequentWord) && frequencyMap.get(leastFrequentWord).equals(leastFrequentEntry.getValue())) {
                frequencyMap.remove(leastFrequentWord);
                return leastFrequentWord;
            }
        }
        return null;
    }
    
    private void rebuildFrequencyQueue() {
        frequencyQueue.clear();
        frequencyQueue.addAll(frequencyMap.entrySet());
    }
}
