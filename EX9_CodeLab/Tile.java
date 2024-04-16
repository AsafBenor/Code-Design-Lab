//Asaf Benor
package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Objects;

public class Tile {
	    public final char letter;
	    public final int score;

	    // Private constructor
	    private Tile(char letter, int score) {
	        this.letter = letter;
	        this.score = score;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof Tile)) return false;
	        Tile tile = (Tile) o;
	        return letter == tile.letter && score == tile.score;
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(letter, score);
	    }

    // Inner Bag class
	    public static class Bag {
	        private final ArrayList<Tile> tiles = new ArrayList<>();
	        private final Random random = new Random();

	        private static final Map<Character, Integer> scoreMap = new HashMap<>();
	        static {
	            // Initialize scores for each letter
	            String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_";
	            int[] scores = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10,0};
	            for (int i = 0; i < letters.length(); i++) {
	                scoreMap.put(letters.charAt(i), scores[i]);
	            }
	        }

	        private static final Map<Character, Integer> quantityMap = new HashMap<>();
	        static {
	            // Initialize quantities for each letter
	            String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_";
	            int[] quantities = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1,100};
	            for (int i = 0; i < letters.length(); i++) {
	                quantityMap.put(letters.charAt(i), quantities[i]);
	            }
	        }

	        // A map to keep track of the current quantity of each letter in the bag
	        private final Map<Character, Integer> currentQuantities = new HashMap<>(quantityMap);

	        public Bag() {
	            // Initialize tiles based on the specified quantities and scores
	            for (Map.Entry<Character, Integer> entry : quantityMap.entrySet()) {
	                char letter = entry.getKey();
	                int quantity = entry.getValue();
	                int score = scoreMap.get(letter);
	                for (int i = 0; i < quantity; i++) {
	                    this.tiles.add(new Tile(letter, score));
	                }
	            }
	            Collections.shuffle(this.tiles);
	        }

	        public Tile getRand() {
	            if (this.tiles.isEmpty()) {
	                return null;
	            }
	            Tile tile = this.tiles.remove(random.nextInt(this.tiles.size()));
	            int currentCount = currentQuantities.get(tile.letter);
	            currentQuantities.put(tile.letter, currentCount - 1);
	            return tile;
	        }

	        public void put(Tile tile) {
	            int currentCount = currentQuantities.getOrDefault(tile.letter, 0);
	            if (currentCount < quantityMap.get(tile.letter)) {
	                this.tiles.add(tile);
	                currentQuantities.put(tile.letter, currentCount + 1);
	            }
	            // Else, do not add the tile as it would exceed the initial limit
	        }

	        public Tile getTile(char c) {
	            for (int i = 0; i < this.tiles.size(); i++) {
	                Tile tile = this.tiles.get(i);
	                if (tile.letter == c) {
	                    this.tiles.remove(i);
	                    int currentCount = currentQuantities.get(c);
	                    currentQuantities.put(c, currentCount - 1);
	                    return tile;
	                }
	            }
	            return null;
	        }

	        public int[] getQuantities() {
	            int[] quantities = new int[31];
	            for (Tile tile : this.tiles) {
	                quantities[tile.letter - 'A']++;
	            }
	            return quantities;
	        }
	        
	 


	        public int size() {
	            return this.tiles.size();
	        }
	    }

}
