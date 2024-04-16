package test;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRU implements CacheReplacementPolicy {
    private final LinkedHashMap<String, String> accessOrder;

    public LRU() {
    	this.accessOrder = new LinkedHashMap<>(16, 0.75f, true); {
            /*@Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                // This method is no longer directly relevant for controlling capacity,
                // but it's kept if LRU class needs to be used in a standalone context.
                return false; // Capacity checks are handled by CacheManager
            }*/
        };
    }

    @Override
    public void add(String word) {
        // Simultaneously updates access order and adds the item if it's not already present
        accessOrder.put(word, word);
    }

    @Override
    public String remove() {
        // This method needs to remove and return the least recently used item.
        if (!accessOrder.isEmpty()) {
            String leastRecentlyUsed = accessOrder.keySet().iterator().next();
            accessOrder.remove(leastRecentlyUsed);
            return leastRecentlyUsed;
        }
        return null;
    }
}
