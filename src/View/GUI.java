package View;

import Model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The GUI class represents the main graphical user interface for the Monopoly game.
 * It initializes the game board, player panels, and other UI components.

 * Created by Rachele Grigoli and modified by Kristian Wright and Collin Cabral-Castro
 */
public class GUI extends JFrame {

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
        // Use the DicePanel class here
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
