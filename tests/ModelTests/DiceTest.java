/**
 * Class Created by Kristian Wright Modified by Collin Cabral-Castro.

 * This is the test class for the Dice class.
 * It contains unit tests to verify the functionality of the Dice class, including rolling the dice,
 * tracking doubles rolled, and ensuring the dice roll results are within the valid range.
 */

package ModelTests;

import Model.Bank;
import Model.Dice;
import Model.GameBoard;
import Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Dice class.
 */
public class DiceTest {
    private Dice dice;
    private Player player;

    /**
     * Sets up the test environment before each test.
     * Initializes a new Dice object and a Player object.
     */
    @BeforeEach
    public void setUp() {
        dice = Dice.getInstance();
        Bank bank = new Bank(new ArrayList<>());
        GameBoard gameBoard = new GameBoard(new ArrayList<>(), true, bank);  // or false, depending on the use case
        player = new Player("TestPlayer", "Car", gameBoard);
    }

    /**
     * Tests the rollDice method of the Dice class.
     * Verifies that the player moves the correct distance or goes to jail.
     */
    @Test
    public void testRollDice() {
        int initialPosition = player.getPosition();
        int initialMoney = player.getMoney();
        ArrayList<Integer> results = dice.rollDice();
        player.move(results.get(0) + results.get(1));
        int finalPosition = player.getPosition();

        // Check if the player passed Go and collected $200
        if (finalPosition < initialPosition && !player.isInJail()) {
            assertEquals(initialMoney + 200, player.getMoney(), "Player should collect $200 for passing Go.");
        }

        // Check if the player moved the correct distance or went to jail
        if (player.isInJail()) {
            assertEquals(10, player.getPosition(), "Player should be in jail at position 10.");
        } else {
            int result1 = results.get(0);
            int result2 = results.get(1);
            assertTrue(result1 + result2 >= 2 && result1 + result2 <= 12, "Player should move between 2 and 12 spaces after rolling dice.");
        }
    }

    /**
     * Tests that doubles rolled are counted correctly.
     */
    @Test
    public void testDoublesRolled() {
        dice.rollDice();
        int initialDoubles = dice.getDoublesRolled();
        ArrayList<Integer> results = dice.rollDice();
        int doublesCount = 0;
        for (int i = 0; i < results.size(); i += 2) {
            if (results.get(i).equals(results.get(i + 1))) {
                doublesCount++;
            }
        }
        assertEquals(initialDoubles + doublesCount, dice.getDoublesRolled(), "Doubles rolled count should increment correctly.");
    }

    /**
     * Tests that the doubles rolled count is reset correctly.
     */
    @Test
    public void testResetDoublesRolled() {
        dice.rollDice();
        dice.resetDoublesRolled();
        assertEquals(0, dice.getDoublesRolled(), "Doubles rolled count should be reset to 0.");
    }

    /**
     * Tests that the dice roll results are within the valid range (1 to 6).
     */
    @Test
    public void testValidDiceRollRange() {
        ArrayList<Integer> results = dice.rollDice();
        for (int result : results) {
            assertTrue(result >= 1 && result <= 6, "Dice roll result should be between 1 and 6.");
        }
    }

    /**
     * Tests that the doubles rolled count accumulates correctly over multiple rolls.
     */
    @Test
    public void testMultipleRolls() {
        int totalDoubles = 0;
        for (int i = 0; i < 10; i++) {
            ArrayList<Integer> results = dice.rollDice();
            for (int j = 0; j < results.size(); j += 2) {
                if (results.get(j).equals(results.get(j + 1))) {
                    totalDoubles++;
                }
            }
        }
        assertEquals(totalDoubles, dice.getDoublesRolled(), "Doubles rolled count should accumulate correctly over multiple rolls.");
    }
}