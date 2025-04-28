package View;

import Model.Player;
import Model.PropertySpace;
import Model.Property;

import javax.swing.*;
import java.awt.*;

/**
 * PlayerPanel shows a player's name, money, and owned properties.
 * Created by Collin Cabral-Castro
 */
public class PlayerPanel extends JPanel {
    private final Player player;
    private final JLabel moneyLabel;
    private final JPanel propertyListPanel;

    public PlayerPanel(Player player) {
        this.player = player;

        setLayout(new BorderLayout());

        moneyLabel = new JLabel("Money: $" + player.getMoney());
        moneyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(moneyLabel, BorderLayout.NORTH);

        propertyListPanel = new JPanel();
        propertyListPanel.setLayout(new BoxLayout(propertyListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(propertyListPanel);

        add(scrollPane, BorderLayout.CENTER);

        refreshProperties();
    }

    public void refreshProperties() {
        propertyListPanel.removeAll();

        for (Property property : player.getProperties()) {
            if (property instanceof PropertySpace propertySpace) {
                TitleDeedCardPanel card = new TitleDeedCardPanel(propertySpace);
                propertyListPanel.add(card);
                propertyListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        // Update money label
        moneyLabel.setText("Money: $" + player.getMoney());

        propertyListPanel.revalidate();
        propertyListPanel.repaint();
    }
}
