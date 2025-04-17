/**
 * GameActionsPanel.java

 * This class represents a panel in the GUI that contains buttons for various game actions.
 * It allows players to interact with the game by performing actions such as rolling dice,
 * starting trades, mortgaging properties, buying houses/hotels, and ending their turn.

 * Authored by Collin Cabral-Castro
 */

package View;

import javax.swing.*;

public class GameActionsPanel extends JPanel {
    public GameActionsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JButton("Roll Dice"));
        add(new JButton("Start Trade"));
        add(new JButton("Mortgage Property"));
        add(new JButton("Buy House/Hotel"));
        add(new JButton("End Turn"));
    }
}
