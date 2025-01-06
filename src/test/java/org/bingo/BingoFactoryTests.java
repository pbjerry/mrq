package org.bingo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;

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
    }

    @Test
    @DisplayName("Counting number of tickets in the bingo strip")
    public void countingTicketsInTheBingoStrip() {
        fail("Not implemented");
    }

    @Test
    @DisplayName("Checking if each ticket consists of 9 columns and 3 rows")
    public void checkingIfEachTicketConsistOfNineColumnsAndThreeRows() {
        fail("Not implemented");
    }

    @Test
    @DisplayName("Counting numbers and spaces in the ticket rows")
    public void countingNumbersAndSpacesInEachBingoTicketInEachRow() {
        fail("Not implemented");
    }

    @Test
    @DisplayName("Counting numbers quantity vertically through all tickets")
    public void countNumbersQuantityVerticallyThroughAllTickets() {
        fail("Not implemented");
    }

    @Test
    @DisplayName("Checking for ascending order in each column")
    public void checkingAscendingOrderInEachColumnInEachTicket() {
        fail("Not implemented");
    }

    @Test
    @DisplayName("Checking if all columns in all tickets contains at least 1 number")
    public void checkingIfAllColumnsInAllTicketsContainsAtLeastOneNumber() {
        fail("Not implemented");
    }

    @Test
    @DisplayName("Checking if numbers vertically are in the right range")
    public void checkingIfNumbersVerticallyAreInTheRightRange() {
        fail("Not implemented");
    }

    @Test
    @DisplayName("Checking if there are any duplicates in the bingo strip tickets")
    public void checkingIfThereAreAnyDuplicatesInTheBingoStripTickets() {
        fail("Not implemented");
    }
}
