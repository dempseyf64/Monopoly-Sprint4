/**
 * CommunityChestCardTest.java
 * This file contains unit tests for the CommunityChestCard class in the Monopoly game model.
 * It tests the shuffling of Community Chest cards and the application of specific cards.

 * Created by Kristian Wright Modified by Collin Cabral-Castro

 */

package ModelTests;

import Model.Bank;
import Model.CommunityChestCard;
import Model.GameBoard;
import Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the CommunityChestCard class.
 */
public class CommunityChestCardTest {
    private GameBoard gameBoard;
    private Player player1;
    private Bank bank;

    /**
     * Sets up the test environment before each test.
     * Initializes a new GameBoard object and two Player objects.
     */
    @BeforeEach
    public void setUp() {
        List<Player> players = new ArrayList<>();
        bank = new Bank(new ArrayList<>());
        gameBoard = new GameBoard(players, true, bank); // Create a GameBoard in test mode
        player1 = new Player("Player 1", "Token1", gameBoard);
        Player player2 = new Player("Player 2", "Token2", gameBoard);
        players.add(player1);
        players.add(player2);
    }

    /**
     * Tests the shuffling of Community Chest cards.
     * Verifies that the order of the cards is changed after shuffling.
     */
    @Test
    public void testShuffleCommunityChestCards() {
        List<String> originalOrder = new ArrayList<>();
        gameBoard.getCommunityDeck().forEach(card -> originalOrder.add(card.getDescription()));

        CommunityChestCard.shuffleCommunityChestCards(gameBoard.getCommunityDeck());

        List<String> shuffledOrder = new ArrayList<>();
        gameBoard.getCommunityDeck().forEach(card -> shuffledOrder.add(card.getDescription()));

        assertNotEquals(originalOrder, shuffledOrder, "Community Chest cards should be shuffled.");
    }

    /**
     * Tests drawing the "Advance to Go" Community Chest card.
     * Verifies that the player's position and money are updated correctly.
     */
    @Test
    public void testDrawAdvanceToGoCard() {
        CommunityChestCard advanceToGoCard = new CommunityChestCard("Advance to Go", (player) -> {
            player.setPosition(0);
            bank.payPlayer(player,200);
        });

        player1.setPosition(7); // Assume player is on Community Chest space
        advanceToGoCard.apply(player1);

        assertEquals(0, player1.getPosition(), "Player 1 should be on Go.");
        assertEquals(1700, player1.getMoney(), "Player 1 should have $1700 after advancing to Go.");
    }

    /**
     * Tests drawing the "Bank error in your favor" Community Chest card.
     * Verifies that the player's money is updated correctly.
     */
    @Test
    public void testDrawBankErrorInYourFavorCard() {
        CommunityChestCard bankErrorCard = new CommunityChestCard("Bank error in your favor", (player) -> bank.payPlayer(player, 200));

        player1.setPosition(2); // Assume player is on Community Chest space
        bankErrorCard.apply(player1);

        assertEquals(1700, player1.getMoney(), "Player 1 should have $1700 after bank error in their favor.");
    }
}