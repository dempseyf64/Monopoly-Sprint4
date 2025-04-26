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
 *
 * @author Kristian Wright
 */
public class DicePanel extends JPanel {
    /** Label displaying dice roll results and current player information */
    private final JLabel diceResultLabel;

    /** Label displaying the first dice image */
    private final JLabel dice1Label;

    /** Label displaying the second dice image */
    private final JLabel dice2Label;

    /** Reference to the singleton Dice object for generating dice rolls */
    private final Dice dice;

    /** Reference to the game board model containing game state information */
    private final GameBoard gameBoard;

    /** Index of the current player whose turn it is */
    private int currentPlayerIndex = 0;

    /** Reference to the game board panel for updating token positions */
    private final GUI.GameBoardPanel gameBoardPanel;

    /**
     * Constructs a DicePanel with the required game components.
     * Initializes the dice roll interface and connects it to the game board.
     *
     * @param gameBoard The game board model containing players and game state
     * @param gameBoardPanel The visual game board panel for updating token positions
     */
    public DicePanel(GameBoard gameBoard, GUI.GameBoardPanel gameBoardPanel) {
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

        JButton rollButton = new JButton("Roll Dice");
        rollButton.setFont(new Font("Arial", Font.BOLD, 16));

        diceResultLabel = new JLabel("Current Player: " +
                gameBoard.getPlayers().get(currentPlayerIndex).getName(),
                SwingConstants.CENTER);

        rollButton.addActionListener(e -> rollDiceAndMove());

        add(rollButton, BorderLayout.NORTH);
        add(diceImagesPanel, BorderLayout.CENTER);
        add(diceResultLabel, BorderLayout.SOUTH);
    }

    /**
     * Handles the dice rolling action and player movement.
     * Updates the dice display, moves the player's token on the board,
     * and handles turn management including doubles.
     */
    private void rollDiceAndMove() {
        // Get current player
        Player currentPlayer = gameBoard.getPlayers().get(currentPlayerIndex);

        // Roll the dice
        ArrayList<Integer> results = dice.rollDice();
        int dice1Value = results.get(0);
        int dice2Value = results.get(1);
        int totalRoll = dice1Value + dice2Value;

        // Update dice display
        updateDiceImages(dice1Value, dice2Value);

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

        // Switch to next player if not doubles
        if (dice1Value != dice2Value) {
            currentPlayerIndex = (currentPlayerIndex + 1) % gameBoard.getPlayers().size();
        } else {
            JOptionPane.showMessageDialog(this,
                    currentPlayer.getName() + " rolled doubles! Roll again.");
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
     * @param player The player whose token should be moved
     * @param newPosition The new board position (0-39) for the token
     */
    private void movePlayerToken(Player player, int newPosition) {
        // Use getToken() instead of getTokenName()
        String tokenName = player.getToken() + "Token";

        Point coords = gameBoardPanel.getCoordinatesForPosition(newPosition);

        // Find the token component and move it
        for (Component component : gameBoardPanel.getComponents()) {
            if (component instanceof JLabel label) {
                if (label.getIcon() != null) {
                    ImageIcon icon = (ImageIcon) label.getIcon();
                    if (icon.toString().contains(tokenName)) {
                        // Found the token, update its position
                        label.setLocation(coords.x, coords.y);
                        break;
                    }
                }
            }
        }
        gameBoardPanel.repaint();
    }
}
