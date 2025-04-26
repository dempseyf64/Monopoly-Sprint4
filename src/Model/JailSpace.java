/*
 * JailSpace.java
 *
 * This class represents the Jail space on a Monopoly board.
 * It extends the Space class and implements the logic for landing on Jail.
 *
 * Created by Kristian Wright
 */
package Model;

import java.util.ArrayList;

/**
 * Represents the Jail space on a Monopoly board.
 * Handles both "Just Visiting" functionality and actual imprisonment.
 * Players can be sent to jail and must either roll doubles, pay a fine,
 * or use a Get Out of Jail Free card to leave.
 */
public class JailSpace extends Space {

    /**
     * Amount a player must pay to get out of jail
     */
    private final int JAIL_FINE = 50;

    /**
     * Map of players currently in jail and their turn count
     */
    private final java.util.Map<Player, Integer> playersInJail;

    /**
     * Constructs a new Jail space
     */
    public JailSpace() {
        super("Jail");
        this.playersInJail = new java.util.HashMap<>();
    }

    /**
     * Handles the event of a player landing on the Jail space.
     * This represents a "Just Visiting" scenario.
     *
     * @param player The player landing on this space
     */
    @Override
    public void landOn(Player player) {
        // When a player lands on Jail, they are "Just Visiting"
        System.out.println(player.getName() + " landed on Jail (Just Visiting).");
    }

    /**
     * Sends a player to jail
     *
     * @param player The player to be imprisoned
     */
    public void sendToJail(Player player) {
        player.setPosition(10); // Jail is at position 10
        playersInJail.put(player, 0); // Initialize jail turn count
        System.out.println(player.getName() + " has been sent to Jail!");
    }

    /**
     * Checks if a player is currently in jail
     *
     * @param player The player to check
     * @return True if the player is in jail, false otherwise
     */
    public boolean isInJail(Player player) {
        return playersInJail.containsKey(player);
    }

    /**
     * Releases a player from jail
     *
     * @param player The player to release
     */
    public void releaseFromJail(Player player) {
        if (playersInJail.containsKey(player)) {
            playersInJail.remove(player);
            System.out.println(player.getName() + " has been released from Jail!");
        }
    }

    /**
     * Attempts to get a player out of jail by rolling dice.
     * If player rolls doubles, they get out of jail but don't get an extra turn.
     * If player fails to roll doubles three times, they must pay the fine.
     *
     * @param player The imprisoned player
     * @param diceRoll The dice roll result (array with two values)
     * @return true if player was released from jail, false if they remain in jail
     */
    public boolean attemptJailRelease(Player player, ArrayList<Integer> diceRoll) {
        if (!isInJail(player)) {
            return false;
        }

        int dice1Value = diceRoll.get(0);
        int dice2Value = diceRoll.get(1);
        boolean isDoubles = (dice1Value == dice2Value);

        // Get the player's current jail turn count
        int jailTurns = playersInJail.get(player);

        if (isDoubles) {
            // Player rolled doubles, they get out of jail
            releaseFromJail(player);
            System.out.println(player.getName() + " rolled doubles and is now free from Jail!");

            // Calculate new position (they move the number of spaces shown on the dice)
            int newPosition = (player.getPosition() + dice1Value + dice2Value) % 40;
            player.setPosition(newPosition);

            return true;
        } else {
            // Player failed to roll doubles
            jailTurns++;
            playersInJail.put(player, jailTurns);

            int MAX_JAIL_TURNS = 3;
            if (jailTurns >= MAX_JAIL_TURNS) {
                // After 3 turns, player must pay fine and leave jail
                // Use player.setMoney instead of decreaseMoney
                player.setMoney(player.getMoney() - JAIL_FINE);
                releaseFromJail(player);

                // Calculate new position after paying
                int newPosition = (player.getPosition() + dice1Value + dice2Value) % 40;
                player.setPosition(newPosition);

                System.out.println(player.getName() + " paid $" + JAIL_FINE +
                        " after failing to roll doubles " + MAX_JAIL_TURNS +
                        " times and is now free from Jail!");
                return true;
            } else {
                System.out.println(player.getName() + " failed to roll doubles. " +
                        "Turns in jail: " + jailTurns + "/" + MAX_JAIL_TURNS);
                return false;
            }
        }
    }

    /**
     * Allows a player to pay the fine to get out of jail
     *
     * @param player The player paying the fine
     * @return true if payment was successful, false otherwise
     */
    public boolean payFineToLeaveJail(Player player) {
        if (!isInJail(player)) {
            return false;
        }

        if (player.getMoney() >= JAIL_FINE) {
            // Use player.setMoney instead of decreaseMoney
            player.setMoney(player.getMoney() - JAIL_FINE);
            releaseFromJail(player);
            System.out.println(player.getName() + " paid $" + JAIL_FINE + " to get out of Jail!");
            return true;
        } else {
            System.out.println(player.getName() + " doesn't have enough money to pay the jail fine!");
            return false;
        }
    }

    /**
     * Gets the number of turns a player has spent in jail
     *
     * @param player The player to check
     * @return Number of turns in jail, or 0 if not in jail
     */
    public int getJailTurns(Player player) {
        return playersInJail.getOrDefault(player, 0);
    }
}
