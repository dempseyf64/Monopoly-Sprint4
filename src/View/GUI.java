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
    private  DicePanel dicePanel;
    private BankPanel bankPanel;
    private List<PlayerPanel> playerPanels;
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

        String numPlayersStr = JOptionPane.showInputDialog(
                this,
                "Enter number of players (2–8):",
                "Player Count",
                JOptionPane.QUESTION_MESSAGE
        );

        if (numPlayersStr == null) {
            System.exit(0);  // User cancelled
        }

        int numPlayers;
        try {
            numPlayers = Integer.parseInt(numPlayersStr);
            if (numPlayers < 2 || numPlayers > 8) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number of players. Exiting...");
            System.exit(0);
            return;
        }

        // Collect player names from the custom name entry panel
        List<String> playerNamesList = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) { //max 8 players
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

        // Create a panel for the "Your Turn" tab with dice and action buttons
        JPanel turnPanel = new JPanel(new BorderLayout());
        dicePanel = new DicePanel(sharedGameBoard, gameBoardPanel);
        turnPanel.add(dicePanel, BorderLayout.NORTH);

        // Create panel for action buttons
        JPanel actionButtonsPanel = new JPanel();
        actionButtonsPanel.setLayout(new GridLayout(5, 1, 0, 10));
        actionButtonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font buttonFont = new Font("Arial", Font.BOLD, 16);

// Create buttons
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

// Add action listeners
        endTurnButton.addActionListener(e -> {
            int currentPlayerIndex = dicePanel.getCurrentPlayerIndex();
            int nextPlayerIndex = (currentPlayerIndex + 1) % sharedGameBoard.getPlayers().size();
            dicePanel.setCurrentPlayerIndex(nextPlayerIndex);
            // No need to explicitly enable the roll button as setCurrentPlayerIndex does this
            System.out.println("Turn ended. Next player: " +
                    sharedGameBoard.getPlayers().get(nextPlayerIndex).getName());
        });
        buyHouseButton.addActionListener(e -> System.out.println("Buy House clicked"));
        buyHotelButton.addActionListener(e -> System.out.println("Buy Hotel clicked"));
        mortgageButton.addActionListener(e -> System.out.println("Mortgage Property clicked"));
        tradeButton.addActionListener(e -> System.out.println("Start Trade clicked"));

// Add buttons to the panel
        actionButtonsPanel.add(endTurnButton);
        actionButtonsPanel.add(buyHouseButton);
        actionButtonsPanel.add(buyHotelButton);
        actionButtonsPanel.add(mortgageButton);
        actionButtonsPanel.add(tradeButton);

        turnPanel.add(actionButtonsPanel, BorderLayout.CENTER);

        tabbedPane.addTab("Your Turn", turnPanel);
        bankPanel = new BankPanel(sharedGameBoard);
        tabbedPane.addTab("Bank", new JScrollPane(bankPanel));

        playerPanels = new ArrayList<>();

        // Add Player Panels
        for (int i = 0; i < playerNames.length; i++) {
            Player player = sharedGameBoard.getPlayers().get(i);
            PlayerPanel playerPanel = new PlayerPanel(player);
            playerPanels.add(playerPanel);
            tabbedPane.addTab(player.getName(), new JScrollPane(playerPanel));
        }


        tabbedPane.setPreferredSize(new Dimension(600, 900));
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
