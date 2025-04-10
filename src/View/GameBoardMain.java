/**
 * Class Created by Kristian Wright Modified by Collin Cabral-Castro

 * This is the main class for testing the GameBoard and GameBoardView classes.
 * It creates a GameBoard object with two players, sets the game board for each player,
 * and then uses the GameBoardView to display the game board and player status.
 */

package View;

import Model.Bank;
import Model.GameBoard;
import Model.Player;

import java.util.ArrayList;
import java.util.List;

public class GameBoardMain {
    public static void main(String[] args) {
        // Create players
        Player player1 = new Player("Player 1", "Token1", null);
        Player player2 = new Player("Player 2", "Token2", null);

        // Create a list of players
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        //Creates a bank object
        Bank bank = new Bank(new ArrayList<>());

        // Create a game board with the list of players
        GameBoard gameBoard = new GameBoard(players, false, bank);

        // Set the game board for each player
        player1.setGameBoard(gameBoard);
        player2.setGameBoard(gameBoard);

        // Create a GameBoardView instance
        GameBoardView gameBoardView = new GameBoardView(gameBoard);

        // Display the game board and player status
        gameBoardView.displayBoard();
        gameBoardView.displayPlayerStatus();
    }
}