/**
 *
 * Monopoly Game Board
 *
 * The GameBoard class represents the Monopoly game board and manages its spaces, players, and decks.
 * It initializes the board, handles player movements, and assigns tokens to players.
 *
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

    /**
     * Initializes the game board with all spaces.
     */
    private void initializeBoard() {
        // Add spaces to the board
        spaces.add(new GoSpace());
        spaces.add(new PropertySpace("Mediterranean Avenue", 1, "Brown", 60, 2, 4, 10, 30, 90, 160, 250, 30, 50, bank));
        // ... (other spaces initialization)
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

        List<String> availableTokens = new ArrayList<>(Arrays.asList(
                "Top Hat", "Battleship", "Thimble", "Cannon", "Cat", "Iron",
                "Scottie dog", "The Shoe", "Boot", "Ducky", "Horse & Rider",
                "Penguin", "Race car", "Train", "Wheelbarrow"
        ));

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