/**
 * GameBoardTest.java

 * This file contains unit tests for the GameBoard class in the Monopoly game model.
 * It tests the initialization of the board, player movement, starting money distribution,
 * and various space interactions.

 * Created by Kristian Wright modified by Collin Cabral-Castro

 */

package ModelTests;

import Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {
    private GameBoard gameBoard;
    private Player player1;

    @BeforeEach
    public void setUp() {
        Bank bank = new Bank(new ArrayList<>());
        List<Player> players = new ArrayList<>();
        gameBoard = new GameBoard(players, true, bank); // Create a GameBoard in test mode
        player1 = new Player("Player 1", "Token1", gameBoard);
        Player player2 = new Player("Player 2", "Token2", gameBoard);
        players.add(player1);
        players.add(player2);
    }

    @Test
    public void testInitializeBoard() {
        assertEquals(40, gameBoard.getSpaces().size(), "The board should have 40 spaces.");
    }

    /**
     * Tests the movement of a player on the board.
     * Verifies the player's position and money after moving.
     */
    @Test
    public void testMovePlayer() {
        gameBoard.movePlayer(player1, 5);
        assertEquals(5, player1.getPosition(), "Player 1 should be at position 5.");
        gameBoard.movePlayer(player1, 35);
        assertEquals(0, player1.getPosition(), "Player 1 should be at position 0 after passing Go.");
        assertEquals(1700, player1.getMoney(), "Player 1 should have $1700 after passing Go.");
    }

    /**
     * Tests the distribution of starting money to players.
     * Verifies that each player has $1500 at the start.
     */
    @Test
    public void testStartingMoney() {
        for (Player player : gameBoard.getPlayers()) {
            assertEquals(1500, player.getMoney(), player.getName() + " should have $1500 at the start.");
        }
    }

    /**
     * Tests landing on different spaces on the board.
     * Verifies the player's position and the name of the space they land on.
     */
    @Test
    public void testLandOnDifferentSpaces() {
        gameBoard.movePlayer(player1, 1); // Move to Mediterranean Avenue
        assertEquals("Mediterranean Avenue", gameBoard.getSpace(player1.getPosition()).getName(), "Player 1 should be on Mediterranean Avenue.");
        assertEquals(1, player1.getPosition(), "Player 1 should be at position 1.");

        gameBoard.movePlayer(player1, 2); // Move to Baltic Avenue
        assertEquals("Baltic Avenue", gameBoard.getSpace(player1.getPosition()).getName(), "Player 1 should be on Baltic Avenue.");
        assertEquals(3, player1.getPosition(), "Player 1 should be at position 3.");

        gameBoard.movePlayer(player1, 1); // Move to Income Tax
        assertEquals("Income Tax", gameBoard.getSpace(player1.getPosition()).getName(), "Player 1 should be on Income Tax.");
        assertEquals(4, player1.getPosition(), "Player 1 should be at position 4.");

        gameBoard.movePlayer(player1, 1); // Move to Reading Railroad
        assertEquals("Reading Railroad", gameBoard.getSpace(player1.getPosition()).getName(), "Player 1 should be on Reading Railroad.");
        assertEquals(5, player1.getPosition(), "Player 1 should be at position 5.");
    }

    /**
     * Tests a player passing the Go space.
     * Verifies the player's position and money after passing Go.
     */
    @Test
    public void testPlayerPassesGo() {
        player1.setPosition(38);
        gameBoard.movePlayer(player1, 3);
        assertEquals(1, player1.getPosition(), "Player 1 should be at position 1 after passing Go.");
        assertEquals(1700, player1.getMoney(), "Player 1 should have $1700 after passing Go.");
    }

    /**
     * Tests a player landing on the GoToJail space.
     * Verifies the player's position and jail status after landing on Go To Jail.
     */
    @Test
    public void testPlayerLandsOnGoToJail() {
        player1.setPosition(29);
        gameBoard.movePlayer(player1, 1); // Move to Go To Jail
        assertEquals(10, player1.getPosition(), "Player 1 should be at position 10 (Jail).");
        assertTrue(player1.isInJail(), "Player 1 should be in jail.");
    }
}