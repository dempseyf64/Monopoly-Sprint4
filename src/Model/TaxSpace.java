/*
 * TaxSpace.java
 *
 * This class represents a tax space on the board in a Monopoly-like game.
 * When a player lands on this space, they will pay a specified tax amount.
 *
 * Remodeled by Finn Dempsey Modified by Collin Cabral-Castro
 */
package Model;

public class TaxSpace extends Space {
    private final int taxAmount;
    private final Bank bank;

    public TaxSpace(String name, int location, int taxAmount, Bank bank) {
        super(name);
        this.taxAmount = taxAmount;
        this.bank = bank;
    }

    @Override
    public void landOn(Player player) {
        bank.collectFromPlayer(player, taxAmount);
        System.out.println(player.getName() + " landed on " + name + " and paid $" + taxAmount + ".");
    }

    public int getTaxAmount() {
        return taxAmount;
    }
}
