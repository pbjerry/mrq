package org.bingo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BingoFactoryTests {

    private static final int TICKETS_ON_STRIP = 6;
    private static final int TICKET_ROWS = 3;
    private static final int TICKET_COLUMNS = 9;
    private static final int ALLOWED_NUMBERS_IN_ROW = 5;
    private static final int ALLOWED_SPACES_IN_ROW = 4;

    BingoFactory bingoFactory;

    @BeforeEach
    public void setUp() {
        bingoFactory = new BingoFactory();
        bingoFactory.generateAllBingoNumbers();
    }

    @Test
    @DisplayName("Counting number of tickets in the bingo strip")
    public void countingTicketsInTheBingoStrip() {
        assertEquals(6, bingoFactory.generateBingoStrip().length);
    }

    @Test
    @DisplayName("Checking if each ticket consists of 9 columns and 3 rows")
    public void checkingIfEachTicketConsistOfNineColumnsAndThreeRows() {
        int[][][] bingoStrip = bingoFactory.generateBingoStrip();
        for (int ticket = 0; ticket < TICKETS_ON_STRIP; ticket++) {
            assertEquals(TICKET_ROWS, bingoStrip[ticket].length);
            for (int row = 0; row < TICKET_ROWS; row++) {
                assertEquals(TICKET_COLUMNS, bingoStrip[ticket][row].length);
            }
        }
    }

    @Test
    @DisplayName("Counting numbers and spaces in the ticket rows")
    public void countingNumbersAndSpacesInEachBingoTicketInEachRow() {
        int[][][] bingoStrip = bingoFactory.generateBingoStrip();
        int counterOfNumbersInRow;
        int counterOfSpacesInRow;
        for (int ticket = 0; ticket < TICKETS_ON_STRIP; ticket++) {
            for (int row = 0; row < TICKET_ROWS; row++) {
                counterOfNumbersInRow = 0;
                counterOfSpacesInRow = 0;
                for (int column = 0; column < TICKET_COLUMNS; column++) {
                    if (bingoStrip[ticket][row][column] == 0) {
                        counterOfSpacesInRow++;
                    } else if (bingoStrip[ticket][row][column] >= 0) {
                        counterOfNumbersInRow++;
                    }
                }
                assertEquals(ALLOWED_NUMBERS_IN_ROW, counterOfNumbersInRow);
                assertEquals(ALLOWED_SPACES_IN_ROW, counterOfSpacesInRow);
            }
        }
    }

    @Test
    @DisplayName("Counting numbers quantity vertically through all tickets")
    public void countNumbersQuantityVerticallyThroughAllTickets() {
        int[][][] bingoStrip = bingoFactory.generateBingoStrip();
        List<Integer> numbersPerColumn = new ArrayList<>(Arrays.asList(9, 10, 10, 10, 10, 10, 10, 10, 11));
        int counterNumbersInColumn;
        for (int column = 0; column < TICKET_COLUMNS; column++) {
            counterNumbersInColumn = 0;
            for (int ticket = 0; ticket < TICKETS_ON_STRIP; ticket++) {
                for (int row = 0; row < TICKET_ROWS; row++) {
                    if (bingoStrip[ticket][row][column] != 0) {
                        counterNumbersInColumn++;
                    }
                }
            }
            int columnNumbersQuantity = numbersPerColumn.get(column);
            assertEquals(columnNumbersQuantity, counterNumbersInColumn);
        }
    }

    @Test
    @DisplayName("Checking for ascending order in each column")
    public void checkingAscendingOrderInEachColumnInEachTicket() {
        int[][][] bingoStrip = bingoFactory.generateBingoStrip();
        for (int ticket = 0; ticket < TICKETS_ON_STRIP; ticket++) {
            for (int column = 0; column < TICKET_COLUMNS; column++) {
                if (bingoStrip[ticket][0][column] != 0 && bingoStrip[ticket][1][column] != 0 && bingoStrip[ticket][2][column] != 0) {
                    assertTrue(bingoStrip[ticket][0][column] < bingoStrip[ticket][1][column] && bingoStrip[ticket][1][column] < bingoStrip[ticket][2][column]);
                } else if (bingoStrip[ticket][0][column] != 0 && bingoStrip[ticket][1][column] == 0 && bingoStrip[ticket][2][column] != 0) {
                    assertTrue(bingoStrip[ticket][0][column] < bingoStrip[ticket][2][column]);
                } else if (bingoStrip[ticket][0][column] != 0 && bingoStrip[ticket][1][column] != 0 && bingoStrip[ticket][2][column] == 0) {
                    assertTrue(bingoStrip[ticket][0][column] < bingoStrip[ticket][1][column]);
                } else if (bingoStrip[ticket][0][column] == 0 && bingoStrip[ticket][1][column] != 0 && bingoStrip[ticket][2][column] != 0) {
                    assertTrue(bingoStrip[ticket][1][column] < bingoStrip[ticket][2][column]);
                }
            }
        }
    }

    @Test
    @DisplayName("Checking if all columns in all tickets contains at least 1 number")
    public void checkingIfAllColumnsInAllTicketsContainsAtLeastOneNumber() {
        int[][][] bingoStrip = bingoFactory.generateBingoStrip();
        for (int ticket = 0; ticket < TICKETS_ON_STRIP; ticket++) {
            for (int column = 0; column < TICKET_COLUMNS; column++) {
                assertFalse(bingoStrip[ticket][0][column] == 0 && bingoStrip[ticket][1][column] == 0 && bingoStrip[ticket][2][column] == 0);
            }
        }
    }

    @Test
    @DisplayName("Checking if numbers vertically are in the right range")
    public void checkingIfNumbersVerticallyAreInTheRightRange() {
        int[][][] bingoStrip = bingoFactory.generateBingoStrip();
        Map<Integer, List<Integer>> bingoNumbers = bingoFactory.generateAllBingoNumbers();
        List<Integer> numbersInColumn = new ArrayList<>();
        for (int column = 0; column < TICKET_COLUMNS; column++) {
            numbersInColumn.clear();
            for (int ticket = 0; ticket < TICKETS_ON_STRIP; ticket++) {
                for (int row = 0; row < TICKET_ROWS; row++) {
                    if (bingoStrip[ticket][row][column] != 0) {
                        numbersInColumn.add(bingoStrip[ticket][row][column]);
                    }
                }
            }
            numbersInColumn.sort(Comparator.naturalOrder());
            bingoNumbers.get(column).sort(Comparator.naturalOrder());
            assertEquals(numbersInColumn, bingoNumbers.get(column));
        }
    }

    @Test
    @DisplayName("Checking if there are any duplicates in the bingo strip tickets")
    public void checkingIfThereAreAnyDuplicatesInTheBingoStripTickets() {
        int[][][] bingoStrip = bingoFactory.generateBingoStrip();
        Set<Integer> allNumbersFromStripTickets = new HashSet<>();
        for (int column = 0; column < TICKET_COLUMNS; column++) {
            for (int ticket = 0; ticket < TICKETS_ON_STRIP; ticket++) {
                for (int row = 0; row < TICKET_ROWS; row++) {
                    if (bingoStrip[ticket][row][column] != 0) {
                        assertTrue(allNumbersFromStripTickets.add(bingoStrip[ticket][row][column]));
                    }
                }
            }
        }
    }
}
