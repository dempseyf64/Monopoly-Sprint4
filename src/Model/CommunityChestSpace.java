/*
 * CommunityChestSpace.java
 *
 * This class represents a space on the game board that allows players to draw a Community Chest card.
 *
 * Remodeled by Finn Dempsey
 */
package Model;

public class CommunityChestSpace extends Space {
    public CommunityChestSpace() {
        super("Community Chest");
    }

    @Override
    public void landOn(Player player) {
        GameBoard gameBoard = player.getGameBoard();
        CommunityChestCard card = gameBoard.getCommunityDeck().pop();
        System.out.println(player.getName() + " drew a Community Chest card: " + card.getDescription());
        card.apply(player);
        gameBoard.getCommunityDeck().push(card); // Optionally, put the card back at the bottom of the deck
    }
}