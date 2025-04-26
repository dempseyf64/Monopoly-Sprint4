package View;

import Model.Dice;
import Model.GameBoard;
import Model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The DicePanel class represents the panel for dice-related functionality in the Monopoly game.
 * It provides a user interface for players to roll dice and displays the results.
 * This panel handles the movement of player tokens on the game board based on dice rolls.
 * It also implements Monopoly rules such as rolling doubles and going to jail after three consecutive doubles.
 *
 * @author Kristian Wright
 */
public class DicePanel extends JPanel {
    /**
     * Label displaying dice roll results and current player information
     */
    private final JLabel diceResultLabel;

    /**
     * Label displaying the first dice image
     */
    private final JLabel dice1Label;

    /**
     * Label displaying the second dice image
     */
    private final JLabel dice2Label;

    /**
     * Reference to the singleton Dice object for generating dice rolls
     */
    private final Dice dice;

    /**
     * Reference to the game board model containing game state information
     */
    private final GameBoard gameBoard;

    /**
     * Index of the current player whose turn it is
     */
    private int currentPlayerIndex = 0;

    /**
     * Button for rolling dice
     */
    private final JButton rollButton;

    /**
     * Reference to the game board panel for updating token positions
     */
    private final GameBoardPanel gameBoardPanel;

    /**
     * Counter for tracking consecutive doubles rolled by the current player
     */
    private int consecutiveDoublesCount = 0;

    /**
     * Gets the current player index
     *
     * @return The index of the current player
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Sets the current player index and updates UI.
     * Resets the consecutive doubles counter and re-enables the roll button.
     *
     * @param index The new player index
     */
    public void setCurrentPlayerIndex(int index) {
        if (index >= 0 && index < gameBoard.getPlayers().size()) {
            currentPlayerIndex = index;
            // Reset doubles counter for the new player
            consecutiveDoublesCount = 0;
            // Re-enable the roll button for the new player
            rollButton.setEnabled(true);
            diceResultLabel.setText("Current Player: " +
                    gameBoard.getPlayers().get(currentPlayerIndex).getName());
        }
    }

    /**
     * Constructs a DicePanel with the required game components.
     * Initializes the dice roll interface and connects it to the game board.
     *
     * @param gameBoard      The game board model containing players and game state
     * @param gameBoardPanel The visual game board panel for updating token positions
     */
    public DicePanel(GameBoard gameBoard, GameBoardPanel gameBoardPanel) {
        this.gameBoard = gameBoard;
        this.gameBoardPanel = gameBoardPanel;
        this.dice = Dice.getInstance();

        setLayout(new BorderLayout());

        JPanel diceImagesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dice1Label = new JLabel();
        dice2Label = new JLabel();

        // Initialize with placeholder images
        dice1Label.setPreferredSize(new Dimension(60, 60));
        dice2Label.setPreferredSize(new Dimension(60, 60));
        dice1Label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        dice2Label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        dice1Label.setHorizontalAlignment(SwingConstants.CENTER);
        dice2Label.setHorizontalAlignment(SwingConstants.CENTER);

        diceImagesPanel.add(dice1Label);
        diceImagesPanel.add(dice2Label);

        rollButton = new JButton("Roll Dice");
        rollButton.setFont(new Font("Arial", Font.BOLD, 16));

        diceResultLabel = new JLabel("Current Player: " +
                (gameBoard.getPlayers().isEmpty() ? "No players" :
                        gameBoard.getPlayers().get(currentPlayerIndex).getName()),
                SwingConstants.CENTER);

        rollButton.addActionListener(e -> rollDiceAndMove());

        add(rollButton, BorderLayout.NORTH);
        add(diceImagesPanel, BorderLayout.CENTER);
        add(diceResultLabel, BorderLayout.SOUTH);
    }

    /**
     * Handles the dice rolling action and player movement.
     * Updates the dice display, moves the player's token on the board,
     * and handles turn management including doubles and going to jail.
     * Implements the rule of going to jail after three consecutive doubles.
     */
    private void rollDiceAndMove() {
        if (gameBoard.getPlayers().isEmpty()) {
            diceResultLabel.setText("No players to assign tokens to.");
            return;
        }

        // Get current player
        Player currentPlayer = gameBoard.getPlayers().get(currentPlayerIndex);

        // Roll the dice
        ArrayList<Integer> results = dice.rollDice();
        int dice1Value = results.get(0);
        int dice2Value = results.get(1);
        int totalRoll = dice1Value + dice2Value;

        // Update dice display
        updateDiceImages(dice1Value, dice2Value);

        // Check if it's doubles
        boolean isDoubles = (dice1Value == dice2Value);

        if (isDoubles) {
            consecutiveDoublesCount++;
        } else {
            consecutiveDoublesCount = 0;
        }

        // If third consecutive doubles, go to jail
        if (consecutiveDoublesCount >= 3) {
            // Reset doubles counter
            consecutiveDoublesCount = 0;

            // Disable roll button
            rollButton.setEnabled(false);

            // Set player position to jail (position 10)
            currentPlayer.setPosition(10);

            // Move token to jail
            movePlayerToken(currentPlayer, 10);

            // Update message
            diceResultLabel.setText(currentPlayer.getName() + " rolled three doubles in a row! Go to Jail!");

            // Show message dialog
            JOptionPane.showMessageDialog(this,
                    currentPlayer.getName() + " rolled three doubles in a row! Go to Jail!");

            return;
        }

        // Calculate new position
        int currentPosition = currentPlayer.getPosition();
        int newPosition = (currentPosition + totalRoll) % 40;

        // Update player position
        currentPlayer.setPosition(newPosition);

        // Update result label
        diceResultLabel.setText(currentPlayer.getName() + " rolled " +
                dice1Value + " + " + dice2Value + " = " + totalRoll);

        // Move token on board
        movePlayerToken(currentPlayer, newPosition);

        // If doubles, allow player to roll again (unless it's the third double)
        if (isDoubles) {
            JOptionPane.showMessageDialog(this,
                    currentPlayer.getName() + " rolled doubles! Roll again. " +
                            "Consecutive doubles: " + consecutiveDoublesCount);
            // Keep roll button enabled
        } else {
            // Disable roll button until next player's turn
            rollButton.setEnabled(false);
            diceResultLabel.setText(currentPlayer.getName() + " rolled " +
                    dice1Value + " + " + dice2Value + " = " + totalRoll +
                    ". Click End Turn when done.");
        }
    }

    /**
     * Updates the dice images based on the rolled values.
     * Attempts to load dice face images from resources,
     * with a fallback to text representation if images aren't available.
     *
     * @param dice1Value The value of the first dice (1-6)
     * @param dice2Value The value of the second dice (1-6)
     */
    private void updateDiceImages(int dice1Value, int dice2Value) {
        try {
            ImageIcon icon1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/dice" + dice1Value + ".png")));
            ImageIcon icon2 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/dice" + dice2Value + ".png")));

            dice1Label.setIcon(icon1);
            dice2Label.setIcon(icon2);
        } catch (Exception e) {
            // Fallback if images not found
            dice1Label.setText(Integer.toString(dice1Value));
            dice2Label.setText(Integer.toString(dice2Value));
        }
    }

    /**
     * Moves a player's token on the game board to reflect their new position.
     * Finds the corresponding token label and updates its location.
     *
     * @param player      The player whose token should be moved
     * @param newPosition The new board position (0-39) for the token
     */
    private void movePlayerToken(Player player, int newPosition) {
        String tokenName = player.getToken();

        // Get the coordinates for the new position
        Point coords = gameBoardPanel.getCoordinatesForPosition(newPosition);

        System.out.println("Moving " + player.getName() + " (" + tokenName + ") to position " +
                newPosition + " at coordinates: " + coords.x + "," + coords.y);

        // Get the player's token label directly from the GameBoardPanel
        JLabel tokenLabel = gameBoardPanel.getPlayerToken(tokenName);

        if (tokenLabel != null) {
            // Move the token to the new position
            tokenLabel.setLocation(coords.x, coords.y);
            System.out.println("Found and moved token for " + player.getName());
        } else {
            System.out.println("Token not found for " + player.getName());
        }

        // Force UI update
        gameBoardPanel.revalidate();
        gameBoardPanel.repaint();
    }
}
