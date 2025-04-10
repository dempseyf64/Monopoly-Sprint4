/**`
 * CommunityChestCard.java
 *
 * This class represents a Community Chest card in the Monopoly game.
 * Each card has a description and an effect that is applied to a player.
 * The class also provides methods to initialize and shuffle the deck of cards.
 *
 * Created by: ... modified by Collin Cabral-Castro
 */
package Model;

import java.util.Collections;
import java.util.Stack;
import java.util.function.Consumer;

public class CommunityChestCard extends Card {

    private GameBoard gameBoard;
    private Bank bank;

    public CommunityChestCard(String description, Consumer<Player> effect) {
        super(description, effect);
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public static Stack<CommunityChestCard> initializeCommunityChestCards(GameBoard gameBoard, Bank bank) {
        Stack<CommunityChestCard> communityDeck = new Stack<>();
        communityDeck.add(new CommunityChestCard("Advance to Go (Collect $200).", player -> {player.setPosition(0); bank.payPlayer(player, 200);}));
        communityDeck.add(new CommunityChestCard("Bank error in your favor. Collect $200.", player -> bank.payPlayer(player, 200)));
        communityDeck.add(new CommunityChestCard("Doctor’s fee. Pay $50.", player -> bank.collectFromPlayer(player, 50)));
        communityDeck.add(new CommunityChestCard("From sale of stock you get $50.", player -> bank.payPlayer(player, 50)));
        communityDeck.add(new CommunityChestCard("Get Out of Jail Free.", Player::receiveGetOutOfJailFreeCard));
        communityDeck.add(new CommunityChestCard("Go to Jail. Go directly to jail, do not pass Go, do not collect $200.", Player::goToJail));
        communityDeck.add(new CommunityChestCard("Holiday fund matures. Receive $100.", player -> bank.payPlayer(player, 100)));
        communityDeck.add(new CommunityChestCard("Income tax refund. Collect $20.", player -> bank.payPlayer(player, 20)));
        communityDeck.add(new CommunityChestCard("It is your birthday. Collect $10 from every player.", player -> {
            CommunityChestCard card = new CommunityChestCard("", null);
            card.setGameBoard(gameBoard);
            card.setBank(bank);
            card.collectFromEachPlayer(player, 10);
        }));
        communityDeck.add(new CommunityChestCard("Life insurance matures. Collect $100.", player -> bank.payPlayer(player, 100)));
        communityDeck.add(new CommunityChestCard("Pay hospital fees of $100.", player -> bank.collectFromPlayer(player, 100)));
        communityDeck.add(new CommunityChestCard("Pay school fees of $50.", player -> bank.collectFromPlayer(player, 50)));
        communityDeck.add(new CommunityChestCard("Receive $25 consultancy fee.", player -> bank.payPlayer(player, 25)));
        communityDeck.add(new CommunityChestCard("You are assessed for street repair. $40 per house. $115 per hotel.", player -> {
            // Implement the assessStreetRepairs logic here
        }));
        communityDeck.add(new CommunityChestCard("You have won second prize in a beauty contest. Collect $10.", player -> bank.payPlayer(player, 10)));
        communityDeck.add(new CommunityChestCard("You inherit $100.", player -> bank.payPlayer(player, 100)));
        return communityDeck;
    }

    public static void shuffleCommunityChestCards(Stack<CommunityChestCard> communityDeck) {
        Collections.shuffle(communityDeck);
    }

    public void collectFromEachPlayer(Player birthdayPlayer, int amount) {
        for (Player player : gameBoard.getPlayers()) {
            if (!player.equals(birthdayPlayer)) {
                bank.collectFromPlayer(player, amount);
                bank.payPlayer(birthdayPlayer, amount);
            }
        }
        System.out.println(birthdayPlayer.getName() + " collected $" + amount + " from each player.");
    }
}