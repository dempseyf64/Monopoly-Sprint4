package View;

import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ComputerPanel extends JPanel {
    private final GameBoard gameBoard;
    private final GameBoardPanel gameBoardPanel;
    private final Bank bank;
    private final Dice dice;
    private final JailSpace jailSpace;
    private final GUI parentGUI;

    public ComputerPanel(GameBoard gameBoard, GameBoardPanel gameBoardPanel, Bank bank, GUI parentGUI) {
        this.gameBoard = gameBoard;
        this.gameBoardPanel = gameBoardPanel;
        this.bank = bank;
        this.parentGUI = parentGUI;
        this.dice = Dice.getInstance();
        this.jailSpace = new JailSpace();
    }

    public void handleComputerTurn(ComputerPlayer computerPlayer, Runnable endTurnCallback) {
        // Disable user interaction
        parentGUI.getEndTurnButton().setEnabled(false);
        parentGUI.getDicePanel().getRollButton().setEnabled(false);

        // Check if the computer is in jail
        if (jailSpace.isInJail(computerPlayer)) {
            if (jailSpace.attemptJailRelease(computerPlayer, dice.rollDice())) {
                JOptionPane.showMessageDialog(this, computerPlayer.getName() + " got out of jail!");
            } else {
                JOptionPane.showMessageDialog(this, computerPlayer.getName() + " is still in jail.");
                endTurnCallback.run();
                return;
            }
        }

        int consecutiveDoubles = 0;

        while (true) {
            // Roll dice
            ArrayList<Integer> results = dice.rollDice();
            int dice1Value = results.get(0);
            int dice2Value = results.get(1);
            int totalRoll = dice1Value + dice2Value;
            boolean isDoubles = (dice1Value == dice2Value);

            // Update dice display
            updateDiceDisplay(dice1Value, dice2Value);

            // Move the computer player
            int currentPosition = computerPlayer.getPosition();
            int newPosition = (currentPosition + totalRoll) % 40;
            computerPlayer.setPosition(newPosition);
            movePlayerToken(computerPlayer, newPosition);

            // Get the space the computer landed on
            Space space = gameBoard.getSpace(newPosition);

            // Decision-making logic
            if (space instanceof PropertySpace propertySpace && !propertySpace.isOwned()) {
                if (computerPlayer.getMoney() >= propertySpace.getPrice()) {
                    propertySpace.buy(computerPlayer);
                    bank.collectFromPlayer(computerPlayer, propertySpace.getPrice());
                    JOptionPane.showMessageDialog(this,
                            computerPlayer.getName() + " bought " + propertySpace.getName() + " for $" + propertySpace.getPrice() + "!");
                }
            }

            if (isDoubles) {
                consecutiveDoubles++;
                JOptionPane.showMessageDialog(this,
                        computerPlayer.getName() + " rolled doubles and will roll again!");

                // Handle three consecutive doubles (go to jail)
                if (consecutiveDoubles == 3) {
                    JOptionPane.showMessageDialog(this,
                            computerPlayer.getName() + " rolled three doubles in a row and is sent to jail!");
                    jailSpace.sendToJail(computerPlayer);
                    movePlayerToken(computerPlayer, 10); // Move to jail position
                    endTurnCallback.run();
                    return;
                }
            } else {
                break; // Exit loop if no doubles
            }
        }

        // Re-enable user interaction and end the turn
        parentGUI.getEndTurnButton().setEnabled(true);
        parentGUI.getDicePanel().getRollButton().setEnabled(true);
        endTurnCallback.run();
    }

    private void movePlayerToken(Player player, int newPosition) {
        String tokenName = player.getToken();
        Point cords = gameBoardPanel.getCoordinatesForPosition(newPosition);
        JLabel tokenLabel = gameBoardPanel.getPlayerToken(tokenName);

        if (tokenLabel != null) {
            tokenLabel.setLocation(cords.x, cords.y);
        }

        gameBoardPanel.revalidate();
        gameBoardPanel.repaint();
    }

    private void updateDiceDisplay(int dice1Value, int dice2Value) {
        // Optional: Update dice display in the UI if needed
        System.out.println("Computer rolled: " + dice1Value + " and " + dice2Value);
    }
}
