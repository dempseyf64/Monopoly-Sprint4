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
        getContentPane().removeAll();

        String numPlayersStr = JOptionPane.showInputDialog(
                this,
                "Enter number of players (2–8). If you want to play Against a Computer press 1:",
                "Player Count",
                JOptionPane.QUESTION_MESSAGE
        );

        if (numPlayersStr == null) {
            System.exit(0);  // User cancelled
        }

        int numPlayers;
        try {
            numPlayers = Integer.parseInt(numPlayersStr);
            if (numPlayers < 1 || numPlayers > 8) { // Allow 1 player for this case
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number of players. Exiting...");
            System.exit(0);
            return;
        }

        List<String> playerNamesList = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            String name = showPlayerNamePanel("Player " + (i + 1));
            if (name == null || name.isEmpty()) {
                break;
            }
            playerNamesList.add(name);
        }

        if (playerNamesList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Game canceled (must have at least 1 player).", "Cancel", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
            return;
        }

        GameBoard sharedGameBoard = new GameBoard(new ArrayList<>(), false, new Bank(new ArrayList<>()));
        String[] playerTokens = new String[playerNamesList.size() + (playerNamesList.size() == 1 ? 1 : 0)];
        List<String> availableTokens = sharedGameBoard.getAvailableTokens();

        for (int i = 0; i < playerNamesList.size(); i++) {
            String selectedToken = showTokenSelectionPopup(playerNamesList.get(i), availableTokens);
            if (selectedToken == null) {
                JOptionPane.showMessageDialog(this, "Token selection was cancelled for Player " + (i + 1), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            playerTokens[i] = selectedToken;
            availableTokens.remove(selectedToken);
        }

        // Ensure the human player is added first
        for (int i = 0; i < playerNamesList.size(); i++) {
            Player player = new Player(playerNamesList.get(i), playerTokens[i], sharedGameBoard);
            sharedGameBoard.getPlayers().add(player);
        }

        // Add the computer player if there is only one human player
        if (playerNamesList.size() == 1) {
            String computerToken = availableTokens.get(0); // Assign the first available token to the computer
            playerTokens[1] = computerToken;
            availableTokens.remove(computerToken);
            ComputerPlayer computerPlayer = new ComputerPlayer("Computer", computerToken, sharedGameBoard);
            sharedGameBoard.getPlayers().add(computerPlayer);
        }

        gameBoardPanel = new GameBoardPanel(sharedGameBoard, playerTokens);
        add(gameBoardPanel, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel turnPanel = new JPanel(new BorderLayout());
        dicePanel = new DicePanel(sharedGameBoard, gameBoardPanel);
        turnPanel.add(dicePanel, BorderLayout.NORTH);

        JPanel actionButtonsPanel = new JPanel();
        actionButtonsPanel.setLayout(new GridLayout(5, 1, 0, 10));
        actionButtonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        JButton endTurnButton = new JButton("End Turn");
        JButton buyHouseButton = new JButton("Buy House");
        JButton buyHotelButton = new JButton("Buy Hotel");
        JButton mortgageButton = new JButton("Mortgage Property");
        JButton tradeButton = new JButton("Start Trade");

        endTurnButton.setFont(buttonFont);
        endTurnButton.addActionListener(e -> {
            Player currentPlayer = dicePanel.getGameBoard().getPlayers().get(dicePanel.getCurrentPlayerIndex());

            // Disable the button if it's the computer's turn
            if (currentPlayer instanceof ComputerPlayer) {
                JOptionPane.showMessageDialog(this, "It's the computer's turn. Please wait.");
                return;
            }

            // Call the endTurn logic from DicePanel
            dicePanel.endTurn();
        });
        actionButtonsPanel.add(endTurnButton);
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
        tabbedPane.addTab("Your Turn", turnPanel);
        bankPanel = new BankPanel(sharedGameBoard);
        tabbedPane.addTab("Bank", new JScrollPane(bankPanel));

        playerPanels = new ArrayList<>();
        for (int i = 0; i < sharedGameBoard.getPlayers().size(); i++) {
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
