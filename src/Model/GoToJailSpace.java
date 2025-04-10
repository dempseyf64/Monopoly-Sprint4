/*
 * GoToJailSpace.java
 *
 * Represents a space on the board that sends a player to jail when landed on.
 *
 * Remodeled by Finn Dempsey
 */
package Model;

public class GoToJailSpace extends Space {
    public GoToJailSpace() {
        super("Go To Jail");
    }

    @Override
    public void landOn(Player player) {
        player.goToJail(); // Send player to Jail
        System.out.println(player.getName() + " landed on Go To Jail and is sent to Jail.");
    }
}
