/*
 * GameBoard.java
 *
 * This class represents the game board for a Monopoly-like game.
 * It initializes the spaces, decks, and players, and handles player movements.
 *
 * Remodeled by Finn Dempsey, Collin Castro, and Kristian Wright
 */

package Model;

import java.util.*;

public class GameBoard {
    private final List<Space> spaces;
    private final Stack<ChanceCard> chanceDeck;
    private final Stack<CommunityChestCard> communityDeck;
    private final List<Player> players;
    private final boolean isTestMode;
    private final Bank bank;

    public GameBoard(List<Player> players, boolean isTestMode, Bank bank) {
        this.players = players;
        this.isTestMode = isTestMode;
        this.bank = bank;
        this.spaces = new ArrayList<>();
        this.chanceDeck = ChanceCard.initializeChanceCards(bank);
        this.communityDeck = CommunityChestCard.initializeCommunityChestCards(this, bank);
        initializeBoard();
    }

    private void initializeBoard() {
        spaces.add(new GoSpace());
        spaces.add(new PropertySpace("Mediterranean Avenue", 1, "Brown", 60, 2, 4, 10, 30, 90, 160, 250, 30, 50, bank));
        spaces.add(new CommunityChestSpace());
        spaces.add(new PropertySpace("Baltic Avenue", 3, "Brown", 60, 4, 8, 20, 60, 180, 320, 450, 30, 50, bank));
        spaces.add(new TaxSpace("Income Tax", 4, 200, bank));
        spaces.add(new RailroadSpace("Reading Railroad", 5, 200, 25, 50, 100, 200, 100));
        spaces.add(new PropertySpace("Oriental Avenue", 6, "Light Blue", 100, 6, 12, 30, 90, 270, 400, 550, 50, 50, bank));
        spaces.add(new ChanceSpace());
        spaces.add(new PropertySpace("Vermont Avenue", 8, "Light Blue", 100, 6, 12, 30, 90, 270, 400, 550, 50, 50, bank));
        spaces.add(new PropertySpace("Connecticut Avenue", 9, "Light Blue", 120, 8, 16, 40, 100, 300, 450, 600, 60, 50, bank));
        spaces.add(new JailSpace());
        spaces.add(new PropertySpace("St Charles Place", 11, "Pink", 140, 10, 20, 50, 150, 450, 625, 750, 70, 100, bank));
        spaces.add(new UtilitySpace("Electric Company", 12, 150, 75));
        spaces.add(new PropertySpace("States Avenue", 13, "Pink", 140, 10, 20, 50, 150, 450, 625, 750, 70, 100, bank));
        spaces.add(new PropertySpace("Virginia Avenue", 14, "Pink", 160, 12, 24, 60, 180, 500, 700, 900, 80, 100, bank));
        spaces.add(new RailroadSpace("Pennsylvania Railroad", 15, 200, 25, 50, 100, 200, 100));
        spaces.add(new PropertySpace("St James Place", 16, "Orange", 180, 14, 28, 70, 200, 550, 750, 950, 90, 100, bank));
        spaces.add(new CommunityChestSpace());
        spaces.add(new PropertySpace("Tennessee Avenue", 18, "Orange", 180, 14, 28, 70, 200, 550, 750, 950, 90, 100, bank));
        spaces.add(new PropertySpace("New York Avenue", 19, "Orange", 200, 16, 32, 80, 220, 600, 800, 1000, 100, 100, bank));
        spaces.add(new FreeParkingSpace());
        spaces.add(new PropertySpace("Kentucky Avenue", 21, "Red", 220, 18, 36, 90, 250, 700, 875, 1050, 110, 150, bank));
        spaces.add(new ChanceSpace());
        spaces.add(new PropertySpace("Indiana Avenue", 23, "Red", 220, 18, 36, 90, 250, 700, 875, 1050, 110, 150, bank));
        spaces.add(new PropertySpace("Illinois Avenue", 24, "Red", 240, 20, 40, 100, 300, 750, 925, 1100, 120, 150, bank));
        spaces.add(new RailroadSpace("B&O Railroad", 25, 200, 25, 50, 100, 200, 100));
        spaces.add(new PropertySpace("Atlantic Avenue", 26, "Yellow", 260, 22, 44, 110, 330, 800, 975, 1150, 130, 150, bank));
        spaces.add(new PropertySpace("Ventnor Avenue", 27, "Yellow", 260, 22, 44, 110, 330, 800, 975, 1150, 130, 150, bank));
        spaces.add(new UtilitySpace("Water Works", 28, 150, 75));
        spaces.add(new PropertySpace("Marvin Gardens", 29, "Yellow", 280, 24, 48, 120, 360, 850, 1025, 1200, 140, 150, bank));
        spaces.add(new GoToJailSpace());
        spaces.add(new PropertySpace("Pacific Avenue", 31, "Green", 300, 26, 52, 130, 390, 900, 1100, 1275, 150, 200, bank));
        spaces.add(new PropertySpace("North Carolina Avenue", 32, "Green", 300, 26, 52, 130, 390, 900, 1100, 1275, 150, 200, bank));
        spaces.add(new CommunityChestSpace());
        spaces.add(new PropertySpace("Pennsylvania Avenue", 34, "Green", 320, 28, 56, 150, 450, 1000, 1200, 1400, 160, 200, bank));
        spaces.add(new RailroadSpace("Short Line", 35, 200, 25, 50, 100, 200, 100));
        spaces.add(new ChanceSpace());
        spaces.add(new PropertySpace("Park Place", 37, "Dark Blue", 350, 35, 70, 175, 500, 1100, 1300, 1500, 175, 200, bank));
        spaces.add(new TaxSpace("Luxury Tax", 38, 100, bank));
        spaces.add(new PropertySpace("Boardwalk", 39, "Dark Blue", 400, 50, 100, 200, 600, 1400, 1700, 2000, 200, 200, bank));

        ChanceCard.shuffleChanceCards(chanceDeck);
        CommunityChestCard.shuffleCommunityChestCards(communityDeck);

        if (!isTestMode) {
            assignTokensToPlayers();
        }
    }

    public void assignTokensToPlayers() {
        List<String> availableTokens = new ArrayList<>(Arrays.asList(
                "Top Hat", "Battleship", "Thimble", "Cannon", "Cat", "Iron",
                "Scottie dog", "The Shoe", "Boot", "Ducky", "Horse & Rider",
                "Penguin", "Race car", "Train", "Wheelbarrow"
        ));

        if (isTestMode) {
            for (int i = 0; i < players.size(); i++) {
                players.get(i).setToken(availableTokens.get(i % availableTokens.size()));
            }
            return;
        }

        Scanner scanner = new Scanner(System.in);
        for (Player player : players) {
            System.out.println(player.getName() + ", choose your token from the following list:");
            for (int j = 0; j < availableTokens.size(); j++) {
                System.out.println((j + 1) + ": " + availableTokens.get(j));
            }

            int choice = -1;
            while (choice < 1 || choice > availableTokens.size()) {
                System.out.print("Enter the number of your choice: ");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            String chosenToken = availableTokens.remove(choice - 1);
            player.setToken(chosenToken);
            System.out.println(player.getName() + " has chosen the " + chosenToken + " token.");
        }
    }

    public void movePlayer(Player player, int steps) {
        int oldPosition = player.getPosition();
        int newPosition = (oldPosition + steps) % spaces.size();
        if (oldPosition > newPosition) {
            bank.payPlayer(player,200);
            System.out.println(player.getName() + " passed Go and collected $200!");
        }
        player.setPosition(newPosition);
        System.out.println(player.getName() + " moved to " + spaces.get(newPosition).getName());
        spaces.get(newPosition).landOn(player);
    }

    public Space getSpace(int position) {
        return spaces.get(position);
    }

    public Stack<ChanceCard> getChanceDeck() {
        return chanceDeck;
    }

    public Stack<CommunityChestCard> getCommunityDeck() {
        return communityDeck;
    }

    public List<Space> getSpaces() {
        return spaces;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getNearestRailroad(int currentPosition) {
        int[] railroadPositions = {5, 15, 25, 35};
        for (int pos : railroadPositions) {
            if (pos > currentPosition) {
                return pos;
            }
        }
        return railroadPositions[0];
    }
}
