/*
 * FreeParkingSpace.java
 *
 * This class represents a Free Parking space in a board game.
 *
 * Remodeled by Finn Dempsey
 */
package Model;

public class FreeParkingSpace extends Space {
    public FreeParkingSpace() {
        super("Free Parking");
    }

    @Override
    public void landOn(Player player) {
        // Logic for landing on Free Parking space
        System.out.println(player.getName() + " landed on Free Parking.");
    }
}
