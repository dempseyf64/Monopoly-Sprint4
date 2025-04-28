/**
 * The Bank is responsible for:
 *  - Holding and selling unowned properties
 *  - Managing houses and hotels
 *  - Receiving taxes, fees, etc.
 *  - Paying players for passing GO or other earnings
 *  - Auctioning properties
 *  - Handling mortgages
 *
 * Created by Collin Cabral-Castro, Refactored and Remodeled by Kristian Wright
 */
package Model;

import java.util.List;

public class Bank {
    private List<Property> properties;

    /**
     * Constructs a Bank with an initial list of properties.
     *
     * @param properties The list of properties managed by the Bank.
     */
    public Bank(List<Property> properties) {
        this.properties = properties;
    }

    /**
     * Sets the properties managed by the Bank.
     * (Useful if properties are initialized later after the Bank is created.)
     *
     * @param properties List of Property objects.
     */
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    /**
     * Collects a payment from a player.
     *
     * @param player The player paying the money.
     * @param amount The amount to collect.
     */
    public void collectFromPlayer(Player player, int amount) {
        player.setMoney(player.getMoney() - amount);
        System.out.println(player.getName() + " paid $" + amount + " to the Bank.");
    }

    /**
     * Pays money to a player.
     *
     * @param player The player receiving the money.
     * @param amount The amount to pay.
     */
    public void payPlayer(Player player, int amount) {
        player.setMoney(player.getMoney() + amount);
        System.out.println("The Bank paid $" + amount + " to " + player.getName() + ".");
    }

    /**
     * Gets the list of properties still owned by the Bank (unowned properties).
     *
     * @return List of unowned Property objects.
     */
    public List<Property> getUnownedProperties() {
        return properties.stream()
                .filter(property -> !property.isOwned())
                .toList();
    }

    /**
     * Gets the full list of all properties the Bank knows about.
     *
     * @return List of all Property objects.
     */
    public List<Property> getAllProperties() {
        return properties;
    }

    /**
     * Allows a player to buy a house for a specific property.
     *
     * @param player The player buying the house.
     * @param property The property to build the house on.
     * @return true if the purchase was successful, false otherwise.
     */
    public boolean buyHouse(Player player, PropertySpace property) {
        if (property.getHouseCount() >= 4 || property.hasHotel()) {
            System.out.println("Cannot build more houses on " + property.getName() + ".");
            return false;
        }

        int houseCost = property.getCostWithOneHouse(); // Using 1-house cost as house price

        if (player.getMoney() >= houseCost) {
            player.setMoney(player.getMoney() - houseCost);
            property.setHouseCount(property.getHouseCount() + 1);
            System.out.println(player.getName() + " built a house on " + property.getName() + "!");
            return true;
        } else {
            System.out.println(player.getName() + " doesn't have enough money to build a house.");
            return false;
        }
    }

}