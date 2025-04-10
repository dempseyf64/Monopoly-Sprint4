/*
 * RailroadSpace.java
 *
 * This class represents a railroad space on the board in a Monopoly-like game.
 * It extends the Space class and includes properties and methods specific to railroad spaces.
 *
 * Remodeled by Finn Dempsey
 */
package Model;

public class RailroadSpace extends Space {
    private final int price;
    private Player owner;

    public RailroadSpace(String name, int location, int price, int costWithOne, int costWithTwo,
                         int costWithThree, int costWithFour, int mortgageValue) {
        super(name);
        this.price = price;
        this.owner = null; // Initially unowned
    }

    @Override
    public void landOn(Player player) {
        // Logic for landing on Railroad space
        System.out.println(player.getName() + " landed on " + name + " Railroad.");
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
