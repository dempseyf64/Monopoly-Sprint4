/**
 * PlayerPanel.java

 * This class represents a panel that displays the player's information, including their name, money, and properties.

 * Authored by Collin Cabral-Castro
 */
package View;

import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JPanel {
    public PlayerPanel(String playerName, String playerToken) {
        setLayout(new BorderLayout());
        JLabel moneyLabel = new JLabel("Money: $1500");

        String tokenImage = "/Images/" + playerToken + "Token.png"; // Assuming token image files are named this way
        ImageIcon tokenIcon = new ImageIcon(getClass().getResource(tokenImage));
        if (tokenIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Error: Token image for " + playerToken + " not found.");
            tokenIcon = new ImageIcon(getClass().getResource("/Images/DefaultToken.png")); // Fallback image (ensure you have a default image)
        }
        JLabel tokenLabel = new JLabel(tokenIcon);

        JPanel props = new JPanel();
        props.setLayout(new BoxLayout(props, BoxLayout.Y_AXIS));
        props.add(new JLabel("Mediterranean Avenue (Mortgaged)"));
        props.add(new JLabel("Baltic Avenue"));

        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel(playerName));  // Display the player's name
        namePanel.add(tokenLabel);

        add(moneyLabel, BorderLayout.NORTH);
        add(new JScrollPane(props), BorderLayout.CENTER);

        setPreferredSize(new Dimension(300, 500));
    }
}
