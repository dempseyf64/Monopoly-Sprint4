/*
 * PropertySpace.java
 *
 * This class represents a property space on the board in a Monopoly-like game.
 * It includes attributes for pricing, rent calculation, and ownership.
 *
 * Created by Kristian Wright Modified by Collin Cabral-Castro
 */
package Model;

public class PropertySpace extends Space {
    private final int price;
    private final int propertySite;
    private final int propertySiteWithColorSet;
    private final int costWithOneHouse;
    private final int costWithTwoHouses;
    private final int costWithThreeHouses;
    private final int costWithFourHouses;
    private final int costWithHotel;
    private final int mortgageValue;
    private final int costOfHouseHotel;
    private Player owner;
    private final Bank bank;

    public PropertySpace(String name, int location, String color, int price, int propertySite, int propertySiteWithColorSet,
                         int costWithOneHouse, int costWithTwoHouses, int costWithThreeHouses, int costWithFourHouses,
                         int costWithHotel, int mortgageValue, int costOfHouseHotel, Bank bank) {
        super(name);
        this.price = price;
        this.propertySite = propertySite;
        this.propertySiteWithColorSet = propertySiteWithColorSet;
        this.costWithOneHouse = costWithOneHouse;
        this.costWithTwoHouses = costWithTwoHouses;
        this.costWithThreeHouses = costWithThreeHouses;
        this.costWithFourHouses = costWithFourHouses;
        this.costWithHotel = costWithHotel;
        this.mortgageValue = mortgageValue;
        this.costOfHouseHotel = costOfHouseHotel;
        this.bank = bank;
        this.owner = null; // Initially unowned
    }

    @Override
    public void landOn(Player player) {
        if (owner == null) {
            System.out.println(player.getName() + " landed on " + name + " which is unowned.");
        } else if (owner != player) {
            int rent = calculateRent();
            bank.collectFromPlayer(player, rent);
            bank.payPlayer(owner, rent);
            System.out.println(player.getName() + " landed on " + name + " and paid $" + rent + " rent to " + owner.getName());
        } else {
            System.out.println(player.getName() + " landed on their own property " + name + ".");
        }
    }

    public int calculateRent() {
        // Example rent calculation logic using the fields
        return propertySite + propertySiteWithColorSet + costWithOneHouse + costWithTwoHouses + costWithThreeHouses + costWithFourHouses + costWithHotel;
    }

    public void buy(Player player) {
        if (owner == null) {
            owner = player;
            bank.collectFromPlayer(player, price);
            System.out.println(player.getName() + " bought " + name + " for $" + price);
        }
    }

    public boolean isOwned() {
        return owner != null;
    }

    public Player getOwner() {
        return owner;
    }

    public int getPrice() {
        return price;
    }

    // Getter methods for the fields
    public int getPropertySite() {
        return propertySite;
    }

    public int getPropertySiteWithColorSet() {
        return propertySiteWithColorSet;
    }

    public int getCostWithOneHouse() {
        return costWithOneHouse;
    }

    public int getCostWithTwoHouses() {
        return costWithTwoHouses;
    }

    public int getCostWithThreeHouses() {
        return costWithThreeHouses;
    }

    public int getCostWithFourHouses() {
        return costWithFourHouses;
    }

    public int getCostWithHotel() {
        return costWithHotel;
    }

    public int getMortgageValue() {
        return mortgageValue;
    }

    public int getCostOfHouseHotel() {
        return costOfHouseHotel;
    }
}