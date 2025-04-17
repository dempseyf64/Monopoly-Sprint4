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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bank {

    private static final int INITIAL_HOUSES = 32;
    private static final int INITIAL_HOTELS = 12;

    private int housesRemaining;
    private int hotelsRemaining;
    private final List<Property> unownedProperties;

    /**
     * Creates a Bank that starts off with the standard supply
     * of houses/hotels, and any unowned properties.
     *
     * @param allProperties The full list of properties in the game (these are initially unowned).
     */
    public Bank(List<Property> allProperties) {
        this.housesRemaining = INITIAL_HOUSES;
        this.hotelsRemaining = INITIAL_HOTELS;
        this.unownedProperties = new ArrayList<>();

        if (allProperties != null) {
            for (Property p : allProperties) {
                if (!p.isOwned()) {
                    unownedProperties.add(p);
                }
            }
        }
    }

    public int getHousesRemaining() {
        return housesRemaining;
    }

    public int getHotelsRemaining() {
        return hotelsRemaining;
    }

    /**
     * Gives a specified amount of money to a Player.
     *
     * @param player The player to receive the money.
     * @param amount The amount of money to give.
     */
    public void payPlayer(Player player, int amount) {
        if (player != null && amount > 0) {
            player.setMoney(player.getMoney() + amount);
            System.out.println("Bank pays " + player.getName() + " $" + amount);
        }
    }

    /**
     * Collects a specified amount of money from a Player.
     *
     * @param player The player to collect money from.
     * @param amount The amount of money to collect.
     */
    public void collectFromPlayer(Player player, int amount) {
        if (player != null && amount > 0) {
            player.setMoney(player.getMoney() - amount);
            System.out.println("Bank collects $" + amount + " from " + player.getName());
        }
    }

    /**
     * Sells a property to a player at face value (assuming it is unowned).
     *
     * @param property The property to sell.
     * @param buyer The player buying the property.
     */
    public void sellProperty(Property property, Player buyer) {
        if (property != null && buyer != null && unownedProperties.contains(property)) {
            if (buyer.getMoney() >= property.getPrice()) {
                property.buy(buyer);
                unownedProperties.remove(property);
                System.out.println(buyer.getName() + " bought " + property.getName() + " for $" + property.getPrice());
            } else {
                System.out.println(buyer.getName() + " cannot afford " + property.getName());
            }
        } else {
            System.out.println("Property is either already owned or invalid.");
        }
    }

    /**
     * Auctions a property among all players.
     *
     * @param property The property to auction.
     * @param players The list of players participating in the auction.
     */
    public void auctionProperty(Property property, List<Player> players) {
        if (property == null || players == null || players.isEmpty()) {
            System.out.println("Invalid property or players for auction.");
            return;
        }

        unownedProperties.remove(property);
        System.out.println("Starting auction for " + property.getName() + " (Price: $" + property.getPrice() + ")");
        Scanner scanner = new Scanner(System.in);
        int highestBid = 0;
        Player highestBidder = null;

        for (Player player : players) {
            if (player.getMoney() > 0) {
                System.out.println(player.getName() + ", enter your bid (0 to skip): ");
                try {
                    int bid = Integer.parseInt(scanner.nextLine());
                    if (bid > highestBid && bid <= player.getMoney()) {
                        highestBid = bid;
                        highestBidder = player;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid bid. Skipping...");
                }
            }
        }

        if (highestBidder != null) {
            property.buy(highestBidder);
            System.out.println(highestBidder.getName() + " wins the auction for $" + highestBid + ".");
        } else {
            System.out.println("No bids were placed. The property remains with the Bank.");
            unownedProperties.add(property);
        }
    }

    /**
     * Mortgages a property (pays the player half its price).
     *
     * @param property The property to mortgage.
     * @param owner The owner of the property.
     */
    public void mortgageProperty(Property property, Player owner) {
        if (property != null && owner != null && property.getOwner() == owner && !property.isMortgaged()) {
            property.mortgage();
            System.out.println(owner.getName() + " mortgaged " + property.getName());
        } else {
            System.out.println("Cannot mortgage property.");
        }
    }

    /**
     * Lifts a mortgage from a property if the owner has enough money.
     *
     * @param property The property to unmortgage.
     * @param owner The owner of the property.
     */
    public void unmortgageProperty(Property property, Player owner) {
        if (property != null && owner != null && property.getOwner() == owner && property.isMortgaged()) {
            property.unmortgage();
            System.out.println(owner.getName() + " unmortgaged " + property.getName());
        } else {
            System.out.println("Cannot unmortgage property.");
        }
    }

    public void printBankStatus() {
        System.out.println("\n--- Bank Status ---");
        System.out.println("Houses remaining: " + housesRemaining);
        System.out.println("Hotels remaining: " + hotelsRemaining);
        System.out.println("Unowned properties:");
        for (Property p : unownedProperties) {
            System.out.println("   - " + p.getName() + " ($" + p.getPrice() + ")");
        }
        System.out.println("-------------------\n");
    }
}