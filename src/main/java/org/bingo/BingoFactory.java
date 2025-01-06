package org.bingo;

import java.util.*;

/**
 * Contains all operations related to the Bingo strip
 */
public class BingoFactory {

    private static final int TICKETS_ON_STRIP = 6;
    private static final int TICKET_ROWS = 3;
    private static final int TICKET_COLUMNS = 9;
    private static final Random RANDOM = new Random();

    //Map of numbers for insertion into the tickets (key is index of the column, value is the array with numbers for insertion)
    private Map<Integer, List<Integer>> bingoNumbers;

    /**
     * Generator of the all available numbers for insertion into the bingo tickets (1 - 90)
     *
     * @return Map of numbers for insertion into the tickets (key is index of the column, value is the array with numbers for insertion)
     */
    public Map<Integer, List<Integer>> generateAllBingoNumbers() {
        bingoNumbers = new HashMap<>();
        List<Integer> listOfNumsInColum;
        for (int i = 0; i < 9; i++) {
            listOfNumsInColum = new ArrayList<>();
            if (i > 0) {
                listOfNumsInColum.add(10 * i); // adding first number
            }
            for (int j = 1; j < 10; j++) {
                listOfNumsInColum.add((10 * i) + j);
            }
            if (i == 8) {
                listOfNumsInColum.add(90);
            }
            bingoNumbers.put(i, listOfNumsInColum);
        }
        return bingoNumbers;
    }

    /**
     * Generator of the one strip which consist from multiple bingo tickets
     *
     * @return multidimensional array which contains multiple bingo tickets and represents the bingo strip
     */
    public int[][][] generateBingoStrip() {
        int[][][] bingoStrip = new int[TICKETS_ON_STRIP][TICKET_ROWS][TICKET_COLUMNS];
        int numbersInRow;
        Set<Integer> populatedColumns = new HashSet<>();
        Set<Integer> unPopulatedColumns = new HashSet<>();
        return bingoStrip;
    }

    /**
     * Insert of the edge columns numbers
     *
     * @param bingoStrip bingo strip
     * @param edgeColumnIndex column index
     * @return multidimensional array with populated edge columns
     */
    private void generateEdgeColumns (int[][][] bingoStrip, int edgeColumnIndex) {

    }

    /**
     * Counting numbers in the bingo row
     *
     * @param bingoRow array of numbers which are currently in the bingo ticket row
     * @return quantity of the numbers inserted into the provided row
     */
    private int countNumbers (int[] bingoRow) {
        int counter = 0;
        for (int column = 0; column < TICKET_COLUMNS; column++) {
            if (bingoRow[column] != 0) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Resetting unPopulatedColumns Set to all columns
     *
     * @param unPopulatedColumns Set of all unpopulated columns
     */
    private void resetUnPopulatedColumns(Set<Integer> unPopulatedColumns) {
        for (int column = 1; column < 8; column++) {
            unPopulatedColumns.add(column);
        }
    }

            /**
             * Obtaining random number in the column range for provided column index
             *
             * @param columnIndex column index from the ticket
             * @return number for insertion in the column (from the column numbers range)
             */
    private int getNumberForColumn(int columnIndex) {
        if (!bingoNumbers.get(columnIndex).isEmpty()) {
            return bingoNumbers.get(columnIndex).remove(0);
        } else {
            return 0;
        }
    }
}
