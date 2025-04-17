package Model;

/**
 * Represents a property in the Monopoly game.
 * Each property has a name, price, base rent, color group, and ownership status.
 * This class extends the Space class and implements the logic for buying properties,
 * paying rent, mortgaging, and unmortgaging.
 *
 * Created by Kristian Wright modified by Collin Cabral-Castro
 */
public class Property extends Space {
    private final String name;
    private final int price;
    private final int baseRent;
    private final String colorGroup;
    private Player owner;
    private boolean mortgaged;
    private int houseCount;
    private boolean hasHotel;
    private final Bank bank;

    /**
     * Constructs a Property with the given name, price, and color group.
     *
     * @param name The name of the property.
     * @param price The price of the property.
     * @param colorGroup The color group of the property.
     * @param bank The bank managing the property transactions.
     */
    public Property(String name, int price, String colorGroup, Bank bank) {
        super(name); // Assuming Space has a constructor that takes a name
        this.name = name;
        this.price = price;
        this.baseRent = calculateBaseRent(price);
        this.colorGroup = colorGroup;
        this.bank = bank;
        this.owner = null; // Initially unowned
        this.mortgaged = false;
        this.houseCount = 0;
        this.hasHotel = false;
    }

    /**
     * Calculates the base rent for the property based on its price.
     *
     * @param price The price of the property.
     * @return The base rent of the property.
     */
    private int calculateBaseRent(int price) {
        return price / 10; // Example: Rent is 10% of the property price
    }

    /**
     * Buys the property for the specified player if it is unowned.
     *
     * @param player The player buying the property.
     */
    public void buy(Player player) {
        if (this.owner == null) {
            this.owner = player;
            bank.collectFromPlayer(player, this.price);
            player.addProperty(this);
            System.out.println(player.getName() + " bought " + this.name);
        } else {
            System.out.println(this.name + " is already owned.");
        }
    }

    /**
     * Pays rent to the owner of the property if it is owned by another player and not mortgaged.
     *
     * @param player The player paying the rent.
     */
    public void payRent(Player player) {
        if (this.owner != null && this.owner != player && !this.mortgaged) {
            int rentAmount = calculateRent();
            bank.collectFromPlayer(player, rentAmount);
            bank.payPlayer(this.owner, rentAmount);
            System.out.println(player.getName() + " paid $" + rentAmount + " rent to " + this.owner.getName());
        }
    }

    /**
     * Calculates the rent for the property based on the number of houses or hotels.
     *
     * @return The rent amount.
     */
    public int calculateRent() {
        if (hasHotel) return baseRent * 50;
        if (houseCount > 0) return baseRent * (houseCount * 5);
        return baseRent;
    }

    /**
     * Mortgages the property, giving the owner half its price.
     */
    public void mortgage() {
        if (!mortgaged) {
            mortgaged = true;
            bank.payPlayer(owner, price / 2);
            System.out.println(owner.getName() + " mortgaged " + this.name);
        }
    }

    /**
     * Unmortgages the property, charging the owner 10% interest.
     */
    public void unmortgage() {
        if (mortgaged) {
            mortgaged = false;
            bank.collectFromPlayer(owner, (int) (price * 0.55)); // 10% interest
            System.out.println(owner.getName() + " unmortgaged " + this.name);
        }
    }

    /**
     * Checks if the property is owned.
     *
     * @return True if the property is owned, false otherwise.
     */
    public boolean isOwned() {
        return owner != null;
    }

    /**
     * Gets the owner of the property.
     *
     * @return The owner of the property.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the owner of the property.
     *
     * @param owner The new owner of the property.
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Gets the name of the property.
     *
     * @return The name of the property.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the price of the property.
     *
     * @return The price of the property.
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Checks if the property is mortgaged.
     *
     * @return True if the property is mortgaged, false otherwise.
     */
    public boolean isMortgaged() {
        return this.mortgaged;
    }

    /**
     * Gets the color group of the property.
     *
     * @return The color group of the property.
     */
    public String getColorGroup() {
        return this.colorGroup;
    }

    /**
     * Gets the number of houses on the property.
     *
     * @return The number of houses on the property.
     */
    public int getHouseCount() {
        return houseCount;
    }

    /**
     * Sets the number of houses on the property.
     *
     * @param houseCount The new number of houses.
     */
    public void setHouseCount(int houseCount) {
        this.houseCount = houseCount;
    }

    /**
     * Checks if the property has a hotel.
     *
     * @return True if the property has a hotel, false otherwise.
     */
    public boolean hasHotel() {
        return hasHotel;
    }

    /**
     * Sets whether the property has a hotel.
     *
     * @param hasHotel True if the property has a hotel, false otherwise.
     */
    public void setHasHotel(boolean hasHotel) {
        this.hasHotel = hasHotel;
    }

    /**
     * Gets the bank managing the property.
     *
     * @return The bank.
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * Handles the event when a player lands on the property.
     *
     * @param player The player landing on the property.
     */
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
}