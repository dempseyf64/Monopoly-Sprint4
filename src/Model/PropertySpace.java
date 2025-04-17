/**
 * The PropertySpace class represents a property space on the Monopoly game board.
 * It extends the Property class and includes additional fields for rent calculations
 * based on the number of houses or hotels on the property.
 *
 * Created by Kristian Wright
 */
package Model;

public class PropertySpace extends Property {
    private final int propertySite; // Base rent for the property
    private final int propertySiteWithColorSet; // Rent with a color set
    private final int costWithOneHouse; // Rent with one house
    private final int costWithTwoHouses; // Rent with two houses
    private final int costWithThreeHouses; // Rent with three houses
    private final int costWithFourHouses; // Rent with four houses
    private final int costWithHotel; // Rent with a hotel

    /**
     * Constructs a PropertySpace with the specified attributes.
     *
     * @param name                     The name of the property.
     * @param location                 The location of the property on the board.
     * @param color                    The color group of the property.
     * @param price                    The purchase price of the property.
     * @param propertySite             The base rent for the property.
     * @param propertySiteWithColorSet The rent with a color set.
     * @param costWithOneHouse         The rent with one house.
     * @param costWithTwoHouses        The rent with two houses.
     * @param costWithThreeHouses      The rent with three houses.
     * @param costWithFourHouses       The rent with four houses.
     * @param costWithHotel            The rent with a hotel.
     * @param mortgageValue            The mortgage value of the property.
     * @param costOfHouseHotel         The cost of building a house or hotel.
     * @param bank                     The bank managing the property.
     */
    public PropertySpace(String name, int location, String color, int price, int propertySite, int propertySiteWithColorSet,
                         int costWithOneHouse, int costWithTwoHouses, int costWithThreeHouses, int costWithFourHouses,
                         int costWithHotel, int mortgageValue, int costOfHouseHotel, Bank bank) {
        super(name, price, color, bank);
        this.propertySite = propertySite;
        this.propertySiteWithColorSet = propertySiteWithColorSet;
        this.costWithOneHouse = costWithOneHouse;
        this.costWithTwoHouses = costWithTwoHouses;
        this.costWithThreeHouses = costWithThreeHouses;
        this.costWithFourHouses = costWithFourHouses;
        this.costWithHotel = costWithHotel;
    }

    /**
     * Handles the action when a player lands on this property.
     *
     * @param player The player landing on the property.
     */
    @Override
    public void landOn(Player player) {
        if (getOwner() == null) {
            System.out.println(player.getName() + " landed on " + getName() + " which is unowned.");
        } else if (getOwner() != player) {
            int rent = calculateRent();
            getBank().collectFromPlayer(player, rent);
            getBank().payPlayer(getOwner(), rent);
            System.out.println(player.getName() + " landed on " + getName() + " and paid $" + rent + " rent to " + getOwner().getName());
        } else {
            System.out.println(player.getName() + " landed on their own property " + getName() + ".");
        }
    }

    /**
     * Calculates the rent for this property based on its state.
     *
     * @return The calculated rent.
     */
    @Override
    public int calculateRent() {
        return propertySite + propertySiteWithColorSet + costWithOneHouse + costWithTwoHouses + costWithThreeHouses + costWithFourHouses + costWithHotel;
    }

    // Getter methods for the additional fields

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
}