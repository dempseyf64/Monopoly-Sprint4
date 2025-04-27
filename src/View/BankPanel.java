package View;

import Model.GameBoard;
import Model.PropertySpace;
import Model.Space;

import javax.swing.*;
import java.awt.*;

/**
 * BankPanel displays all properties currently owned by the bank.
 * Updated by Collin Cabral-Castro
 */
public class BankPanel extends JPanel {
    private final GameBoard gameBoard;

    public BankPanel(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        setLayout(new GridLayout(0, 2, 10, 10));

        refreshProperties();
    }

    // This method refreshes the list of properties shown
    public void refreshProperties() {
        removeAll();

        for (Space space : gameBoard.getSpaces()) {
            if (space instanceof PropertySpace property && !property.isOwned()) {
                TitleDeedCardPanel card = new TitleDeedCardPanel(property);
                add(card);
            }
        }

        revalidate();
        repaint();
    }
}
