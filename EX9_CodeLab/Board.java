//Asaf Benor
package test;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int BOARD_SIZE = 15;
    private Tile[][] board;
    private int[][] letterMultipliers;
    private int[][] wordMultipliers;
    private int tileCount = 0; // Tracks the number of tiles placed on the board
    
    

    public Board() {
        this.board = new Tile[BOARD_SIZE][BOARD_SIZE];
        this.letterMultipliers = new int[BOARD_SIZE][BOARD_SIZE];
        this.wordMultipliers = new int[BOARD_SIZE][BOARD_SIZE];
        initializeMultipliers(); // Ensure this is called after initializing the arrays
    }
    
    
    public int size() {
        return tileCount;
    }


    private void initializeMultipliers() {
        // Set all multipliers to 1 by default
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                letterMultipliers[i][j] = 1;
                wordMultipliers[i][j] = 1;
            }
        }

        // Triple word score locations
        int[][] tripleWordScores = {{0,0}, {0,7}, {0,14}, {7,0}, {14,0}, {14,7}, {14,14}, {7,14}};
        for (int[] loc : tripleWordScores) {
            wordMultipliers[loc[0]][loc[1]] = 3;
        }
        
        // Double word score locations
        int[][] doubleWordScores = {{7,7}, {1,1}, {2,2}, {3,3}, {4,4}, {4,10}, {3,11}, {2,12}, {1,13}, {13,1}, {12,2}, {11,3}, {10,4}, {13,13}, {12,12}, {11,11}, {10,10}};
        for (int[] loc : doubleWordScores) {
            wordMultipliers[loc[0]][loc[1]] = 2;
        }
        
        // Triple letter score locations
        int[][] tripleLetterScores = {{1,5}, {1,9}, {5,1}, {5,5}, {5,9}, {5,13}, {9,1}, {9,5}, {9,9}, {9,13}, {13,5}, {13,9}};
        for (int[] loc : tripleLetterScores) {
            letterMultipliers[loc[0]][loc[1]] = 3;
        }
        
        // Double letter score locations
        int[][] doubleLetterScores = {{0,3}, {0,11}, {2,6}, {2,8}, {3,0}, {3,7}, {3,14}, {6,2}, {6,6}, {6,8}, {6,12}, {7,3}, {7,11}, {8,2}, {8,6}, {8,8}, {8,12}, {11,0}, {11,7}, {11,14}, {12,6}, {12,8}, {14,3}, {14,11}};
        for (int[] loc : doubleLetterScores) {
            letterMultipliers[loc[0]][loc[1]] = 2;
        }
    }
    
    
    

    public Tile[][] getTiles() {
        Tile[][] tilesCopy = new Tile[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                tilesCopy[i][j] = board[i][j]; // Assuming Tile is immutable
            }
        }
        return tilesCopy;
    }
    
    
    

    // Checks if the placement of a word is legal on the board
    public boolean boardLegal(Word word) {
        boolean usesExistingTile = false;
        boolean isAdjacentToExistingTile = false;
        int rowStart = word.getRow();
        int colStart = word.getCol();
        Tile[] tiles = word.getTiles();
        boolean vertical = word.isVertical();
        
        
        
        // Check if the starting position is within the board bounds
        if (rowStart < 0 || rowStart >= BOARD_SIZE || colStart < 0 || colStart >= BOARD_SIZE) {
            return false; // Starting position is out of bounds
        }
        
        // Check if the word fits within the board dimensions
        if (vertical && (rowStart + tiles.length > BOARD_SIZE) || 
            !vertical && (colStart + tiles.length > BOARD_SIZE)) {
            return false; // Word doesn't fit on the board
        }

        for (int i = 0; i < tiles.length; i++) {
            int row = vertical ? rowStart + i : rowStart;
            int col = vertical ? colStart : colStart + i;

            // Check if placing a word on an empty board aligns with the center tile for the first word
            if (size() == 0) {
                if (row == 7 && col == 7) {
                    usesExistingTile = true;
                }
            }

            // If the tile position is already occupied
            if (board[row][col] != null) {
                // Special handling for underscore tiles
                if (tiles[i].letter == '_') {
                    // If it's an underscore, treat it as using an existing tile without replacing it.
                    usesExistingTile = true;
                } else if (!board[row][col].equals(tiles[i])) {
                    return false; // Attempting to replace an existing tile with a different one
                } else {
                    usesExistingTile = true; // Word uses an existing tile
                }
            } else {
                // Check if the tile is next to an existing tile (excluding diagonally)
                if (isTileAdjacentToExistingTile(row, col)) {
                    isAdjacentToExistingTile = true;
                }
            }
        }
        
        // The word must use an existing tile OR be adjacent to at least one existing tile
        return usesExistingTile || isAdjacentToExistingTile;
    }

    
    
    
    

    private boolean isTileAdjacentToExistingTile(int row, int col) {
        // Check tiles to the left, right, above, and below the current position for existing tiles
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (newRow >= 0 && newRow < BOARD_SIZE && newCol >= 0 && newCol < BOARD_SIZE && board[newRow][newCol] != null) {
                return true; // Found an adjacent existing tile
            }
        }
        return false;
    }
    
    
    
    

    
    public boolean dictionaryLegal(Word word) {
    	return true;
    }
    
    
    
    

    // Places a word on the board if the placement is legal
    public int tryPlaceWord(Word word) {
        // Step 1: Check if the word's placement is legal on the board.
        if (!boardLegal(word)) {
            return 0; // The placement is not legal.
        }
        
        // Step 2: Check if the word itself is in the dictionary.
        if (!dictionaryLegal(word)) {
            return 0; // The word is not in the dictionary.
        }
        
        // Step 3: Place the new word's tiles on the board, without overwriting existing tiles.
        placeWordOnBoard(word);
        
        // Step 4: Get all new words formed by this placement, including the word itself.
        List<Word> newWords = getWords(word);
        
        // Step 5: Calculate the total score for all new words.
        int totalScore = 0;
        totalScore+=getScore(word);
        for (Word newWord : newWords) {
            totalScore += getScore(newWord); // Accumulate scores of all new words.
        }
        
        newWords.add(word);
        
        resetMultipliersForCentralTile(newWords);
        
        return totalScore; // Return the total score of all new words formed.
    }
    
    
    
    
    public void resetMultipliersForCentralTile(List<Word> newWords) {
        for (Word word : newWords) {
            Tile[] tiles = word.getTiles(); // Get the tiles for each word
            boolean vertical = word.isVertical();
            int rowStart = word.getRow();
            int colStart = word.getCol();

            for (int i = 0; i < tiles.length; i++) {
                int row = vertical ? rowStart + i : rowStart;
                int col = vertical ? colStart : colStart + i;

                // Check if the current tile is at the central position (7,7)
                if (row == 7 && col == 7) {
                    // Reset multipliers for the central tile position
                    letterMultipliers[row][col] = 1;
                    wordMultipliers[row][col] = 1;
                    break; // No need to continue for this word since only one tile can occupy (7,7)
                }
            }
        }
    }


    
    
    
    
    
    private void placeWordOnBoard(Word word) {
        for (int i = 0; i < word.getTiles().length; i++) {
            if (word.getTiles()[i].letter == '_') continue; // Skip underscore tiles
            
            int row = word.isVertical() ? word.getRow() + i : word.getRow();
            int col = word.isVertical() ? word.getCol() : word.getCol() + i;
            
            if (board[row][col] == null) {
                board[row][col] = word.getTiles()[i];
                tileCount++; // Remember to update tileCount if needed
            }
        }
    }

    
    
    
    public int getScore(Word word) {
        int baseScore = 0;
        int wordMultiplier = 1;
        
        for (int i = 0; i < word.getTiles().length; i++) {
            int row = word.isVertical() ? word.getRow() + i : word.getRow();
            int col = word.isVertical() ? word.getCol() : word.getCol() + i;
            
            // Determine the score to use (existing tile's score for '_', new tile's score otherwise)
            int letterScore = (word.getTiles()[i].letter == '_' && board[row][col] != null) ?
                              board[row][col].score : word.getTiles()[i].score;
            
            // Always apply letter multiplier
            int letterMultiplier = letterMultipliers[row][col];
            baseScore += letterScore * letterMultiplier;
            
            // Always consider word multiplier
            int localWordMultiplier = wordMultipliers[row][col];
            if (localWordMultiplier > 1) {
                wordMultiplier *= localWordMultiplier;
            }
        }
      
        return baseScore * wordMultiplier;
    }

    
    


    // Returns a list of newly created words after a word placement
    public List<Word> getWords(Word word) {
        List<Word> newWords = new ArrayList<>();
        //newWords.add(word); // Add the placed word itself as a new word
        
        // Check each tile of the newly placed word for additional words formed
        for (int i = 0; i < word.getTiles().length; i++) {
        	if (word.getTiles()[i].letter == '_') continue; // Skip underscores
            int row = word.isVertical() ? word.getRow() + i : word.getRow();
            int col = word.isVertical() ? word.getCol() : word.getCol() + i;
            
            // Look for new words formed perpendicular to the placed word
            if (word.isVertical()) {
                // Check horizontally for new words at (row, col)
                Word horizontalWord = findNewWord(row, col, false);
                if (horizontalWord != null) newWords.add(horizontalWord);
            } else {
                // Check vertically for new words at (row, col)
                Word verticalWord = findNewWord(row, col, true);
                if (verticalWord != null) newWords.add(verticalWord);
            }
        }
        return newWords;
    }
    
    
    

    // Helper method to find a new word starting from a specific tile and scanning in a specific direction
    private Word findNewWord(int startRow, int startCol, boolean vertical) {
        List<Tile> wordTiles = new ArrayList<>();
        int rowIncrement = vertical ? 1 : 0;
        int colIncrement = vertical ? 0 : 1;
        int row = startRow, col = startCol;
        
        
        row -= rowIncrement;
        col -= colIncrement;

        // Move backwards to the word's start
        while (row >= 0 && col >= 0 && row < BOARD_SIZE && col < BOARD_SIZE && board[row][col] != null) {
            row -= rowIncrement;
            col -= colIncrement;
        }
        // Adjust to the first tile of the word
        row += rowIncrement;
        col += colIncrement;
        
        //set the first tail of the new word to send in the return
        int sendRow=row;
        int sendCol=col;
        

        // Now collect the tiles forward to the word's end
        while (row < BOARD_SIZE && col < BOARD_SIZE && board[row][col] != null) {
            wordTiles.add(board[row][col]);
            row += rowIncrement;
            col += colIncrement;
        }

        // If we've collected tiles and the resulting word is longer than 1 tile
        if (wordTiles.size() > 1) {
            Tile[] tilesArray = wordTiles.toArray(new Tile[0]);
            Word newWord = new Word(tilesArray, sendRow, sendCol, vertical);
            if (dictionaryLegal(newWord)) {
                return newWord;
            }
        }
        
        return null; // No valid new word found
    }


    
}
