/*
 * Player class represents a player in the Monopoly game.
 * It contains attributes like name, token, money, position on the board,
 * properties owned, jail status, and methods to move, go to jail, and manage properties.
 *
 * Created by Kristian Wright modified by Collin Cabral-Castro
 */

package Model;

import java.util.*;

/**
 * Player class represents a player in the Monopoly game.
 * It contains attributes like name, token, money, position on the board,
 * properties owned, jail status, and methods to move, go to jail, and manage properties.
 */
public class Player {
    private int playerIndex;
    final private String name;
    private String token;
    private int money;
    private int position;
    final private List<Property> properties;
    private boolean inJail;
    private boolean hasGetOutOfJailFreeCard;
    private final Dice dice;
    private GameBoard gameBoard;

    /**
     * Constructs a Player with the specified name, token, and game board.
     *
     * @param name the name of the player
     * @param token the token chosen by the player
     * @param gameBoard the game board the player is playing on
     */
    public Player(String name, String token, GameBoard gameBoard) {
        this.name = name;
        this.token = token;
        this.money = 1500;
        this.position = 0;
        this.properties = new ArrayList<>();
        this.inJail = false;
        int jailTurns = 0;
        this.hasGetOutOfJailFreeCard = false;
        this.gameBoard = gameBoard;
        this.dice = Dice.getInstance();
    }

    /**
     * Moves the player by the specified number of steps.
     *
     * @param steps the number of steps to move
     */
    public void move(int steps) {
        position = (position + steps) % 40;
        Space newSpace = gameBoard.getSpace(position);
        System.out.println(token + " moved to " + newSpace.getName());
    }

    /**
     * Sends the player to jail.
     */
    public void goToJail() {
        this.inJail = true;
        this.position = 10;
        System.out.println(token + " is sent to Jail.");
    }

    /**
     * Returns the name of the player.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the token of the player.
     *
     * @return the token of the player
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token of the player.
     *
     * @param token the new token of the player
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Returns the position of the player on the board.
     *
     * @return the position of the player
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position of the player on the board.
     *
     * @param position the new position of the player
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Returns the amount of money the player has.
     *
     * @return the amount of money the player has
     */
    public int getMoney() {
        return money;
    }

    /**
     * Sets the amount of money the player has.
     *
     * @param money the new amount of money the player has
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Returns whether the player is in jail.
     *
     * @return true if the player is in jail, false otherwise
     */
    public boolean isInJail() {
        return inJail;
    }

    /**
     * Returns whether the player has a Get Out of Jail Free card.
     *
     * @return true if the player has a Get Out of Jail Free card, false otherwise
     */
    public boolean hasGetOutOfJailFreeCard() {
        return hasGetOutOfJailFreeCard;
    }

    /**
     * Gives the player a Get Out of Jail Free card.
     */
    public void receiveGetOutOfJailFreeCard() {
        this.hasGetOutOfJailFreeCard = true;
    }

    /**
     * Returns the game board the player is playing on.
     *
     * @return the game board the player is playing on
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Sets the game board the player is playing on.
     *
     * @param gameBoard the new game board the player is playing on
     */
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    /**
     * Adds a property to the player's list of owned properties.
     *
     * @param property the property to add
     */
    public void addProperty(Property property) {properties.add(property);}

    /**
     * Returns the list of properties owned by the player.
     *
     * @return the list of properties owned by the player
     */
    public List<Property> getProperties() {
        return properties;
    }

    /**
     * Returns the dice used by the player.
     *
     * @return the dice used by the player
     */
    public Dice getDice() {
        return dice;
    }

    public void setPlayerIndex(int index) {
        this.playerIndex = index;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
    public void adjustMoney(int amount) {
        this.money += amount;
    }

    /**
     * Checks if the player owns all properties of a specific color group.
     */
    public boolean ownsFullColorGroup(String colorGroup) {
        int totalInGroup = 0;
        int ownedInGroup = 0;

        for (Space space : gameBoard.getSpaces()) {
            if (space instanceof PropertySpace propSpace) {
                if (propSpace.getColorGroup().equalsIgnoreCase(colorGroup)) {
                    totalInGroup++;
                    if (propSpace.getOwner() == this) {
                        ownedInGroup++;
                    }
                }
            }
        }

        return totalInGroup > 0 && totalInGroup == ownedInGroup;
    }

}
