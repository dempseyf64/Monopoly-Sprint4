/**
 * The ComputerPlayer class represents an AI-controlled player in the Monopoly game.
 * It extends the Player class and includes logic for automated decision-making.
 *
 * Created by Kristian Wright
 */
package Model;

public class ComputerPlayer extends Player {

    /**
     * Constructs a ComputerPlayer with the specified name, token, and game board.
     *
     * @param name      The name of the computer player.
     * @param token     The token representing the computer player on the board.
     * @param gameBoard The game board the player is part of.
     */
    public ComputerPlayer(String name, String token, GameBoard gameBoard) {
        super(name, token, gameBoard);
    }

    /**
     * Moves the computer player by the specified number of steps.
     *
     * @param steps The number of steps to move.
     */
    @Override
    public void move(int steps) {
        System.out.println(getName() + " (Computer) is moving...");
        super.move(steps);
    }

    /**
     * Makes a decision for the computer player, such as buying a property if affordable.
     */
    public void makeDecision() {
        // Example decision logic: Buy property if affordable
        Space currentSpace = getGameBoard().getSpace(getPosition());
        if (currentSpace instanceof PropertySpace property) {
            if (!property.isOwned() && getMoney() >= property.getPrice()) {
                System.out.println(getName() + " (Computer) decided to buy " + property.getName());
                setMoney(getMoney() - property.getPrice());
                property.setOwner(this);
                addProperty(property);
            } else {
                System.out.println(getName() + " (Computer) decided not to buy " + property.getName());
            }
        }
    }
}