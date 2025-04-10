/**
 *
 * Class Created by Kristian Wright

 * Package: ViewTests

 * This class contains unit tests for the `DiceView` class. It verifies the functionality of methods
 * that display dice roll results, display the count of doubles rolled, and reset the doubles rolled count.
 */

package ViewTests;

import Model.Dice;
import View.DiceView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiceViewTest {
    private Dice dice;
    private DiceView diceView;

    /**
     * Sets up the test environment before each test.
     * Initializes the `Dice` instance and creates a `DiceView` instance.
     */
    @BeforeEach
    public void setUp() {
        dice = Dice.getInstance();
        diceView = new DiceView(dice);
    }

    /**
     * Tests the `displayRollResults` method of the `DiceView` class.
     * Verifies that the roll results are not null and contain the expected text.
     */
    @Test
    public void testDisplayRollResults() {
        String result = diceView.displayRollResults();
        assertNotNull(result, "Roll results should not be null.");
        assertTrue(result.contains("Rolled:"), "Roll results should contain 'Rolled:' text.");
    }

    /**
     * Tests the `displayDoublesRolled` method of the `DiceView` class.
     * Verifies that the doubles rolled display is not null and contains the expected text.
     */
    @Test
    public void testDisplayDoublesRolled() {
        dice.rollDice(); // Roll the dice to potentially get doubles
        String result = diceView.displayDoublesRolled();
        assertNotNull(result, "Doubles rolled display should not be null.");
        assertTrue(result.contains("Current doubles rolled:"), "Doubles rolled display should contain 'Current doubles rolled:' text.");
    }

    /**
     * Tests the `resetDoublesRolled` method of the `DiceView` class.
     * Verifies that the doubles rolled count is reset to 0.
     */
    @Test
    public void testResetDoublesRolled() {
        dice.rollDice(); // Roll the dice to potentially get doubles
        diceView.resetDoublesRolled();
        assertEquals(0, dice.getDoublesRolled(), "Doubles rolled count should be reset to 0.");
    }
}
