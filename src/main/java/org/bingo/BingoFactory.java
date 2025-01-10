package org.bingo;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Contains all operations related to the Bingo strip
 */
public class BingoFactory {

    public static final int TICKETS_ON_STRIP = 6;
    public static final int TICKET_ROWS = 3;
    public static final int TICKET_COLUMNS = 9;
    public static final int ALLOWED_NUMBERS_IN_ROW = 5;
    public static final int ALLOWED_SPACES_IN_ROW = 4;
    private static final int FIRST_COLUMN_ALLOWED_DOUBLES = 3;
    private static final int INNER_COLUMN_ALLOWED_DOUBLES = 4;
    private int numbersInRow;
    private int randomColumnIndex;
    private int randomNumberForColumn;
    private final List<Integer> populatedColumns;
    private final List<Integer> unPopulatedColumns;
    private final HashMap<Integer, Integer> doubleOccurrencesPerColumn;
    private static final Random RANDOM = ThreadLocalRandom.current();

    //Map of numbers for insertion into the tickets (key is index of the column, value is the array with numbers for insertion)
    private final Map<Integer, List<Integer>> bingoNumbers;

    public BingoFactory() {
        bingoNumbers = new HashMap<>();
        populatedColumns = new ArrayList<>();
        unPopulatedColumns = new ArrayList<>();
        doubleOccurrencesPerColumn = new HashMap<>();
    }

    /**
     * Generator of the all available numbers for insertion into the bingo tickets (1 - 90)
     */
    private void generateAllBingoNumbers() {
        bingoNumbers.clear();
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
    }

    /**
     * Generator of the one strip which consist from multiple bingo tickets
     *
     * @return multidimensional array which contains multiple bingo tickets and represents the bingo strip
     */
    public int[][][] generateBingoStrip() {
        int[][][] bingoStrip = new int[TICKETS_ON_STRIP][TICKET_ROWS][TICKET_COLUMNS];
        generateAllBingoNumbers();
        doubleOccurrencesPerColumn.clear();

        // Populating first two rows in each ticket, fulfilling all columns, to prevent empty columns in tickets
        insertNumbersIntoFirstTwoLinesOfAllTickets(bingoStrip);

        // Populating last row, third, in each ticket with remaining numbers, starting with numbers from the largest arrays for specific columns
        insertRemainingNumbersInThirdRows(bingoStrip);

        return bingoStrip;
    }

    /**
     * Inserting numbers into first and second row in each ticket
     *
     * @param bingoStrip bingo strip with 6 tickets
     */
    private void insertNumbersIntoFirstTwoLinesOfAllTickets(int[][][] bingoStrip) {
        for (int ticket = 0; ticket < TICKETS_ON_STRIP; ticket++) {
            // Set populated and unpopulated columns for each ticket
            setUnPopulatedColumns();
            populatedColumns.clear();
            for (int row = 0; row < TICKET_ROWS - 1; row++) {
                numbersInRow = 0;
                while (numbersInRow < 5) {
                    setRandomColumnIndex();
                    if ((row == 0 && bingoStrip[ticket][row][randomColumnIndex] == 0) || (row == 1 && bingoStrip[ticket][row][randomColumnIndex] == 0 && (!populatedColumns.contains(randomColumnIndex) || unPopulatedColumns.isEmpty()))) {

                        if (!areDoublesInColumnsOk(bingoStrip, ticket, randomColumnIndex)) {
                            continue;
                        }

                        // Obtaining random number for insert into the ticket
                        randomNumberForColumn = bingoNumbers.get(randomColumnIndex).remove(0);

                        // Inserting number in the column
                        bingoStrip[ticket][row][randomColumnIndex] = randomNumberForColumn;

                        populatedColumns.add(randomColumnIndex);
                        if (!unPopulatedColumns.isEmpty()) {
                            unPopulatedColumns.remove((Integer) randomColumnIndex);
                        }
                        numbersInRow++;
                        increaseDoublesForColumn(bingoStrip, ticket, randomColumnIndex);
                    }
                }
            }
        }
    }

    /**
     * Inserting numbers into third rows in each ticket
     *
     * @param bingoStrip bingo strip with 6 tickets
     */
    private void insertRemainingNumbersInThirdRows(int[][][] bingoStrip) {
        for (int ticket = 0; ticket < TICKETS_ON_STRIP; ticket++) {
            setUnPopulatedColumns();
            numbersInRow = 0;
            populatedColumns.clear();
            while (numbersInRow < 5) {
                setColumnIndexOfNextLongestArrayWithNumbersForTicket();
                randomNumberForColumn = bingoNumbers.get(randomColumnIndex).remove(0);
                bingoStrip[ticket][2][randomColumnIndex] = randomNumberForColumn;
                populatedColumns.add(randomColumnIndex);
                if (!unPopulatedColumns.isEmpty()) {
                    unPopulatedColumns.remove((Integer) randomColumnIndex);
                }
                numbersInRow++;
            }
        }
    }

    /**
     * Setting random column index using array of unpopulated columns indexes or by complete row range if all
     * columns are already populated
     */
    private void setRandomColumnIndex() {
        if (!unPopulatedColumns.isEmpty()) {
            setColumnIndexOfNextLongestArrayWithNumbersForTicket();
        } else {
            randomColumnIndex = RANDOM.nextInt(9);
        }
    }

    /**
     * Increasing number of two values in one ticket specific column range
     * (only restricted number of double repetition is allowed in specific column range, otherwise some columns remain empty)
     *
     * @param bingoStrip strip of bingo tickets
     * @param ticket ticket index
     * @param columnIndex column index
     */
    private void increaseDoublesForColumn(int[][][] bingoStrip, int ticket, int columnIndex) {
        doubleOccurrencesPerColumn.putIfAbsent(columnIndex, 0);
        if (bingoStrip[ticket][0][columnIndex] > 0 && bingoStrip[ticket][1][columnIndex] > 0) {
            doubleOccurrencesPerColumn.put(columnIndex, doubleOccurrencesPerColumn.get(columnIndex) + 1);
        }
    }

    /**
     * Checking if generator already reached maximum number of doubles in specific column range (two values in one specific ticket column)
     *
     * @param bingoStrip strip of bingo tickets
     * @param ticket ticket index
     * @param columnIndex column index
     * @return confirm or deny maximum number of doubles for specific column range
     */
    private boolean areDoublesInColumnsOk(int[][][] bingoStrip, int ticket, int columnIndex) {
        if (doubleOccurrencesPerColumn.get(columnIndex) == null) {
            return true;
        } else if (columnIndex == 0 && doubleOccurrencesPerColumn.get(columnIndex).equals(FIRST_COLUMN_ALLOWED_DOUBLES) && bingoStrip[ticket][0][columnIndex] != 0) {
            return false;
        } else if (columnIndex > 0 && columnIndex < 8 && doubleOccurrencesPerColumn.get(columnIndex).equals(INNER_COLUMN_ALLOWED_DOUBLES) && bingoStrip[ticket][0][columnIndex] != 0) {
            return false;
        } else if (columnIndex == 8) {
            return true;
        }
        return true;
    }

    /**
     * Resetting unPopulatedColumns to all columns
     */
    private void setUnPopulatedColumns() {
        unPopulatedColumns.clear();
        for (int column = 0; column < 9; column++) {
            unPopulatedColumns.add(column);
        }
    }

    /**
     * Obtain next index of the largest array with remaining numbers for specific column, it covers EDGE CASE, when current ticket
     * has already fulfilled column with the numbers from the provided column index
     */
    private void setColumnIndexOfNextLongestArrayWithNumbersForTicket() {
        int longest = bingoNumbers.get(unPopulatedColumns.get(0)).size();
        int indexOfFirstLongest = unPopulatedColumns.get(0);
        for (int columnIndex : unPopulatedColumns) {
            if (bingoNumbers.get(columnIndex).size() > longest) {
                longest = bingoNumbers.get(columnIndex).size();
                indexOfFirstLongest = columnIndex;
            }
        }
        randomColumnIndex = indexOfFirstLongest;
    }
}
