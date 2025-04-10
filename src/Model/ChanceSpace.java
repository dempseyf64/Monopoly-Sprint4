/*
 * This file is part of the Monopoly game implementation.
 * It defines the ChanceSpace class which represents a space on the board where players can draw a chance card.
*
*  Remodeled by Finn Dempsey
 */
package Model;

public class ChanceSpace extends Space {
    public ChanceSpace() {
        super("Chance");
    }

    @Override
    public void landOn(Player player) {
        GameBoard gameBoard = player.getGameBoard();
        ChanceCard card = gameBoard.getChanceDeck().pop();
        System.out.println(player.getName() + " drew a Chance card: " + card.getDescription());
        card.apply(player);
        gameBoard.getChanceDeck().push(card); // Optionally, put the card back at the bottom of the deck
    }
}
