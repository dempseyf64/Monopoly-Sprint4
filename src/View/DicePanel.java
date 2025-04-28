package View;

import Model.*;

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
    private final JButton rollButton;
    private final JButton payJailFineButton;
    private final GameBoardPanel gameBoardPanel;
    private int consecutiveDoublesCount = 0;
    private final JailSpace jailSpace;
    private final Bank bank;
    private final GUI parentGUI;
    private final ComputerPanel computerPanel;

    public DicePanel(GameBoard gameBoard, GameBoardPanel gameBoardPanel, Bank bank, GUI parentGUI) {
        this.gameBoard = gameBoard;
        this.gameBoardPanel = gameBoardPanel;
        this.bank = bank;
        this.parentGUI = parentGUI;
        this.dice = Dice.getInstance();
        this.jailSpace = new JailSpace();
        this.computerPanel = new ComputerPanel(gameBoard, gameBoardPanel, bank, parentGUI);

        setLayout(new BorderLayout());

        JPanel diceImagesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dice1Label = new JLabel();
        dice2Label = new JLabel();

        dice1Label.setPreferredSize(new Dimension(60, 60));
        dice2Label.setPreferredSize(new Dimension(60, 60));
        dice1Label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        dice2Label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        dice1Label.setHorizontalAlignment(SwingConstants.CENTER);
        dice2Label.setHorizontalAlignment(SwingConstants.CENTER);

        diceImagesPanel.add(dice1Label);
        diceImagesPanel.add(dice2Label);

        JPanel controlPanel = new JPanel(new FlowLayout());

        rollButton = new JButton("Roll Dice");
        rollButton.setFont(new Font("Arial", Font.BOLD, 16));
        rollButton.addActionListener(e -> rollDiceAndMove());

        payJailFineButton = new JButton("Pay $50 Fine to Get Out of Jail");
        payJailFineButton.setFont(new Font("Arial", Font.BOLD, 14));
        payJailFineButton.addActionListener(e -> payJailFine());
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

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int index) {
        if (index >= 0 && index < gameBoard.getPlayers().size()) {
            currentPlayerIndex = index;
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

            rollButton.setEnabled(!(currentPlayer instanceof ComputerPlayer));

            diceResultLabel.setText("Current Player: " +
                    currentPlayer.getName() + playerStatus);
        }
    }

    public JButton getRollButton() {
        return rollButton;
    }

    private void rollDiceAndMove() {
        Player currentPlayer = gameBoard.getPlayers().get(currentPlayerIndex);

        if (currentPlayer instanceof ComputerPlayer computerPlayer) {
            rollButton.setEnabled(false); // Disable roll button
            parentGUI.getEndTurnButton().setEnabled(false); // Disable end turn button
            computerPanel.handleComputerTurn(computerPlayer, this::endTurn);
            return;
        }

        ArrayList<Integer> results = dice.rollDice();
        int dice1Value = results.get(0);
        int dice2Value = results.get(1);
        int totalRoll = dice1Value + dice2Value;
        boolean isDoubles = (dice1Value == dice2Value);

        updateDiceImages(dice1Value, dice2Value);

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
        handleLandingSpace(currentPlayer, newPosition);

        String spaceName = gameBoard.getSpace(newPosition).getName();

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

    public void endTurn() {
        Player currentPlayer = gameBoard.getPlayers().get(currentPlayerIndex);

        currentPlayerIndex = (currentPlayerIndex + 1) % gameBoard.getPlayers().size();
        setCurrentPlayerIndex(currentPlayerIndex);

        Player nextPlayer = gameBoard.getPlayers().get(currentPlayerIndex);
        if (nextPlayer instanceof ComputerPlayer computerPlayer) {
            rollButton.setEnabled(false); // Disable roll button
            parentGUI.getEndTurnButton().setEnabled(false); // Disable end turn button
            computerPanel.handleComputerTurn(computerPlayer, this::endTurn);
        } else {
            rollButton.setEnabled(true); // Enable roll button for human player
            parentGUI.getEndTurnButton().setEnabled(true); // Enable end turn button
        }
    }

    private void updateDiceImages(int dice1Value, int dice2Value) {
        try {
            ImageIcon icon1 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/dice" + dice1Value + ".png")));
            ImageIcon icon2 = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/dice" + dice2Value + ".png")));

            dice1Label.setIcon(icon1);
            dice2Label.setIcon(icon2);
        } catch (Exception e) {
            dice1Label.setText(Integer.toString(dice1Value));
            dice2Label.setText(Integer.toString(dice2Value));
        }
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

    private void handleLandingSpace(Player player, int position) {
        Space space = gameBoard.getSpaces().get(position);

        if (space instanceof PropertySpace propertySpace) {
            if (!propertySpace.isOwned()) {
                int response = JOptionPane.showConfirmDialog(
                        this,
                        player.getName() + ", would you like to purchase " +
                                propertySpace.getName() + " for $" + propertySpace.getPrice() + "?",
                        "Purchase Property",
                        JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    if (player.getMoney() >= propertySpace.getPrice()) {
                        propertySpace.buy(player);
                        bank.collectFromPlayer(player, propertySpace.getPrice());

                        JOptionPane.showMessageDialog(this,
                                player.getName() + " successfully purchased " + propertySpace.getName() + " for $" + propertySpace.getPrice() + "!",
                                "Purchase Successful",
                                JOptionPane.INFORMATION_MESSAGE);

                        if (parentGUI != null) {
                            parentGUI.getBankPanel().refreshProperties();
                            parentGUI.getPlayerPanels().get(player.getPlayerIndex()).refreshProperties();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "You don't have enough money to buy " + propertySpace.getName() + "!",
                                "Not Enough Funds",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

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
}
