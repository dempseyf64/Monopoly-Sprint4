/**
 * The GUI class represents the main graphical user interface for the Monopoly game.
 * It initializes the game board, player panels, and other UI components.
 *
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

    private JPanel gameBoardPanel;

    public GUI() {
        setTitle("Monopoly Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        String[] playerNames = {"Stacy", "Alex", "Jamie", "Jordan"};
        String[] playerTokens = new String[playerNames.length];

        GameBoard gameBoard = new GameBoard(new ArrayList<>(), false, new Bank(new ArrayList<>()));

        for (int i = 0; i < playerNames.length; i++) {
            playerTokens[i] = showTokenSelectionPopup(playerNames[i], gameBoard.getAvailableTokens());
        }

        String[] selectedPlayerTokens = new String[playerNames.length];
        System.arraycopy(playerTokens, 0, selectedPlayerTokens, 0, playerTokens.length);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Your Turn", new JPanel());
        tabbedPane.addTab("Bank", new JPanel());

        gameBoardPanel = new GameBoardPanel(selectedPlayerTokens);
        add(gameBoardPanel, BorderLayout.CENTER);

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
        private static final int SQUARE_SIZE = 82;
        private final List<Space> spaces;

        public GameBoardPanel(String[] selectedPlayerTokens) {
            // setPreferredSize(new Dimension(900, 900));
            setBackground(Color.WHITE);
            setLayout(null);

            // Pass an empty list of players instead of null
            GameBoard gameBoard = new GameBoard(new ArrayList<>(), false, new Bank(new ArrayList<>()));
            spaces = gameBoard.getSpaces();

            addSpacesToBoard();

            if (selectedPlayerTokens != null && selectedPlayerTokens.length > 0) {
                addTokens(selectedPlayerTokens);
            }

            revalidate();
            repaint();
        }

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
            addSpaceButton(spaces.get(0), x - CornerSpaceSize, y, CornerSpaceSize, CornerSpaceSize); // GO
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
            int[] xOffsets = {900, 900, 900, 900};
            int[] yOffsets = {780, 750, 720, 690};
            int tokenWidth = 55;

            for (int i = 0; i < selectedPlayerTokens.length; i++) {
                String tokenName = selectedPlayerTokens[i] + "Token";
                addToken(tokenName, xOffsets[i], yOffsets[i], tokenWidth);
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
            add(tokenLabel);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}