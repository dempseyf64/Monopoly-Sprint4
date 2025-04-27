package View;

import Model.PropertySpace;

import javax.swing.*;
import java.awt.*;

/**
 * Redesigned TitleDeedCardPanel based on real Monopoly cards.
 * Created by Collin Cabral-Castro
 */
public class TitleDeedCardPanel extends JPanel {
    public TitleDeedCardPanel(PropertySpace property) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setBackground(Color.WHITE);

        // Top colored banner
        JPanel colorBanner = new JPanel();
        colorBanner.setBackground(getColorFromGroup(property.getColorGroup()));
        JLabel nameLabel = new JLabel(property.getName(), SwingConstants.CENTER);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        colorBanner.add(nameLabel);

        add(colorBanner, BorderLayout.NORTH);

        // Center rent info
        JPanel rentPanel = new JPanel();
        rentPanel.setLayout(new BoxLayout(rentPanel, BoxLayout.Y_AXIS));
        rentPanel.setBackground(Color.WHITE);
        rentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rentPanel.add(new JLabel("Rent: $" + property.getPropertySite()));
        rentPanel.add(new JLabel("With 1 House: $" + property.getCostWithOneHouse()));
        rentPanel.add(new JLabel("With 2 Houses: $" + property.getCostWithTwoHouses()));
        rentPanel.add(new JLabel("With 3 Houses: $" + property.getCostWithThreeHouses()));
        rentPanel.add(new JLabel("With 4 Houses: $" + property.getCostWithFourHouses()));
        rentPanel.add(new JLabel("With Hotel: $" + property.getCostWithHotel()));
        rentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rentPanel.add(new JLabel("Mortgage Value: $" + (property.getPrice() / 2)));
        rentPanel.add(new JLabel("House Cost: $" + property.getCostWithOneHouse()));
        rentPanel.add(new JLabel("Hotel Cost: $" + property.getCostWithOneHouse())); // Assuming hotels cost same for now

        add(rentPanel, BorderLayout.CENTER);

        // Fine print
        JLabel finePrint = new JLabel("<html><center>If a player owns ALL lots of a color group,<br>rent is doubled on unimproved lots.</center></html>", SwingConstants.CENTER);
        finePrint.setFont(new Font("Arial", Font.PLAIN, 8));
        add(finePrint, BorderLayout.SOUTH);
    }

    private Color getColorFromGroup(String colorGroup) {
        return switch (colorGroup) {
            case "Brown" -> new Color(139, 69, 19);
            case "Light Blue" -> new Color(173, 216, 230);
            case "Pink" -> new Color(255, 105, 180);
            case "Orange" -> new Color(255, 165, 0);
            case "Red" -> new Color(220, 20, 60);
            case "Yellow" -> new Color(255, 255, 102);
            case "Green" -> new Color(0, 128, 0);
            case "Dark Blue" -> new Color(25, 25, 112);
            default -> Color.GRAY;
        };
    }
}
