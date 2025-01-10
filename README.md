# Bingo strip generator

# Table of contents

<!-- TOC -->
* [Description](#description)
* [Technologies](#technologies)
* [Test cases](#test-cases)
* [Usage example](#usage-example)
<!-- TOC -->

## Description

Simple implementation of the Bingo strip tickets generator. It generates Bingo strip with 6 tickets. 
Each ticket consists of 9 columns and 3 rows and has 15 numbers (5 numbers in each line) and there is no column 
in the tickets without at least one number. Ticket contains numbers between 1 and 90 (inclusive). **Number 0 represent
the blank space in the bingo ticket and should be ignored**.

Generator first generate all Bingo numbers (1 - 90) which are then used to populate tickets.
It continues with the population of the first two rows in each Bingo strip ticket. During
population of the first two rows it fulfill all columns in the ticket, which means that there is no column in the 
ticket without at least one number after first two rows are fulfilled. Last step is population of the
last, third row in each ticket with the remaining numbers. **Sorting and swapping of the numbers in the columns is not 
necessary because numbers are picked in the ascending order from the map of numbers for each column**. 

## Technologies

Project is created with:

- [Java 19][2]
- [Maven 3.8.1][1]
- [Intellij IDEA][3]

Testing library:
- [JUnit 5][4]

## Test cases

Project contains 9 test cases which covers all the necessary rules to generate Bingo strip with 6 tickets.
Implemented test cases:

- Counting number of tickets in the Bingo strip
- Checking if each ticket consists of 9 columns and 3 rows
- Counting numbers and spaces in the ticket rows (must be 5 numbers and 4 spaces)
- Counting numbers quantity vertically in columns through all tickets
- Checking for ascending order in each column of each ticket
- Checking if all columns in all tickets contains at least 1 number
- Checking if numbers are in the right range vertically
- Checking if there are any duplicates in the Bingo strip tickets
- Generating 10k Bingo strips in less than 1 second (1000ms)
  - If this test is used then output of the execution time in milliseconds print in the console

## Usage example

Main class of the Bingo ticket generator is the BingoFactory. Using it's instance and public method generateBingoStrip()
enables generating of the Bingo strip.

Example which creates new BingoFactory, generate new Bingo strip and print the ticket into the console (as mentiond in the description
section above - zeros represent the empty spaces in the ticket):

```
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
```

**If you run Bingo ticket generator it will already run above code example and print the Bingo strip with 6 tickets in the console**

[1]: https://maven.apache.org/  "Maven"
[2]: https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html   "Java 19"
[3]: https://www.jetbrains.com/idea/    "Intellij IDEA"
[4]: https://junit.org/junit5/  "JUnit 5"


