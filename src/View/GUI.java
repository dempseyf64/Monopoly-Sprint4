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
    private Bank bank;
    private  DicePanel dicePanel;
    private BankPanel bankPanel;
    private List<PlayerPanel> playerPanels;
    private GameBoardPanel gameBoardPanel;
    private GameBoard sharedGameBoard;
    private JButton endTurnButton;


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
        // Remove the previous content (if any) and prepare for game mode selection
        getContentPane().removeAll();

        // Prompt the user to select the game mode
        String[] options = {"Quick Game (vs Computer)", "Regular Game (with Friends)"};
        int gameMode = JOptionPane.showOptionDialog(
                this,
                "Select Game Mode:",
                "Game Mode Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (gameMode == -1) {
            System.exit(0); // User cancelled
        }

        if (gameMode == 0) {
            // Quick Game with Computer
            quickGame();
        } else {
            // Regular Game with Friends
            regularGame();
        }

        setVisible(true);
    }

    private void quickGame() {
        bank = new Bank(new ArrayList<>()); // Assign to the GUI's bank field
        sharedGameBoard = new GameBoard(new ArrayList<>(), false, bank);

        // After GameBoard initializes all spaces
        List<Property> propertyList = new ArrayList<>();
        for (Space space : sharedGameBoard.getSpaces()) {
            if (space instanceof Property property) {
                propertyList.add(property);
            }
        }

        // Now set the bank’s list of properties
        bank.setProperties(propertyList);

        // Create one human player and one computer player
        String humanPlayerName = showPlayerNamePanel("Player 1");
        if (humanPlayerName == null || humanPlayerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Game canceled.", "Cancel", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
            return;
        }

        String humanPlayerToken = showTokenSelectionPopup(humanPlayerName, sharedGameBoard.getAvailableTokens());
        if (humanPlayerToken == null) {
            JOptionPane.showMessageDialog(this, "Token selection was cancelled.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Player humanPlayer = new Player(humanPlayerName, humanPlayerToken, sharedGameBoard);
        humanPlayer.setPlayerIndex(0);
        sharedGameBoard.getPlayers().add(humanPlayer);

        String computerPlayerName = "Computer";
        String computerPlayerToken = sharedGameBoard.getAvailableTokens().get(0); // Assign the first available token
        ComputerPlayer computerPlayer = new ComputerPlayer(computerPlayerName, computerPlayerToken, sharedGameBoard);
        computerPlayer.setPlayerIndex(1);
        sharedGameBoard.getPlayers().add(computerPlayer);

        // Create game board panel and add it to the frame
        gameBoardPanel = new GameBoardPanel(sharedGameBoard, new String[]{humanPlayerToken, computerPlayerToken});
        add(gameBoardPanel, BorderLayout.CENTER);

        setupGameUI();
    }

    private void regularGame() {
        bank = new Bank(new ArrayList<>()); // Assign to the GUI's bank field
        sharedGameBoard = new GameBoard(new ArrayList<>(), false, bank);

        // After GameBoard initializes all spaces
        List<Property> propertyList = new ArrayList<>();
        for (Space space : sharedGameBoard.getSpaces()) {
            if (space instanceof Property property) {
                propertyList.add(property);
            }
        }

        // Now set the bank’s list of properties
        bank.setProperties(propertyList);

        // Ask the user for the number of players
        String numPlayersStr = JOptionPane.showInputDialog(
                this,
                "Enter the number of players (2–8):",
                "Player Count",
                JOptionPane.QUESTION_MESSAGE
        );

        if (numPlayersStr == null || numPlayersStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Game canceled.", "Cancel", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
            return;
        }

        int numPlayers;
        try {
            numPlayers = Integer.parseInt(numPlayersStr);
            if (numPlayers < 2 || numPlayers > 8) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number of players. Please enter a number between 2 and 8.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Collect player names
        String[] playerNames = new String[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            String name = showPlayerNamePanel("Player " + (i + 1));
            if (name == null || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Game canceled.", "Cancel", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
                return;
            }
            playerNames[i] = name;
        }

        // Initialize game board
        String[] playerTokens = new String[numPlayers];
        List<String> availableTokens = sharedGameBoard.getAvailableTokens();

        // Assign tokens to players using the token selection popup
        for (int i = 0; i < numPlayers; i++) {
            String selectedToken = showTokenSelectionPopup(playerNames[i], availableTokens);
            if (selectedToken == null) {
                JOptionPane.showMessageDialog(this, "Token selection was cancelled for Player " + (i + 1), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            playerTokens[i] = selectedToken;
            availableTokens.remove(selectedToken); // Avoid token duplicates
        }

        // Create players
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(playerNames[i], playerTokens[i], sharedGameBoard);
            player.setPlayerIndex(i);
            sharedGameBoard.getPlayers().add(player);
        }

        // Create game board panel and add it to the frame
        gameBoardPanel = new GameBoardPanel(sharedGameBoard, playerTokens);
        add(gameBoardPanel, BorderLayout.CENTER);

        setupGameUI();
    }

    private void setupGameUI() {
        // Set up tabs and other UI components
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create a panel for the "Your Turn" tab with dice and action buttons
        JPanel turnPanel = new JPanel(new BorderLayout());
        dicePanel = new DicePanel(sharedGameBoard, gameBoardPanel, bank, this);
        turnPanel.add(dicePanel, BorderLayout.NORTH);

        // Create panel for action buttons
        JPanel actionButtonsPanel = new JPanel();
        actionButtonsPanel.setLayout(new GridLayout(5, 1, 0, 10));
        actionButtonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        // Assign the class-level endTurnButton field
        endTurnButton = new JButton("End Turn");
        endTurnButton.setFont(buttonFont);
        endTurnButton.addActionListener(e -> {
            int currentPlayerIndex = dicePanel.getCurrentPlayerIndex();
            int nextPlayerIndex = (currentPlayerIndex + 1) % sharedGameBoard.getPlayers().size();
            dicePanel.setCurrentPlayerIndex(nextPlayerIndex);
            System.out.println("Turn ended. Next player: " +
                    sharedGameBoard.getPlayers().get(nextPlayerIndex).getName());
        });

        // Add other buttons
        JButton buyHouseButton = new JButton("Buy House");
        JButton buyHotelButton = new JButton("Buy Hotel");
        JButton mortgageButton = new JButton("Mortgage Property");
        JButton tradeButton = new JButton("Start Trade");

        // Set font for all buttons
        buyHouseButton.setFont(buttonFont);
        buyHotelButton.setFont(buttonFont);
        mortgageButton.setFont(buttonFont);
        tradeButton.setFont(buttonFont);

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

        // Add Player Panels
        playerPanels = new ArrayList<>();
        for (Player player : sharedGameBoard.getPlayers()) {
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

    public JButton getEndTurnButton() {
        return endTurnButton;
    }

    private JPanel createTurnPanel(GameBoard sharedGameBoard) {
        JPanel turnPanel = new JPanel(new BorderLayout());
        dicePanel = new DicePanel(sharedGameBoard, gameBoardPanel, bank, this);
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

    public BankPanel getBankPanel() {
        return bankPanel;
    }

    public List<PlayerPanel> getPlayerPanels() {
        return playerPanels;
    }


    /**
     * Handles buying a house by the current player.
     * Relies on the Bank to process the purchase.
     */
    private void buyHouse(Player player) {
        List<Property> eligibleProperties = new ArrayList<>();

        for (Property property : player.getProperties()) {
            if (property instanceof PropertySpace propSpace) {
                if (propSpace.getHouseCount() < 4 && !propSpace.hasHotel()) {
                    if (player.ownsFullColorGroup(propSpace.getColorGroup())) {
                        eligibleProperties.add(propSpace);
                    }
                }
            }
        }

        if (eligibleProperties.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No eligible properties to build houses on.", "No Properties", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Property selectedProperty = (Property) JOptionPane.showInputDialog(
                this,
                "Select a property to build a house:",
                "Build House",
                JOptionPane.QUESTION_MESSAGE,
                null,
                eligibleProperties.toArray(),
                eligibleProperties.get(0)
        );

        if (selectedProperty == null) {
            return; // Cancelled
        }

        if (selectedProperty instanceof PropertySpace propSpace) {
            boolean success = bank.buyHouse(player, propSpace);
            if (success) {
                JOptionPane.showMessageDialog(this, "Built a house on " + propSpace.getName() + "!");
                bankPanel.refreshProperties();
                playerPanels.get(player.getPlayerIndex()).refreshProperties();
            } else {
                JOptionPane.showMessageDialog(this, "Could not build a house on " + propSpace.getName() + ".", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public DicePanel getDicePanel() {
        return dicePanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
