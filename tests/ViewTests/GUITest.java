/**
 * Class Created by Kristian Wright

 *  This class is responsible for testing the GUI of the Monopoly game.
 *  It uses AssertJ Swing to create a GUI test framework.
 *
 */

package ViewTests;

import View.GUI;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GUITest {

    private FrameFixture window;

    @BeforeEach
    public void setUp() {
        // Create the GUI instance in the EDT (Event Dispatch Thread)
        GUI gui = GuiActionRunner.execute(GUI::new);
        window = new FrameFixture(gui);
        window.show(); // Show the GUI for testing
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp(); // Clean up resources after each test
    }

    @Test
    public void testTabbedPaneExists() {
        // Verify that the tabbed pane exists
        JTabbedPane tabbedPane = window.robot().finder().findByType(JTabbedPane.class);
        assertNotNull(tabbedPane, "TabbedPane should exist");
    }

    @Test
    public void testPlayerTabsExist() {
        // Verify that all player tabs exist
        String[] playerNames = {"Stacy", "Alex", "Jamie", "Jordan"};
        String[] tabTitles = window.tabbedPane().tabTitles(); // Get all tab titles
        for (String playerName : playerNames) {
            boolean tabExists = java.util.Arrays.asList(tabTitles).contains(playerName);
            assertNotNull(tabExists, "Tab for player " + playerName + " should exist");
        }
    }
}
