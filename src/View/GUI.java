/**
 * The GUI class represents the main graphical user interface for the Monopoly game.
 * It initializes the game board, player panels, and other UI components.

 * Created by Rachele Grigoli and modified by Kristian Wright and Collin Cabral-Castro
 */

package View;

import Model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GUI extends JFrame {

    public GUI() {
        setTitle("Monopoly Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Initialize dice instance

        String[] playerNames = {"Stacy", "Alex", "Jamie", "Jordan"};
        String[] playerTokens = new String[playerNames.length];

        // Initialize class field instead of local variable
        GameBoard sharedGameBoard = new GameBoard(new ArrayList<>(), false, new Bank(new ArrayList<>()));

        List<String> availableTokens = sharedGameBoard.getAvailableTokens();

        for (int i = 0; i < playerNames.length; i++) {
            String selectedToken = showTokenSelectionPopup(playerNames[i], availableTokens);
            playerTokens[i] = selectedToken;
            availableTokens.remove(selectedToken); // Avoids token duplicates
        }

        for (int i = 0; i < playerNames.length; i++) {
            Player player = new Player(playerNames[i], playerTokens[i], sharedGameBoard);
            sharedGameBoard.getPlayers().add(player);
        }

        String[] selectedPlayerTokens = new String[playerNames.length];
        System.arraycopy(playerTokens, 0, selectedPlayerTokens, 0, playerTokens.length);

        // Create game board panel first
        GameBoardPanel gameBoardPanel = new GameBoardPanel(sharedGameBoard, selectedPlayerTokens);
        add(gameBoardPanel, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane();
        // Use the new DicePanel class here
        tabbedPane.addTab("Your Turn", new DicePanel(sharedGameBoard, gameBoardPanel));
        tabbedPane.addTab("Bank", new JPanel());

        for (String playerName : playerNames) {
            tabbedPane.addTab(playerName, new JPanel());
        }

        tabbedPane.setPreferredSize(new Dimension(300, 900));
        tabbedPane.setBackground(new Color(217, 233, 211));
        add(tabbedPane, BorderLayout.EAST);

        setVisible(true);
    }

    private String showTokenSelectionPopup(String playerName, List<String> availableTokens) {
        String[] tokenOptions = availableTokens.toArray(new String[0]);
        return (String) JOptionPane.showInputDialog(
                this,
                playerName + ", choose your token:",
                "Token Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                tokenOptions,
                tokenOptions[0]
        );
    }

    static class GameBoardPanel extends JPanel {
        private final List<Space> spaces;

        /**
         * Creates visible panel for game board
         */
        public GameBoardPanel(GameBoard gameBoard, String[] selectedPlayerTokens) {
            // setPreferredSize(new Dimension(900, 900));
            setBackground(Color.WHITE);
            setLayout(null);

            // Pass an empty list of players instead of null
            spaces = gameBoard.getSpaces();

            addSpacesToBoard();

            if (selectedPlayerTokens != null && selectedPlayerTokens.length > 0) {
                addTokens(selectedPlayerTokens);
            }

            revalidate();
            repaint();
        }

        /**
         * positioning spaces (buttons) around the board
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

        private void addTokens(String[] selectedPlayerTokens) {
            int tokenWidth = 55;

            for (int i = 0; i < selectedPlayerTokens.length; i++) {
                String tokenName = selectedPlayerTokens[i] + "Token"; // e.g., "CarToken", "DogToken"
                int playerStartPosition = 0; // All players start on "GO" space (index 0)
                Point coords = getCoordinatesForPosition(playerStartPosition); // Get pixel coordinates for that space
                int offset = i * 10; // offsetting token on the board from the rest of the pieces
                addToken(tokenName, coords.x + offset, coords.y, tokenWidth);
            }
        }

        private void addToken(String pngName, int xOffset, int yOffset, int tokenWidth) {
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
        }

        /**
         * assigns positions for tokens to jump around on thorughout the game baord spaces
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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
