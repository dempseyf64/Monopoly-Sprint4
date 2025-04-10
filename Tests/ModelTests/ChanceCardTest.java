/**
 * ChanceCardTest.java

 * This file contains unit tests for the ChanceCard class in the Monopoly game model.
 * It tests the shuffle and application of chance cards.

 * Created by Kristian Wright Modified by Collin Cabral-Castro

 */

package ModelTests;

import Model.ChanceCard;
import Model.GameBoard;
import Model.Player;
import Model.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the ChanceCard class.
 */
public class ChanceCardTest {
    private GameBoard gameBoard;
    private Player player1;


    /**
     * Sets up the test environment before each test.
     * Initializes a new GameBoard object and two Player objects.
     */
    @BeforeEach
    public void setUp() {
        List<Player> players = new ArrayList<>();
        Bank bank = new Bank(new ArrayList<>());
        gameBoard = new GameBoard(players , true, bank); // Create a GameBoard in test mode
        player1 = new Player("Player 1", "Token1", gameBoard);
        Player player2 = new Player("Player 2", "Token2", gameBoard);
        players.add(player1);
        players.add(player2);
    }

    /**
     * Tests the shuffling of Chance cards.
     * Verifies that the order of the cards is changed after shuffling.
     */
    @Test
    public void testShuffleChanceCards() {
        List<String> originalOrder = new ArrayList<>();
        gameBoard.getChanceDeck().forEach(card -> originalOrder.add(card.getDescription()));

        ChanceCard.shuffleChanceCards(gameBoard.getChanceDeck());

        List<String> shuffledOrder = new ArrayList<>();
        gameBoard.getChanceDeck().forEach(card -> shuffledOrder.add(card.getDescription()));

        assertNotEquals(originalOrder, shuffledOrder, "Chance cards should be shuffled.");
    }

    /**
     * Tests drawing the "Go to Jail" Chance card.
     * Verifies that the player's position and jail status are updated correctly.
     */
    @Test
    public void testDrawGoToJailCard() {
        ChanceCard goToJailCard = new ChanceCard("Go to Jail", Player::goToJail);

        player1.setPosition(7); // Assume player is on Chance space
        goToJailCard.apply(player1);

        assertEquals(10, player1.getPosition(), "Player 1 should be in Jail.");
        assertTrue(player1.isInJail(), "Player 1 should be in jail.");
    }

    /**
     * Tests drawing the "Advance to Nearest Railroad" Chance card.
     * Verifies that the player's position is updated correctly.
     */
    @Test
    public void testDrawAdvanceToRailroadCard() {
        ChanceCard advanceToRailroadCard = new ChanceCard("Advance to Nearest Railroad", (player) -> {
            int newPosition = gameBoard.getNearestRailroad(player.getPosition());
            player.setPosition(newPosition);
        });

        player1.setPosition(7); // Assume player is on Chance space
        advanceToRailroadCard.apply(player1);

        int expectedRailroadPosition = gameBoard.getNearestRailroad(7);
        assertEquals(expectedRailroadPosition, player1.getPosition(), "Player 1 should be on the nearest Railroad.");
        assertTrue(gameBoard.getSpace(player1.getPosition()).getName().contains("Railroad"), "Player 1 should be on a Railroad space.");
    }
}