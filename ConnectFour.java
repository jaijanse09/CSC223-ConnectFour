
/**
 * Connect Four - AS91896
 * @author Jai Janse
 * 
 * @version 
 * 17/03/26 - Array and Variable setup
 * 18/03/26 - Created and initialized the Array
 * 19/03/26 - Basic input + Code cleanup
 * 24/03/26 - Intro section + Start of token placement
 * 25/03/26 - Added basic token placement + Basic input sanitization
 * 26/03/26 - Added good input sanitization + Commented code
 * 28/03/26 - Code Cleanup + GameLoop start + Created a basic P1 and P2 setup
 * 29/03/26 - Rebuilt input sanitization again (3rd time) + Created and improved 
 *            recursiveness of P1 and P2 turn methods so that they use a helper method 
 *            instead of a seperate methods + Improved GameLoop + Added and updated 
 *            different variable names + Moved array initialization to a new method called 
 *            setUp + Clean and Commented Code
 * 30/03/26 - Updated gameLoop + Added moves counter for draw function + 
 *            Made game go to draw when board is full (42 tokens) + Added Horizontal, 
 *            Vertical, Incline Diagonal, decline Diagonal row checking + 
 *            Improved win statements + Clean and Commented Code
 * 31/03/26 - Fixed Incline Diagonal + Advanced testing + Clean and Commented code
 * 01/04/26 - Advanced testing + Fixed Draw win function
 * 02/04/26 - Advanced testing + Last minute tweaks
 */

/**   
 *  ____                            _     _____                
 * / ___|___  _ __  _ __   ___  ___| |_  |  ___|__  _   _ _ __ 
 * | |   / _ \| '_ \| '_ \ / _ \/ __| __| | |_ / _ \| | | | '__|
 * | |__| (_) | | | | | | |  __/ (__| |_  |  _| (_) | |_| | |   
 *  \____\___/|_| |_|_| |_|\___|\___|\__| |_|  \___/ \__,_|_|   
 */

import java.util.Scanner; // Keyboard input package
public class ConnectFour
{
    Scanner input = new Scanner(System.in); // Keyboard Package

    // Variable setup for board
    final int BOARDROWS = 6;
    final int BOARDCOLUMNS = 7;

    // Variable setup for inputs
    final String start = "Type 'Start' to begin";
    final String question = "Choose a column between 0-6";
    final String invalid = "Invaild Input! Please try again!";
    final String clearTerminal = "\u000C";

    // Handles keyboard input for Players (start + column)
    String choice; 

    // Used to check column numerical values passed from choice
    int column;

    // Used to count the amount of moves the player makes
    int moves = 0;

    // Booleans for column input
    public boolean valid = false; 
    public boolean showError = false;

    // Boolean for if column is full
    public boolean columnFull = false;

    // Boolean for token placing
    public boolean tokenPlaced = false;

    // Boolean for GameOver
    public boolean gameOver = false;

    // Boolean for draw
    public boolean draw = false;

    // Player setup
    final String P1TOKEN = "X";
    final String P2TOKEN = "O";
    String currentPlayer = P1TOKEN;
    int playerNumber;

    // Defines the array board
    String [][] board = new String [BOARDROWS][BOARDCOLUMNS];

    // Main method that calls setUp and repeats gameLoop
    public ConnectFour()
    {

        // Calls setup method
        setUp();

        // Repeats gameLoop until player gets 4 in a row or 42 moves (Connect Four)
        while (!gameOver) { 
            gameLoop();

            // If moves = 42 and gameOver is false then set gameOver and Draw to true
            if (moves == 42 && !gameOver) {
                gameOver = true; 
                draw = true;    
            } 
        }  

        System.out.print(clearTerminal); // Clears terminal
        display(); // Calls Display method
        System.out.println();

        // If a draw says its a draw otherwise says which player has Connect Four
        if (draw) {
            System.out.println("It's a Draw!");
        } else{    
            System.out.println("Player " + playerNumber + " has Won!");
            System.out.println("Connect Four detected!");
        }

        System.out.println("Game Over!");
        System.out.println(); 
        System.exit(0); // Exits Game

    }

    // Main loop that calls the playerTurn helper method and switches players (P1 and P2)
    public void gameLoop() 
    {    

        // If game is still running
        if(!gameOver) { 
            // If P1 token in use then P1 turn if P2 token in use then P2 turn
            if (currentPlayer.equals(P1TOKEN)) {
                playerNumber = 1;
                playerTurn(); // Calls playerTurn helper method
                rowCheck(currentPlayer); // Calls rowCheck method
                currentPlayer = P2TOKEN; 
            } else {
                playerNumber = 2;
                playerTurn(); // Calls playerTurn helper method
                rowCheck(currentPlayer); // Calls rowCheck method
                currentPlayer = P1TOKEN;
            }

        }
    }

    // SetUp and initialization of array
    public void setUp ()
    {

        // Initializes array and sets with value of " "
        for(int r=0; r<BOARDROWS; r++) {
            for (int c=0; c<BOARDCOLUMNS; c++){
                board[r][c] = " ";
            }
        }        

        intro(); // Calls intro Method
    }

    // Start of Connect Four game
    public void intro()
    { 

        System.out.print(clearTerminal); // Clears terminal

        System.out.println(start); // Calls the start variable
        choice = input.nextLine(); // Player Keyboard input 

        // Asks and requires player to type start
        while (!choice.equalsIgnoreCase("Start")) {
            System.out.print(clearTerminal);// Clears terminal
            System.out.println(invalid);
            System.out.println(start); // Calls the start variable
            choice = input.nextLine();
        }

        System.out.print(clearTerminal);// Clears terminal
    }

    // Displays the board (array) to the players
    public void display()
    {

        // Prints the board (array) (6 x 7 = 42 Slots)
        for(int r=0; r<BOARDROWS; r++) {

            for (int c=0; c<BOARDCOLUMNS; c++){

                if (board[r][c].equals(" ")) {
                    System.out.print("• ");
                } else {
                    System.out.print(board[r][c] + " ");
                }
            }

            System.out.println();
        }

        // Prints the board (array) column numbers

        for (int c=0; c<BOARDCOLUMNS; c++){
            System.out.print(c + " ");
        }

        System.out.println();
    }   

    // Helper Method for Players for where they want to place their token
    public void playerTurn()
    {        

        // Resets vaild
        valid = false;

        // Repeatedly Asks the user for a column number until that column is vaild (0-6) 
        while (!valid) {

            // Clears the terminal then displays the Connect 4 board
            System.out.print(clearTerminal);
            display();

            // If the user enters an invalid input it prints invalid above the question
            if (showError) {
                System.out.println(invalid);
                showError = false;
            }

            // If the player fills the column it prints "Column is full" above the question
            if (columnFull) {
                System.out.println("Column is full!");
                columnFull = false;
            }

            // Player Questions 
            System.out.println("Player " + playerNumber + " where would you like your token?");
            System.out.println(question);

            // Player keyboard Input
            choice = input.nextLine();

            // Takes players input then seperates them into numerical inputs and other inputs
            // then checks if that numerical value is within the range of the columns (0-6) 
            // and will place the token. If input is not within range then will return an 
            // invaild message above the question saying "Invaild Input! Please try again!"
            try {

                // Checks numerical input
                column = Integer.parseInt(choice);
                if (column >= 0 && column <= 6) {

                    // Sets tokenPlaced to false
                    tokenPlaced = false;

                    // Places the tokens from row 5 backwards to row 0 so that the tokens 
                    // fill up bottom to top for the player rather than top to bottom
                    for (int row = BOARDROWS -1; row >= 0; row--){

                        if (board[row][column].equals(" ")) {
                            board[row][column] = currentPlayer;
                            tokenPlaced = true;
                            break;
                        }
                    }

                    // Called after the input is sanitized and is column input is 0-6 
                    // Player token is then placed on the board (array)
                    if (tokenPlaced) {
                        moves++;
                        valid = true;
                    } else {
                        columnFull = true;
                    }

                    // Calls showError if the numerical value entered into column input 
                    // is not column number 0-6
                }else {
                    showError = true; 
                } 

                // Calls showError if column input is not a numerical input (eg:hdhdh)
            } catch (NumberFormatException e) {
                showError = true;
            }
        }
    }

    // Method for checking if 4 tokens in a row (Connect Four)
    // Resets rows and columns then keeps checking each row and column until there is 
    // 4 in row then calls gameOver function so that the user can't enter anymore tokens
    public void rowCheck (String currentPlayer) {

        // Horizontal token check
        // For each possible starting position it checks for 4 tokens on a 
        // Vertical Line going to the right 
        for (int r = 0; r<6; r++) {
            for (int c = 0; c<4; c++) {
                // Checks first token placed
                if(board[r][c].equals(currentPlayer) &&
                    // Checks if second token placed one right
                board[r][c+1].equals(currentPlayer) && 
                    // Checks if third token placed two right
                board[r][c+2].equals(currentPlayer) && 
                    // Checks if fourth token placed three right
                board[r][c+3].equals(currentPlayer)) {
                    gameOver = true;
                }       
            }
        }

        // Vertical token check
        // For each possible starting position it checks for 4 tokens on a 
        // Vertical Line going up 
        for (int r = 0; r<3; r++) {
            for (int c = 0; c<7; c++) {
                // Checks first token placed
                if(board[r][c].equals(currentPlayer) &&
                    // Checks if second token placed one up
                board[r+1][c].equals(currentPlayer) &&
                    // Checks if third token placed two up
                board[r+2][c].equals(currentPlayer) &&
                    // Checks if fourth token placed three up
                board[r+3][c].equals(currentPlayer)) {
                    gameOver = true;
                }
            }
        }

        // Decline Diagonal token check
        // For each possible starting position it checks for 4 tokens on a
        // Decline Diagonal going down and to the right
        for (int r = 0; r<3; r++) {
            for (int c = 0; c<4; c++) {
                // Checks first token placed
                if(board[r][c].equals(currentPlayer) &&
                    // Checks if second token placed one down, one right
                board[r+1][c+1].equals(currentPlayer) &&
                    // Checks if third token placed two down, two right
                board[r+2][c+2].equals(currentPlayer) &&
                    // Checks if fourth token placed three down, three right
                board[r+3][c+3].equals(currentPlayer)) {
                    gameOver = true;
                }
            }
        }

        // Incline Diagonal token check
        // For each possible starting position it checks for 4 tokens on a
        // Incline Diagonal going up and to the right
        for (int r = 3; r<6; r++) {
            for (int c = 0; c<4; c++) {
                // Checks first token placed
                if(board[r][c].equals(currentPlayer) &&
                    // Checks if second token placed one up, one right
                board[r-1][c+1].equals(currentPlayer) &&
                    // Checks if third token placed two up, two right
                board[r-2][c+2].equals(currentPlayer) &&
                    // Checks if fourth token placed three up, three right
                board[r-3][c+3].equals(currentPlayer)) {
                    gameOver = true; 
                }
            }
        }

    }
}