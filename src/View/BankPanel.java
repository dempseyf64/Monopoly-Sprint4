/**
 * BankPanel.java

 * This class represents the bank panel in the Monopoly game GUI.
 * It displays the bank's properties and allows players to view details about them.

 * Authored by Collin Cabral-Castro
 */

package View;

import javax.swing.*;

public class BankPanel extends JPanel {
    public BankPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Example card preview button
        JButton mediterraneanBtn = new JButton("Mediterranean Avenue");
        mediterraneanBtn.addActionListener(e -> showCardDetails("Mediterranean Avenue"));
        add(mediterraneanBtn);
    }

    private void showCardDetails(String propertyName) {
        JOptionPane.showMessageDialog(this, propertyName + " Info here.");
    }
}
