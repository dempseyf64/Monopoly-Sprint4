/**
 *
 * Monopoly Game Board
 * <p>
 * The GameBoard class represents the Monopoly game board and manages its spaces, players, and decks.
 * It initializes the board, handles player movements, and assigns tokens to players.
 * <p>
 * Created by Kristian Wright
 */
package Model;

import java.util.*;

/**
 * Represents the game board in Monopoly.
 * Manages spaces, players, chance and community chest decks, and the bank.
 */
public class GameBoard {
    private final List<Space> spaces; // List of all spaces on the board
    private final Stack<ChanceCard> chanceDeck; // Deck of Chance cards
    private final Stack<CommunityChestCard> communityDeck; // Deck of Community Chest cards
    private final List<Player> players; // List of players in the game
    private final boolean isTestMode; // Indicates if the game is in test mode
    private final Bank bank; // The bank managing game finances
    private final List<String> availableTokens = new ArrayList<>(Arrays.asList(
            "Boat", "Cannon", "Car", "Cat", "Dog", "Duck",
            "Hat", "Horse", "Iron", "Penguin", "Shoe",
            "Thimble" //missing Train, Wheelbarrow, Boot/Shoe
    ));

    /**
     * Constructs a GameBoard with the specified players, test mode, and bank.
     *
     * @param players   The list of players in the game.
     * @param isTestMode Whether the game is in test mode.
     * @param bank      The bank managing game finances.
     * @throws IllegalArgumentException if players or bank is null.
     */
    public GameBoard(List<Player> players, boolean isTestMode, Bank bank) {
        if (players == null) {
            throw new IllegalArgumentException("Players list cannot be null");
        }
        if (bank == null) {
            throw new IllegalArgumentException("Bank cannot be null");
        }

        this.players = players;
        this.isTestMode = isTestMode;
        this.bank = bank;
        this.spaces = new ArrayList<>();
        this.chanceDeck = ChanceCard.initializeChanceCards(bank);
        this.communityDeck = CommunityChestCard.initializeCommunityChestCards(this, bank);
        initializeBoard();
    }

    /**
     * Adds a player to the game.
     *
     * @param player The player to add.
     */
    public void addPlayer(Player player) {
        if (player != null) {
            players.add(player);
        } else {
            System.out.println("Cannot add a null player.");
        }
    }

    private void initializeBoard() {
        // Add spaces to the board
        spaces.add(new GoSpace());
        spaces.add(new PropertySpace("Mediterranean Avenue", 1, "Brown", 60, 2, 4, 10, 30, 90, 160, 250, 30, 50, bank));
        spaces.add(new CommunityChestSpace());
        spaces.add(new PropertySpace("Baltic Avenue", 3, "Brown", 60, 4, 8, 20, 60, 180, 320, 450, 30, 50, bank));
        spaces.add(new TaxSpace("Income Tax", 4, 200, bank));
        spaces.add(new RailroadSpace("Reading Railroad", 5, 200, 25, 100, 50, 100, 200)); // Added missing argument
        spaces.add(new PropertySpace("Oriental Avenue", 6, "Light Blue", 100, 6, 12, 30, 90, 270, 400, 550, 50, 50, bank));
        spaces.add(new ChanceSpace());
        spaces.add(new PropertySpace("Vermont Avenue", 8, "Light Blue", 100, 6, 12, 30, 90, 270, 400, 550, 50, 50, bank));
        spaces.add(new PropertySpace("Connecticut Avenue", 9, "Light Blue", 120, 8, 16, 40, 100, 300, 450, 600, 60, 50, bank));
        spaces.add(new JailSpace());
        spaces.add(new PropertySpace("St. Charles Place", 11, "Pink", 140, 10, 20, 50, 150, 450, 625, 750, 70, 100, bank));
        spaces.add(new UtilitySpace("Electric Company", 12, 150, 4));
        spaces.add(new PropertySpace("States Avenue", 13, "Pink", 140, 10, 20, 50, 150, 450, 625, 750, 70, 100, bank));
        spaces.add(new PropertySpace("Virginia Avenue", 14, "Pink", 160, 12, 24, 60, 180, 500, 700, 900, 80, 100, bank));
        spaces.add(new RailroadSpace("Pennsylvania Railroad", 15, 200, 25, 100, 50, 100, 200)); // Added missing argument
        spaces.add(new PropertySpace("St. James Place", 16, "Orange", 180, 14, 28, 70, 200, 550, 750, 950, 90, 100, bank));
        spaces.add(new CommunityChestSpace());
        spaces.add(new PropertySpace("Tennessee Avenue", 18, "Orange", 180, 14, 28, 70, 200, 550, 750, 950, 90, 100, bank));
        spaces.add(new PropertySpace("New York Avenue", 19, "Orange", 200, 16, 32, 80, 220, 600, 800, 1000, 100, 100, bank));
        spaces.add(new FreeParkingSpace());
        spaces.add(new PropertySpace("Kentucky Avenue", 21, "Red", 220, 18, 36, 90, 250, 700, 875, 1050, 110, 150, bank));
        spaces.add(new ChanceSpace());
        spaces.add(new PropertySpace("Indiana Avenue", 23, "Red", 220, 18, 36, 90, 250, 700, 875, 1050, 110, 150, bank));
        spaces.add(new PropertySpace("Illinois Avenue", 24, "Red", 240, 20, 40, 100, 300, 750, 925, 1100, 120, 150, bank));
        spaces.add(new RailroadSpace("B&O Railroad", 25, 200, 25, 100, 50, 100, 200)); // Added missing argument
        spaces.add(new PropertySpace("Atlantic Avenue", 26, "Yellow", 260, 22, 44, 110, 330, 800, 975, 1150, 130, 150, bank));
        spaces.add(new PropertySpace("Ventnor Avenue", 27, "Yellow", 260, 22, 44, 110, 330, 800, 975, 1150, 130, 150, bank));
        spaces.add(new UtilitySpace("Water Works", 28, 150, 4));
        spaces.add(new PropertySpace("Marvin Gardens", 29, "Yellow", 280, 24, 48, 120, 360, 850, 1025, 1200, 140, 150, bank));
        spaces.add(new GoToJailSpace());
        spaces.add(new PropertySpace("Pacific Avenue", 31, "Green", 300, 26, 52, 130, 390, 900, 1100, 1275, 150, 200, bank));
        spaces.add(new PropertySpace("North Carolina Avenue", 32, "Green", 300, 26, 52, 130, 390, 900, 1100, 1275, 150, 200, bank));
        spaces.add(new CommunityChestSpace());
        spaces.add(new PropertySpace("Pennsylvania Avenue", 34, "Green", 320, 28, 56, 150, 450, 1000, 1200, 1400, 160, 200, bank));
        spaces.add(new RailroadSpace("Short Line", 35, 200, 25, 100, 50, 100, 200)); // Added missing argument
        spaces.add(new ChanceSpace());
        spaces.add(new PropertySpace("Park Place", 37, "Dark Blue", 350, 35, 70, 175, 500, 1100, 1300, 1500, 175, 200, bank));
        spaces.add(new TaxSpace("Luxury Tax", 38, 100, bank));
        spaces.add(new PropertySpace("Boardwalk", 39, "Dark Blue", 400, 50, 100, 200, 600, 1400, 1700, 2000, 200, 200, bank));

        // Shuffle decks
        ChanceCard.shuffleChanceCards(chanceDeck);
        CommunityChestCard.shuffleCommunityChestCards(communityDeck);

        // Assign tokens if not in test mode
        if (!isTestMode) {
            assignTokensToPlayers();
        }
    }

    /**
     * Assigns tokens to players.
     */
    public void assignTokensToPlayers() {
        if (players == null || players.isEmpty()) {
            System.out.println("No players to assign tokens to.");
            return;
        }

        for (Player player : players) {
            if (player instanceof ComputerPlayer) {
                String chosenToken = availableTokens.remove(0);
                player.setToken(chosenToken);
                System.out.println(player.getName() + " (Computer) has chosen the " + chosenToken + " token.");
            } else {
                System.out.println(player.getName() + ", choose your token from the following list:");
                for (int j = 0; j < availableTokens.size(); j++) {
                    System.out.println((j + 1) + ": " + availableTokens.get(j));
                }

                Scanner scanner = new Scanner(System.in);
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
    }

    /**
     * Moves a player by the specified number of steps.
     *
     * @param player The player to move.
     * @param steps  The number of steps to move.
     */
    public void movePlayer(Player player, int steps) {
        if (player == null || spaces.isEmpty()) {
            System.out.println("Invalid player or board is not initialized.");
            return;
        }

        int oldPosition = player.getPosition();
        int newPosition = (oldPosition + steps) % spaces.size();
        if (oldPosition > newPosition) {
            bank.payPlayer(player, 200);
            System.out.println(player.getName() + " passed Go and collected $200!");
        }
        player.setPosition(newPosition);
        System.out.println(player.getName() + " moved to " + spaces.get(newPosition).getName());
        spaces.get(newPosition).landOn(player);
    }

    /**
     * Gets the space at the specified position.
     *
     * @param position The position on the board.
     * @return The space at the specified position.
     * @throws IndexOutOfBoundsException if the position is invalid.
     */
    public Space getSpace(int position) {
        if (position < 0 || position >= spaces.size()) {
            throw new IndexOutOfBoundsException("Invalid position on the board.");
        }
        return spaces.get(position);
    }

    /**
     * Gets the Chance deck.
     *
     * @return The Chance deck.
     */
    public Stack<ChanceCard> getChanceDeck() {
        return chanceDeck;
    }

    /**
     * Gets the Community Chest deck.
     *
     * @return The Community Chest deck.
     */
    public Stack<CommunityChestCard> getCommunityDeck() {
        return communityDeck;
    }

    /**
     * Gets the list of spaces on the board.
     *
     * @return The list of spaces.
     */
    public List<Space> getSpaces() {
        return spaces;
    }

    /**
     * Gets the list of players in the game.
     *
     * @return The list of players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the list of available tokens upon selection.
     *
     * @return The list of available tokens.
     */
    public List<String> getAvailableTokens() {
        return availableTokens;
    }

    /**
     * Finds the nearest railroad space from the current position.
     *
     * @param currentPosition The current position of the player.
     * @return The position of the nearest railroad.
     */
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