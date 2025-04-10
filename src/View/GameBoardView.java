/**
 * Class Created by Kristian Wright

 * This class is responsible for displaying the game board and player status.
 * It uses the GameBoard object to retrieve information about the spaces and players.
 */

package View;

import Model.GameBoard;
import Model.Player;
import Model.Space;
import Model.PropertySpace;

public class GameBoardView {
    private final GameBoard gameBoard;

    public GameBoardView(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void displayBoard() {
        System.out.println("Displaying game board...");
        for (int i = 0; i < gameBoard.getSpaces().size(); i++) {
            Space space = gameBoard.getSpace(i);
            StringBuilder spaceInfo = new StringBuilder(i + ": " + space.getName());

            // Check if the space is a property and display ownership information
            if (space instanceof PropertySpace property) {
                if (property.getOwner() != null) {
                    spaceInfo.append(" (Owned by ").append(property.getOwner().getName()).append(")");
                } else {
                    spaceInfo.append(" (Unowned)");
                }
            }

            // Check if any players are on this space
            for (Player player : gameBoard.getPlayers()) {
                if (player.getPosition() == i) {
                    spaceInfo.append(" [").append(player.getName()).append("]");
                }
            }

            System.out.println(spaceInfo);
        }
        System.out.println("Finished displaying game board.");
    }

    public void displayPlayerStatus() {
        System.out.println("Displaying player status...");
        for (Player player : gameBoard.getPlayers()) {
            StringBuilder playerInfo = new StringBuilder(player.getName() + " - Position: " + player.getPosition() + ", Money: $" + player.getMoney());
            if (player.isInJail()) {
                playerInfo.append(", In Jail");
            }
            playerInfo.append(", Properties: ");
            for (Space space : gameBoard.getSpaces()) {
                if (space instanceof PropertySpace property && property.getOwner() == player) {
                    playerInfo.append(property.getName()).append(", ");
                }
            }
            System.out.println(playerInfo);
        }
        System.out.println("Finished displaying player status.");
    }
}