package View;

import Model.PropertySpace;
import Model.Space;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the visual game board panel that displays spaces and player tokens.
 * Handles the rendering and positioning of board spaces and player tokens.
 *
 * @author Rachele Grigoli, Kristian Wright
 */
public class GameBoardPanel extends JPanel {
    private final List<Space> spaces;
    private final Map<String, JLabel> playerTokens = new HashMap<>();

    /**
     * Creates visible panel for game board
     *
     * @param gameBoard The game board model containing spaces
     * @param selectedPlayerTokens Array of token names for players
     */
    public GameBoardPanel(Model.GameBoard gameBoard, String[] selectedPlayerTokens) {
        setBackground(Color.WHITE);
        setLayout(null);

        spaces = gameBoard.getSpaces();

        addSpacesToBoard();

        if (selectedPlayerTokens != null && selectedPlayerTokens.length > 0) {
            addTokens(selectedPlayerTokens);
        }

        revalidate();
        repaint();
    }

    /**
     * Positions spaces (buttons) around the board
     */
    private void addSpacesToBoard() {
        int constant = 12; // change this number to change size of tiles
        int CornerSpaceSize = 82 + constant; // width-height for Go, Jail, Free Parking, and Go to Jail spaces
        int horizontalWidth = 64 + constant;
        int horizontalHeight = 82 + constant;
        int verticalWidth = 82 + constant;
        int verticalHeight = 60 + constant;

        int x = 82 + constant;
        int y = 0;

        // Top row (0–9)
        addSpaceButton(spaces.get(0), 0, y, CornerSpaceSize, CornerSpaceSize); // GO
        for (int i = 1; i < 10; i++) {
            addSpaceButton(spaces.get(i), x, y, horizontalWidth, horizontalHeight);
            x += horizontalWidth;
        }
        addSpaceButton(spaces.get(10), x, y, CornerSpaceSize, CornerSpaceSize); // Jail

        // Right column (11–19)
        y += CornerSpaceSize;
        for (int i = 11; i < 20; i++) {
            addSpaceButton(spaces.get(i), x, y, verticalWidth, verticalHeight);
            y += verticalHeight;
        }
        addSpaceButton(spaces.get(20), x, y, CornerSpaceSize, CornerSpaceSize); // Free Parking

        x += horizontalWidth;

        // Bottom row (21–29)
        x -= horizontalWidth;
        for (int i = 21; i < 30; i++) {
            x -= horizontalWidth;
            addSpaceButton(spaces.get(i), x, y, horizontalWidth, horizontalHeight);
        }
        addSpaceButton(spaces.get(30), x - CornerSpaceSize, y, CornerSpaceSize, CornerSpaceSize); // Go to Jail

        // Left column (31–39)
        x -= CornerSpaceSize;
        y -= verticalHeight;
        for (int i = 31; i < 40; i++) {
            addSpaceButton(spaces.get(i), x, y, verticalWidth, verticalHeight);
            y -= verticalHeight;
        }
    }

    private int[] colorSelection(String color) {
        if (color.equalsIgnoreCase("Brown")) {
            return new int[]{181, 101, 29};
        } else if (color.equalsIgnoreCase("Light Blue")) {
            return new int[]{173, 216, 230};
        } else if (color.equalsIgnoreCase("Pink")) {
            return new int[]{248, 24, 148};
        } else if (color.equalsIgnoreCase("Orange")) {
            return new int[]{255, 95, 31};
        } else if (color.equalsIgnoreCase("Red")) {
            return new int[]{255, 0, 0};
        } else if (color.equalsIgnoreCase("Yellow")) {
            return new int[]{255, 244, 79};
        } else if (color.equalsIgnoreCase("Green")) {
            return new int[]{0, 128, 0};
        } else if (color.equalsIgnoreCase("Dark Blue")) {
            return new int[]{65, 105, 255};
        } else {
            return new int[]{0, 0, 0}; //black
        }
    }

    private void addSpaceButton(Space space, int x, int y, int width, int height) {
        JPanel spacePanel = new JPanel(new GridBagLayout());
        spacePanel.setBounds(x, y, width, height);
        spacePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        spacePanel.setBackground(Color.LIGHT_GRAY); // Default color

        String formattedName = "<html><div style='text-align:center;'>" +
                space.getName().replace(" ", "<br>") +
                "</div></html>";
        JLabel nameLabel = new JLabel(formattedName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 10));
        spacePanel.add(nameLabel); // GridBagLayout centers it

        int index = spaces.indexOf(space);
        boolean isCorner = index == 0 || index == 10 || index == 20 || index == 30;

        if (isCorner) {
            spacePanel.setBackground(Color.WHITE);
        } else if (space instanceof PropertySpace) {
            String colorName = ((PropertySpace) space).getColorGroup();
            int[] rgb = colorSelection(colorName);
            spacePanel.setBackground(new Color(rgb[0], rgb[1], rgb[2]));
        } else {
            spacePanel.setBackground(new Color(200, 200, 200)); // default light gray
        }

        // Add "click" behavior
        spacePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JDialog dialog = new JDialog();
                dialog.setTitle(space.getName());
                dialog.setSize(300, 200);
                dialog.setLocationRelativeTo(null);
                dialog.add(new JLabel("<html><center>Details about " + space.getName() + "</center></html>", SwingConstants.CENTER));
                dialog.setVisible(true);
            }
        });

        this.add(spacePanel);
    }

    /**
     * Adds player tokens to the game board at the starting position
     *
     * @param selectedPlayerTokens Array of token names
     */
    private void addTokens(String[] selectedPlayerTokens) {
        int tokenWidth = 55;

        for (int i = 0; i < selectedPlayerTokens.length; i++) {
            String tokenName = selectedPlayerTokens[i];
            String tokenImageName = tokenName + "Token"; // e.g., "CarToken", "DogToken"
            int playerStartPosition = 0; // All players start on "GO" space (index 0)
            Point coords = getCoordinatesForPosition(playerStartPosition); // Get pixel coordinates for that space
            int offset = i * 10; // offsetting token on the board from the rest of the pieces
            JLabel tokenLabel = addToken(tokenImageName, coords.x + offset, coords.y, tokenWidth);
            playerTokens.put(tokenName, tokenLabel);
        }
    }

    /**
     * Creates and adds a token to the board
     *
     * @param pngName Name of the token image file (without extension)
     * @param xOffset X coordinate on the board
     * @param yOffset Y coordinate on the board
     * @param tokenWidth Width of the token image
     * @return The JLabel representing the token
     */
    public JLabel addToken(String pngName, int xOffset, int yOffset, int tokenWidth) {
        ImageIcon tokenIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/" + pngName + ".png")));
        Image img = tokenIcon.getImage();

        int newHeight = (int) (img.getHeight(null) * ((double) tokenWidth / img.getWidth(null)));
        Image scaledImage = img.getScaledInstance(tokenWidth, newHeight, Image.SCALE_SMOOTH);
        tokenIcon = new ImageIcon(scaledImage);

        JLabel tokenLabel = new JLabel(tokenIcon);
        tokenLabel.setBounds(xOffset, yOffset, tokenWidth, newHeight);
        add(tokenLabel); // Adds token label to the board panel

        setComponentZOrder(tokenLabel, 0); // Ensure tokens stay visually on top
        repaint(); // Refresh UI

        return tokenLabel;
    }

    /**
     * Assigns positions for tokens to jump around on throughout the game board spaces
     *
     * @param position Board position (0-39)
     * @return Point containing x,y coordinates
     */
    public Point getCoordinatesForPosition(int position) {
        int constant = 12;
        int CornerSpaceSize = 82 + constant;
        int horizontalWidth = 64 + constant;
        int verticalHeight = 60 + constant;

        int x = 82 + constant;
        int y = 0;

        if (position >= 0 && position <= 10) {
            // Top row (0–10)
            x = (position == 0) ? 0 : x + (horizontalWidth * (position - 1));
        } else if (position > 10 && position <= 20) {
            // Right column (11–20)
            x = x + (horizontalWidth * 9);
            y = CornerSpaceSize + (verticalHeight * (position - 11));
        } else if (position > 20 && position <= 30) {
            // Bottom row (21–30)
            x = (horizontalWidth * (30 - position));
            y = CornerSpaceSize + (verticalHeight * 9);
        } else if (position > 30 && position < 40) {
            // Left column (31–39)
            x = 0;
            y = CornerSpaceSize + (verticalHeight * (39 - position));
        }

        // Small offset tweak to center token
        return new Point(x + 10, y + 10);
    }

    /**
     * Gets the JLabel for a specific player token
     *
     * @param tokenName The token name
     * @return The JLabel for the token
     */
    public JLabel getPlayerToken(String tokenName) {
        return playerTokens.get(tokenName);
    }

    /**
     * Gets the map of all player tokens
     *
     * @return Map of token names to JLabels
     */
    public Map<String, JLabel> getPlayerTokens() {
        return playerTokens;
    }
}
