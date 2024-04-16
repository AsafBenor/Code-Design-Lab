//Asaf Benor
package test;

import java.util.Arrays;
import java.util.Objects;

public class Word {
    private final Tile[] tiles;
    private final int row;
    private final int col;
    private final boolean vertical;

    // Constructor
    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles = Arrays.copyOf(tiles, tiles.length); // Create a copy for immutability
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }

    // Getters
    public Tile[] getTiles() {
        return Arrays.copyOf(tiles, tiles.length); // Return a copy to maintain immutability
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isVertical() {
        return vertical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;
        Word word = (Word) o;
        return row == word.row &&
               col == word.col &&
               vertical == word.vertical &&
               Arrays.equals(tiles, word.tiles); // Use Arrays.equals for array content comparison
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(row, col, vertical);
        result = 31 * result + Arrays.hashCode(tiles); // Compute hash code based on array content
        return result;
    }
}
