package View;

import Model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The GUI class represents the main graphical user interface for the Monopoly game.
 * It initializes the game board, player panels, and other UI components.
 * <p>
 * Created by Rachele Grigoli and modified by Kristian Wright and Collin Cabral-Castro
 */
public class GUI extends JFrame {
    private DicePanel dicePanel;
    private GameBoardPanel gameBoardPanel;

    public GUI() {
        setTitle("Monopoly Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Start the game by showing the name input panel for Player 1 directly
        startGame();

        setVisible(true);
    }

    private void startGame() {
        // Remove the previous content (if any) and prepare for player name input
        getContentPane().removeAll();

        // Collect player names from the custom name entry panel
        List<String> playerNamesList = new ArrayList<>();
        for (int i = 0; i < 8; i++) { // maximum 8 players
            String name = showPlayerNamePanel("Player " + (i + 1));  // Call custom panel for name entry
            if (name == null || name.isEmpty()) {
                break;
            }
            playerNamesList.add(name);  // Store the entered name
        }

        // Check if there are less than 2 players
        if (playerNamesList.size() < 2) {
            JOptionPane.showMessageDialog(this, "Game canceled (must have at least 2 players).", "Cancel", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);  // Exit the application
            return;
        }

        // Convert the list to an array
        String[] playerNames = playerNamesList.toArray(new String[0]);

        // Initialize game board
        GameBoard sharedGameBoard = new GameBoard(new ArrayList<>(), false, new Bank(new ArrayList<>()));
        String[] playerTokens = new String[playerNames.length];
        List<String> availableTokens = sharedGameBoard.getAvailableTokens();

        // Assign tokens to players using the token selection popup
        for (int i = 0; i < playerNames.length; i++) {
            String selectedToken = showTokenSelectionPopup(playerNames[i], availableTokens);
            if (selectedToken == null) {
                JOptionPane.showMessageDialog(this, "Token selection was cancelled for Player " + (i + 1), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            playerTokens[i] = selectedToken;
            availableTokens.remove(selectedToken); // Avoid token duplicates
        }

        // Create players
        for (int i = 0; i < playerNames.length; i++) {
            Player player = new Player(playerNames[i], playerTokens[i], sharedGameBoard);
            sharedGameBoard.getPlayers().add(player);
        }

        // Create game board panel and add it to the frame
        gameBoardPanel = new GameBoardPanel(sharedGameBoard, playerTokens);
        add(gameBoardPanel, BorderLayout.CENTER);

        // Set up tabs and other UI components
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Your Turn", createTurnPanel(sharedGameBoard));
        tabbedPane.addTab("Bank", new JPanel());

        // Add Player Panels
        for (int i = 0; i < playerNames.length; i++) {
            tabbedPane.addTab(playerNames[i], new PlayerPanel(playerNames[i], playerTokens[i]));
        }

        tabbedPane.setPreferredSize(new Dimension(300, 900));
        tabbedPane.setBackground(new Color(217, 233, 211));
        add(tabbedPane, BorderLayout.EAST);

        revalidate();
        repaint();
    }

    private JPanel createTurnPanel(GameBoard sharedGameBoard) {
        JPanel turnPanel = new JPanel(new BorderLayout());
        dicePanel = new DicePanel(sharedGameBoard, gameBoardPanel);
        turnPanel.add(dicePanel, BorderLayout.NORTH);

        // Action buttons panel (same as your current implementation)
        JPanel actionButtonsPanel = new JPanel();
        actionButtonsPanel.setLayout(new GridLayout(5, 1, 0, 10));
        actionButtonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        JButton endTurnButton = new JButton("End Turn");
        JButton buyHouseButton = new JButton("Buy House");
        JButton buyHotelButton = new JButton("Buy Hotel");
        JButton mortgageButton = new JButton("Mortgage Property");
        JButton tradeButton = new JButton("Start Trade");

        // Set font for all buttons
        endTurnButton.setFont(buttonFont);
        buyHouseButton.setFont(buttonFont);
        buyHotelButton.setFont(buttonFont);
        mortgageButton.setFont(buttonFont);
        tradeButton.setFont(buttonFont);

        actionButtonsPanel.add(endTurnButton);
        actionButtonsPanel.add(buyHouseButton);
        actionButtonsPanel.add(buyHotelButton);
        actionButtonsPanel.add(mortgageButton);
        actionButtonsPanel.add(tradeButton);
        turnPanel.add(actionButtonsPanel, BorderLayout.CENTER);

        return turnPanel;
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

    private String showPlayerNamePanel(String playerNamePrompt) {
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(150, 30));
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));

        Object[] message = {
                playerNamePrompt + " (Enter Name):",
                nameField
        };

        String name = "";

        while (name.trim().isEmpty()) {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    "Player Name",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (option == JOptionPane.OK_OPTION) {
                name = nameField.getText().trim();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (option == JOptionPane.CANCEL_OPTION) {
                return null;  // If user cancels, exit gracefully
            }
        }

        return name;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
