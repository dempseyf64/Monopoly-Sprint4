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

        for (int i = 0; i < playerNames.length; i++) {
            playerTokens[i] = showTokenSelectionPopup(playerNames[i]);
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

    private String showTokenSelectionPopup(String playerName) {
        String[] tokenOptions = {"Car", "Dog", "Hat", "Iron", "Shoe", "Thimble"};
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
            setPreferredSize(new Dimension(900, 900));
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
            int x = 0, y = 0;

            for (int i = 0; i < 10; i++) {
                addSpaceButton(spaces.get(i), x, y);
                x += SQUARE_SIZE;
            }

            x -= SQUARE_SIZE;
            y += SQUARE_SIZE;
            for (int i = 10; i < 20; i++) {
                addSpaceButton(spaces.get(i), x, y);
                y += SQUARE_SIZE;
            }

            y -= SQUARE_SIZE;
            x -= SQUARE_SIZE;
            for (int i = 20; i < 30; i++) {
                addSpaceButton(spaces.get(i), x, y);
                x -= SQUARE_SIZE;
            }

            x += SQUARE_SIZE;
            y -= SQUARE_SIZE;
            for (int i = 30; i < 40; i++) {
                addSpaceButton(spaces.get(i), x, y);
                y -= SQUARE_SIZE;
            }
        }

        private void addSpaceButton(Space space, int x, int y) {
            JButton spaceButton = new JButton("<html><center>" + space.getName() + "</center></html>");
            spaceButton.setBounds(x, y, SQUARE_SIZE, SQUARE_SIZE);
            spaceButton.setFont(new Font("Arial", Font.PLAIN, 10));
            spaceButton.setOpaque(true);

            if (space instanceof PropertySpace) {
                spaceButton.setBackground(new Color(153, 102, 51)); // Brown for properties
                spaceButton.setToolTipText("Property: " + space.getName());
            } else if (space instanceof ChanceSpace) {
                spaceButton.setBackground(new Color(255, 165, 0)); // Orange for chance
                spaceButton.setToolTipText("Chance: Draw a card!");
            } else if (space instanceof CommunityChestSpace) {
                spaceButton.setBackground(new Color(255, 215, 0)); // Gold for community chest
                spaceButton.setToolTipText("Community Chest: Draw a card!");
            } else if (space instanceof TaxSpace) {
                spaceButton.setBackground(new Color(255, 0, 0)); // Red for taxes
                spaceButton.setToolTipText("Tax: Pay " + ((TaxSpace) space).getTaxAmount());
            } else if (space instanceof RailroadSpace) {
                spaceButton.setBackground(new Color(0, 0, 0)); // Black for railroads
                spaceButton.setToolTipText("Railroad: " + space.getName());
            } else if (space instanceof UtilitySpace) {
                spaceButton.setBackground(new Color(0, 255, 255)); // Cyan for utilities
                spaceButton.setToolTipText("Utility: " + space.getName());
            } else {
                spaceButton.setBackground(new Color(200, 200, 200)); // Default color
                spaceButton.setToolTipText(space.getName());
            }

            spaceButton.addActionListener(e -> {
                JDialog dialog = new JDialog();
                dialog.setTitle(space.getName());
                dialog.setSize(300, 200);
                dialog.setLocationRelativeTo(null);
                dialog.add(new JLabel("<html><center>Details about " + space.getName() + "</center></html>", SwingConstants.CENTER));
                dialog.setVisible(true);
            });

            add(spaceButton);
        }

        private void addTokens(String[] selectedPlayerTokens) {
            int[] xOffsets = {760, 760, 760, 760};
            int[] yOffsets = {740, 710, 680, 650};
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