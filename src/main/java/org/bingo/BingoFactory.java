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
        Set<Integer> populatedColumns = new HashSet<>();
        Set<Integer> unPopulatedColumns = new HashSet<>();

        // Populating first column numbers (1 - 9), to prevent empty columns in tickets
        generateEdgeColumns(bingoStrip, 0);

        // Populating last column numbers (80 - 90), to prevent empty columns in tickets
        generateEdgeColumns(bingoStrip, 8);

        // Populating first two rows in each ticket, fulfilling all columns, to prevent empty columns in tickets, (EDGE CASE: if necessary add number in third row to prevent empty columns in ticket)
        insertNumbersIntoFirstTwoLinesOfInnerColumns(bingoStrip, unPopulatedColumns, populatedColumns);

        // Populating last row, third, in each ticket with remaining numbers, starting with numbers from the largest arrays for specific columns
        insertRemainingNumbersInThirdRows(bingoStrip, unPopulatedColumns, populatedColumns);

        return bingoStrip;
    }

    /**
     * Inserting numbers into first and second row in each ticket
     *
     * @param bingoStrip bingo strip with 6 tickets
     * @param unPopulatedColumns unpopulated columns indexes
     * @param populatedColumns populated columns indexes
     */
    private void insertNumbersIntoFirstTwoLinesOfInnerColumns(int[][][] bingoStrip, Set<Integer> unPopulatedColumns, Set<Integer> populatedColumns) {
        int numbersInRow;
        for (int ticket = 0; ticket < TICKETS_ON_STRIP; ticket++) {
            resetUnPopulatedColumns(unPopulatedColumns);
            populatedColumns.clear();
            for (int row = 0; row < TICKET_ROWS - 1; row++) {
                numbersInRow = countNumbers(bingoStrip[ticket][row]);
                while (numbersInRow < 5 && !bingoNumbers.isEmpty()) {
                    int randomColumnIndex = RANDOM.nextInt(7) + 1;
                    if ((row == 0 && bingoStrip[ticket][row][randomColumnIndex] == 0) || (row == 1 && bingoStrip[ticket][row][randomColumnIndex] == 0 && (!populatedColumns.contains(randomColumnIndex) || unPopulatedColumns.isEmpty()))) {
                        int randomNumberForColumn = getNumberForColumn(randomColumnIndex);
                        if (randomNumberForColumn == 0) {
                            continue;
                        }

                        // Inserting and swapping numbers if necessary
                        if (row == 0) {
                            bingoStrip[ticket][row][randomColumnIndex] = randomNumberForColumn;
                        } else if (randomNumberForColumn < bingoStrip[ticket][0][randomColumnIndex] && bingoStrip[ticket][0][randomColumnIndex] > 0) {
                            bingoStrip[ticket][row][randomColumnIndex] = bingoStrip[ticket][0][randomColumnIndex];
                            bingoStrip[ticket][0][randomColumnIndex] = randomNumberForColumn;
                        } else {
                            bingoStrip[ticket][row][randomColumnIndex] = randomNumberForColumn;
                        }

                        populatedColumns.add(randomColumnIndex);
                        unPopulatedColumns.remove(randomColumnIndex);
                        numbersInRow++;
                    }

                    // Edge case, if in first two rows all columns aren't populated, then number is applied in third row, no need for sort here
                    if (numbersInRow == 5 && row == 1 && !unPopulatedColumns.isEmpty()) {
                        int columnIndex = unPopulatedColumns.stream().findFirst().get();
                        populatedColumns.remove(columnIndex);
                        int randomNumberForColumn = getNumberForColumn(columnIndex);
                        bingoStrip[ticket][2][columnIndex] = randomNumberForColumn;
                        populatedColumns.add(columnIndex);
                        unPopulatedColumns.remove(columnIndex);
                    }
                }
            }
        }
    }

    /**
     * Inserting numbers into third rows in each ticket
     *
     * @param bingoStrip bingo strip with 6 tickets
     * @param unPopulatedColumns unpopulated columns indexes
     * @param populatedColumns populated columns indexes
     */
    private void insertRemainingNumbersInThirdRows(int[][][] bingoStrip, Set<Integer> unPopulatedColumns, Set<Integer> populatedColumns) {
        int numbersInRow;
        for (int row = 0; row < TICKET_ROWS; row++) {
            for (int ticket = 0; ticket < TICKETS_ON_STRIP; ticket++) {
                generateNonPopulatedColumns(bingoStrip[ticket][row], unPopulatedColumns);
                numbersInRow = 9 - unPopulatedColumns.size();
                populatedColumns.clear();
                while (numbersInRow < 5 && !bingoNumbers.isEmpty()) {
                    int randomColumnIndex = getIndexOfFirstLongestArrayWithNumbersForTicket();
                    while (bingoStrip[ticket][row][randomColumnIndex] != 0 || (randomColumnIndex == 0 || randomColumnIndex == 8)) {
                        randomColumnIndex = getIndexOfNextLongestArrayWithNumbersForTicket(unPopulatedColumns);
                    }
                    int randomNumberForColumn = getNumberForColumn(randomColumnIndex);
                    bingoStrip[ticket][row][randomColumnIndex] = randomNumberForColumn;
                    populatedColumns.add(randomColumnIndex);
                    unPopulatedColumns.remove(randomColumnIndex);
                    numbersInRow++;
                }
            }
        }
    }

    /**
     * Resetting unPopulatedColumns Set to all columns
     *
     * @param unPopulatedColumns Set of all unpopulated columns
     */
    private void resetUnPopulatedColumns (Set<Integer> unPopulatedColumns) {
        for (int column = 1; column < 8; column++) {
            unPopulatedColumns.add(column);
        }
    }

    /**
     * Insert of the edge columns numbers
     *
     * @param bingoStrip bingo strip
     * @param edgeColumnIndex column index
     */
    private void generateEdgeColumns (int[][][] bingoStrip, int edgeColumnIndex) {
        int ticketIndex = 0;
        while (!bingoNumbers.get(edgeColumnIndex).isEmpty()) {
            int randomRowIndex = RANDOM.nextInt(3);
            if (bingoStrip[ticketIndex][randomRowIndex][edgeColumnIndex] == 0 && !bingoNumbers.get(edgeColumnIndex).isEmpty()) {
                swapAndInsertNumber(bingoStrip[ticketIndex], randomRowIndex, edgeColumnIndex, getNumberForColumn(edgeColumnIndex));
                ticketIndex++;
            }
            if (ticketIndex == 6) {
                ticketIndex = 0;
            }
        }
    }

    /**
     * Swaps (if necessary) numbers to be sorted in the ascending order in columns and insert number for insertion
     *
     * @param ticket bingo ticket where we will insert new number
     * @param row row for insertion
     * @param randomColumnIndex column index for insertion
     * @param randomNumberForColumn new number for insertion
     */
    private void swapAndInsertNumber(int[][] ticket, int row, int randomColumnIndex, int randomNumberForColumn) {
        // Check if the entire column is empty
        if (ticket[0][randomColumnIndex] == 0 && ticket[1][randomColumnIndex] == 0 && ticket[2][randomColumnIndex] == 0) {
            ticket[row][randomColumnIndex] = randomNumberForColumn;
            return;
        }
        // Helper: Insert and adjust/sort values based on row and column
        switch (row) {
            case 0 -> handleRowZero(ticket, randomColumnIndex, randomNumberForColumn);
            case 1 -> handleRowOne(ticket, randomColumnIndex, randomNumberForColumn);
            case 2 -> handleRowTwo(ticket, randomColumnIndex, randomNumberForColumn);
        }
    }

    /**
     * Helper method to swap numbers if insertion in row 0 is performed
     *
     * @param ticket two-dimensional array for swap and insert
     * @param col column index
     * @param number new number for insert
     */
    private void handleRowZero(int[][] ticket, int col, int number) {
        if (ticket[1][col] > 0 && number > ticket[1][col]) {
            if (ticket[2][col] > 0 && number > ticket[2][col]) {
                ticket[0][col] = ticket[1][col];
                ticket[1][col] = ticket[2][col];
                ticket[2][col] = number;
            } else {
                ticket[0][col] = ticket[1][col];
                ticket[1][col] = number;
            }
        } else if (ticket[2][col] > 0 && number > ticket[2][col]) {
            ticket[0][col] = ticket[2][col];
            ticket[2][col] = number;
        } else {
            ticket[0][col] = number;
        }
    }

    /**
     * Helper method to swap numbers if insertion in row 1 is performed
     *
     * @param ticket two-dimensional array for swap and insert
     * @param col column index
     * @param number new number for insert
     */
    private void handleRowOne(int[][] ticket, int col, int number) {
        if (ticket[2][col] > 0 && number > ticket[2][col]) {
            ticket[1][col] = ticket[2][col];
            ticket[2][col] = number;
        } else if (ticket[0][col] > 0 && number < ticket[0][col]) {
            ticket[1][col] = ticket[0][col];
            ticket[0][col] = number;
        } else {
            ticket[1][col] = number;
        }
    }

    /**
     * Helper method to swap numbers if insertion in row 2 is performed
     *
     * @param ticket two-dimensional array for swap and insert
     * @param col column index
     * @param number new number for insert
     */
    private void handleRowTwo(int[][] ticket, int col, int number) {
        if (ticket[1][col] > 0 && number < ticket[1][col]) {
            if (ticket[0][col] > 0 && number < ticket[0][col]) {
                ticket[2][col] = ticket[1][col];
                ticket[1][col] = ticket[0][col];
                ticket[0][col] = number;
            } else {
                ticket[2][col] = ticket[1][col];
                ticket[1][col] = number;
            }
        } else if (ticket[0][col] > 0 && number < ticket[0][col]) {
            ticket[2][col] = ticket[0][col];
            ticket[0][col] = number;
        } else {
            ticket[2][col] = number;
        }
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
     * Generating non populated columns when fulfilling the last, third, rows in the tickets
     *
     * @param bingoRow array of numbers which are currently in the bingo ticket row
     */
    private void generateNonPopulatedColumns (int[] bingoRow, Set<Integer> unPopulatedColumns) {
        unPopulatedColumns.clear();
        int columnIndex = 0;
        for (int number : bingoRow) {
            if (number == 0) {
                unPopulatedColumns.add(columnIndex);
            }
            columnIndex++;
        }
    }

    /**
     * Obtain index of the longest array with remaining numbers for specific column
     *
     * @return index of the longest array with numbers for ticket, which is also column index in the ticket
     */
    private int getIndexOfFirstLongestArrayWithNumbersForTicket() {
        int longest = 0;
        int indexOfFirstLongest = 0;
        for (Map.Entry<Integer, List<Integer>> entry: bingoNumbers.entrySet()) {
            if (entry.getValue().size() > longest) {
                longest = entry.getValue().size();
                indexOfFirstLongest = entry.getKey();
            }
        }
        return indexOfFirstLongest;
    }

    /**
     * Obtain next index of the largest array with remaining numbers for specific column, it covers EDGE CASE, when current ticket
     * has already fulfilled column with the numbers from the provided column index
     *
     * @param unPopulatedColumns Set of indexes of populated columns
     * @return index of the longest array which is not the same as previous index
     */
    private int getIndexOfNextLongestArrayWithNumbersForTicket(Set<Integer> unPopulatedColumns) {
        int longest = 0;
        int indexOfFirstLongest = 0;
        for (int columnIndex : unPopulatedColumns.stream().toList()) {
            if (bingoNumbers.get(columnIndex).size() > longest) {
                longest = bingoNumbers.get(columnIndex).size();
                indexOfFirstLongest = columnIndex;
            }
        }
        return indexOfFirstLongest;
    }

    /**
     * Obtaining random number in the column range for provided column index
     *
     * @param randomColumnIndex column index from the ticket
     * @return number for insertion in the column (from the column numbers range)
     */
    private int getNumberForColumn(int randomColumnIndex) {
        if (!bingoNumbers.get(randomColumnIndex).isEmpty()) {
            return bingoNumbers.get(randomColumnIndex).remove(0);
        } else {
            return 0;
        }
    }
}
