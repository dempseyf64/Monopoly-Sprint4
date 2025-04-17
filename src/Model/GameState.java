/**
 * The GameState class represents the state of the Monopoly game.
 * It manages the game board, players, dice, and the flow of turns.
 * This class handles player turns, jail conditions, and determines the current player.
 *
 * Responsibilities:
 * - Tracks the current player and their actions.
 * - Manages the flow of the game, including handling turns and jail conditions.
 * - Provides access to the game board, dice, and players.
 *
 * Fixed by Collin Castro and refactored by Kristian Wright.
 */
package Model;

import java.util.List;

public class GameState {
    private final GameBoard board; // The game board
    private final Dice dice; // The dice used in the game
    private final List<Player> players; // List of players in the game
    private int currentPlayerIndex; // Index of the current player
    private boolean gameOver; // Flag indicating if the game is over

    /**
     * Constructs a GameState with the specified game board.
     *
     * @param board The game board.
     */
    public GameState(GameBoard board) {
        this.board = board;
        this.dice = Dice.getInstance();
        this.players = board.getPlayers();
        this.currentPlayerIndex = 0;
        this.gameOver = false;
    }

    /**
     * Gets the current player.
     *
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Advances to the next player's turn.
     */
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        dice.resetDoublesRolled();
    }

    /**
     * Handles the current player's turn.
     */
    public void handleTurn() {
        Player current = getCurrentPlayer();
        if (current.isInJail()) {
            handleJailTurn(current);
            return;
        }

        List<Integer> roll = dice.rollDice();
        int total = roll.get(0) + roll.get(1);
        board.movePlayer(current, total);

        if (current instanceof ComputerPlayer) {
            ((ComputerPlayer) current).makeDecision();
        }

        if (dice.getDoublesRolled() > 0) {
            System.out.println(current.getName() + " rolled doubles and gets another turn!");
        } else {
            nextTurn();
        }
    }

    /**
     * Handles the turn of a player in jail.
     *
     * @param player The player in jail.
     */
    private void handleJailTurn(Player player) {
        if (player.hasGetOutOfJailFreeCard()) {
            System.out.println(player.getName() + " used a Get Out of Jail Free card.");
            player.receiveGetOutOfJailFreeCard(); // Resets flag
            player.setPosition(10);
            player.getDice().resetDoublesRolled();
            player.move(1); // Move out of jail
            player.goToJail(); // Should release the player
        } else {
            List<Integer> roll = player.getDice().rollDice();
            if (roll.get(0).equals(roll.get(1))) {
                System.out.println(player.getName() + " rolled doubles and is released from jail!");
                player.setPosition((player.getPosition() + roll.get(0) + roll.get(1)) % 40);
                player.goToJail(); // Should release the player
            } else {
                System.out.println(player.getName() + " did not roll doubles and stays in jail.");
                nextTurn();
            }
        }
    }

    /**
     * Checks if the game is over.
     *
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Ends the game.
     */
    public void endGame() {
        this.gameOver = true;
        System.out.println("Game Over!");
    }

    // Interface methods for Controller and View

    /**
     * Gets the game board.
     *
     * @return The game board.
     */
    public GameBoard getBoard() {
        return board;
    }

    /**
     * Gets the dice used in the game.
     *
     * @return The dice.
     */
    public Dice getDice() {
        return dice;
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
     * Gets the index of the current player.
     *
     * @return The index of the current player.
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Sets the index of the current player.
     *
     * @param currentPlayerIndex The new index of the current player.
     */
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * Sets the game over flag.
     *
     * @param gameOver True to end the game, false otherwise.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}