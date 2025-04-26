package View;

import Model.Dice;
import Model.GameBoard;
import Model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class DicePanel extends JPanel {
    private final JLabel diceResultLabel;
    private final JLabel dice1Label;
    private final JLabel dice2Label;
    private final Dice dice;
    private final GameBoard gameBoard;
    private int currentPlayerIndex = 0;
    private final GUI.GameBoardPanel gameBoardPanel;

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
