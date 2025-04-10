/*
 * UtilitySpace.java
 *
 * This class represents a utility space on the board in a Monopoly-like game.
 * It extends the Space class and includes properties specific to utility spaces.
 *
 * Remodeled by Finn Dempsey
 */
package Model;

public class UtilitySpace extends Space {
    private final int price;
    private final Player owner;

    public UtilitySpace(String name, int location, int price, int mortgageValue) {
        super(name);
        this.price = price;
        this.owner = null; // Initially unowned
    }

    @Override
    public void landOn(Player player) {
        // Logic for landing on Utility space
        System.out.println(player.getName() + " landed on " + name + " Utility.");
    }

    public int getPrice() {
        return price;
    }

    public boolean isOwned() {
        return owner != null;
    }

    public Player getOwner() {
        return owner;
    }
}
