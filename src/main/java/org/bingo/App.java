package org.bingo;

import java.util.Arrays;

import static org.bingo.BingoFactory.TICKETS_ON_STRIP;
import static org.bingo.BingoFactory.TICKET_ROWS;

/**
 * Class with main method and example of the usage of BingoFactory
 */
public class App
{
    public static void main( String[] args )
    {
        BingoFactory bingoFactory = new BingoFactory();
        int[][][] bingoStrip = bingoFactory.generateBingoStrip();

        // Simple print of the bingo strip tickets
        System.out.println("Bingo strip tickets:");
        for (int ticket = 0; ticket < TICKETS_ON_STRIP; ticket++) {
            for (int row = 0; row < TICKET_ROWS; row++) {
                System.out.println(Arrays.toString(bingoStrip[ticket][row]));
            }
            System.out.println();
        }
    }
}
