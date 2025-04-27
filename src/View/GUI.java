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
    private final DicePanel dicePanel;
    private BankPanel bankPanel;
    private List<PlayerPanel> playerPanels;

    public GUI() {
        setTitle("Monopoly Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

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

        for (int i = 0; i < playerNames.length; i++) {
            Player player = sharedGameBoard.getPlayers().get(i);
            PlayerPanel playerPanel = new PlayerPanel(player);
            playerPanels.add(playerPanel);
            tabbedPane.addTab(player.getName(), new JScrollPane(playerPanel));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
