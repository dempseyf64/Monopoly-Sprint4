package View;

import Model.Dice;
import Model.GameBoard;
import Model.JailSpace;
import Model.Player;
import Model.ComputerPlayer;
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
     * Button for paying jail fine
     */
    private final JButton payJailFineButton;

    /**
     * Reference to the game board panel for updating token positions
     */
    private final GameBoardPanel gameBoardPanel;

    /**
     * Counter for tracking consecutive doubles rolled by the current player
     */
    private int consecutiveDoublesCount = 0;

    /**
     * Reference to the jail space object
     */
    private final JailSpace jailSpace;

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

            Player currentPlayer = gameBoard.getPlayers().get(currentPlayerIndex);
            String playerStatus = "";

            if (jailSpace.isInJail(currentPlayer)) {
                int jailTurns = jailSpace.getJailTurns(currentPlayer);
                playerStatus = " (IN JAIL - turn " + jailTurns + "/3)";
                payJailFineButton.setVisible(true);
            } else {
                payJailFineButton.setVisible(false);
            }

            // Enable or disable the roll button based on the current player type
            // Enable for human players
            rollButton.setEnabled(!(currentPlayer instanceof ComputerPlayer)); // Disable for computer players

            diceResultLabel.setText("Current Player: " +
                    currentPlayer.getName() + playerStatus);
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
        this.jailSpace = new JailSpace(); // Create the jail space

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

        // Create control buttons panel
        JPanel controlPanel = new JPanel(new FlowLayout());

        rollButton = new JButton("Roll Dice");
        rollButton.setFont(new Font("Arial", Font.BOLD, 16));
        rollButton.addActionListener(e -> rollDiceAndMove());

        payJailFineButton = new JButton("Pay $50 Fine to Get Out of Jail");
        payJailFineButton.setFont(new Font("Arial", Font.BOLD, 14));
        payJailFineButton.addActionListener(e -> payJailFine());
        // Initially hidden
        payJailFineButton.setVisible(false);

        controlPanel.add(rollButton);
        controlPanel.add(payJailFineButton);

        diceResultLabel = new JLabel("Current Player: " +
                (gameBoard.getPlayers().isEmpty() ? "No players" :
                        gameBoard.getPlayers().get(currentPlayerIndex).getName()),
                SwingConstants.CENTER);

        add(controlPanel, BorderLayout.NORTH);
        add(diceImagesPanel, BorderLayout.CENTER);
        add(diceResultLabel, BorderLayout.SOUTH);
    }

    /**
     * Handles the player paying a fine to get out of jail
     */
    private void payJailFine() {
        Player currentPlayer = gameBoard.getPlayers().get(currentPlayerIndex);

        if (jailSpace.isInJail(currentPlayer)) {
            if (jailSpace.payFineToLeaveJail(currentPlayer)) {
                JOptionPane.showMessageDialog(this,
                        currentPlayer.getName() + " paid $50 to get out of jail!");
                diceResultLabel.setText(currentPlayer.getName() +
                        " paid the fine and is now free. Roll the dice.");
                rollButton.setEnabled(true);
                payJailFineButton.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Not enough money to pay the jail fine!");
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    currentPlayer.getName() + " is not in jail!");
            payJailFineButton.setVisible(false);
        }
    }

    /**
     * Handles the dice rolling action and player movement.
     * Updates the dice display, moves the player's token on the board,
     * and handles turn management including doubles and going to jail.
     * Implements the rule of going to jail after three consecutive doubles.
     */

    private void rollDiceAndMove() {
        Player currentPlayer = gameBoard.getPlayers().get(currentPlayerIndex);

        if (currentPlayer instanceof ComputerPlayer computerPlayer) {
            rollButton.setEnabled(false); // Disable roll button during computer's turn

            ArrayList<Integer> results = dice.rollDice();
            int dice1Value = results.get(0);
            int dice2Value = results.get(1);
            int totalRoll = dice1Value + dice2Value;

            // Update dice display
            updateDiceImages(dice1Value, dice2Value);

            // Move the computer player
            int currentPosition = computerPlayer.getPosition();
            int newPosition = (currentPosition + totalRoll) % 40;
            computerPlayer.setPosition(newPosition);
            movePlayerToken(computerPlayer, newPosition);

            // Get the space the computer landed on
            String spaceName = gameBoard.getSpace(newPosition).getName();

            // Display what the computer rolled and where it landed
            diceResultLabel.setText(computerPlayer.getName() + " rolled " +
                    dice1Value + " + " + dice2Value + " = " + totalRoll +
                    " and landed on " + spaceName + ".");

            // Create a modal dialog to block user interaction
            JDialog blockingDialog = new JDialog((Frame) null, "Computer's Turn", true);
            blockingDialog.setUndecorated(true);
            blockingDialog.setSize(300, 100);
            blockingDialog.setLocationRelativeTo(this);
            JLabel messageLabel = new JLabel("Computer is making a decision...", SwingConstants.CENTER);
            blockingDialog.add(messageLabel);

            // Show the dialog in a separate thread to avoid blocking the UI
            SwingUtilities.invokeLater(() -> blockingDialog.setVisible(true));
            // Add a delay to allow the user to see the result
            Timer timer = new Timer(3000, e -> {
                blockingDialog.dispose(); // Close the blocking dialog

                // Make a decision (e.g., buy property)
                computerPlayer.makeDecision();

                // Automatically end the turn
                SwingUtilities.invokeLater(() -> {
                    endTurn();
                    rollButton.setEnabled(true); // Re-enable roll button for the user's turn
                });
            });
            timer.setRepeats(false);
            timer.start();
            return;
        }

        // Logic for human players
        ArrayList<Integer> results = dice.rollDice();
        int dice1Value = results.get(0);
        int dice2Value = results.get(1);
        int totalRoll = dice1Value + dice2Value;
        boolean isDoubles = (dice1Value == dice2Value);

        // Update dice display
        updateDiceImages(dice1Value, dice2Value);

        // Check if player is in jail
        if (jailSpace.isInJail(currentPlayer)) {
            if (jailSpace.attemptJailRelease(currentPlayer, results)) {
                movePlayerToken(currentPlayer, currentPlayer.getPosition());
                String releaseMethod = isDoubles ? "by rolling doubles" : "after 3 turns";
                diceResultLabel.setText(currentPlayer.getName() + " got out of jail " +
                        releaseMethod + " and moved " + totalRoll + " spaces!");
                rollButton.setEnabled(false);
                payJailFineButton.setVisible(false);
            } else {
                int jailTurn = jailSpace.getJailTurns(currentPlayer);
                diceResultLabel.setText(currentPlayer.getName() + " is still in jail. Jail turn " +
                        jailTurn + "/3");
                rollButton.setEnabled(false);
                payJailFineButton.setVisible(true);
            }
            return;
        }

        // Not in jail - proceed with normal turn
        if (isDoubles) {
            consecutiveDoublesCount++;
        } else {
            consecutiveDoublesCount = 0;
        }

        if (consecutiveDoublesCount >= 3) {
            consecutiveDoublesCount = 0;
            rollButton.setEnabled(false);
            jailSpace.sendToJail(currentPlayer);
            movePlayerToken(currentPlayer, 10);
            diceResultLabel.setText(currentPlayer.getName() + " rolled three doubles in a row! Go to Jail!");
            payJailFineButton.setVisible(true);
            JOptionPane.showMessageDialog(this,
                    currentPlayer.getName() + " rolled three doubles in a row! Go to Jail!");
            return;
        }

        int currentPosition = currentPlayer.getPosition();
        int newPosition = (currentPosition + totalRoll) % 40;
        currentPlayer.setPosition(newPosition);
        movePlayerToken(currentPlayer, newPosition);

        // Get the space the player landed on
        String spaceName = gameBoard.getSpace(newPosition).getName();

        // Display what the player rolled and where they landed
        diceResultLabel.setText(currentPlayer.getName() + " rolled " +
                dice1Value + " + " + dice2Value + " = " + totalRoll +
                " and landed on " + spaceName + ".");

        if (isDoubles) {
            JOptionPane.showMessageDialog(this,
                    currentPlayer.getName() + " rolled doubles! Roll again. " +
                            "Consecutive doubles: " + consecutiveDoublesCount);
        } else {
            rollButton.setEnabled(false);
            diceResultLabel.setText(currentPlayer.getName() + " rolled " +
                    dice1Value + " + " + dice2Value + " = " + totalRoll +
                    " and landed on " + spaceName + ". Click End Turn when done.");
        }
    }

    /**
     * Ends the current player's turn and moves to the next player.
     */

    public void endTurn() {
        Player currentPlayer = gameBoard.getPlayers().get(currentPlayerIndex);

        // Generate a summary of the current player's turn
        String turnSummary = currentPlayer.getName() + " has ended their turn.";
        if (currentPlayer instanceof ComputerPlayer) {
            turnSummary = currentPlayer.getName() + " has ended its turn";
        }

        // Display the summary in a dialog
        JOptionPane.showMessageDialog(this, turnSummary, "Turn Summary", JOptionPane.INFORMATION_MESSAGE);

        // Move to the next player
        currentPlayerIndex = (currentPlayerIndex + 1) % gameBoard.getPlayers().size();
        setCurrentPlayerIndex(currentPlayerIndex);

        Player nextPlayer = gameBoard.getPlayers().get(currentPlayerIndex);
        if (nextPlayer instanceof ComputerPlayer) {
            // Add a delay before the computer rolls the dice
            Timer timer = new Timer(5000, e -> {
                rollDiceAndMove();
                rollButton.setEnabled(true); // Re-enable the button after the computer's turn
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            // Re-enable the button for the human player's turn
            rollButton.setEnabled(true);
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
        Point cords = gameBoardPanel.getCoordinatesForPosition(newPosition);

        System.out.println("Moving " + player.getName() + " (" + tokenName + ") to position " +
                newPosition + " at coordinates: " + cords.x + "," + cords.y);

        // Get the player's token label directly from the GameBoardPanel
        JLabel tokenLabel = gameBoardPanel.getPlayerToken(tokenName);

        if (tokenLabel != null) {
            // Move the token to the new position
            tokenLabel.setLocation(cords.x, cords.y);
            System.out.println("Found and moved token for " + player.getName());
        } else {
            System.out.println("Token not found for " + player.getName());
        }

        // Force UI update
        gameBoardPanel.revalidate();
        gameBoardPanel.repaint();
    }
    public GameBoard getGameBoard() {
        return gameBoard;
    }
}
