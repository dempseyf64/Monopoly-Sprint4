/*
 * Card.java
 *
 * This class represents a card in the game. It contains a description and an effect that can be applied to a player.
 *
 * Created by Kristian Wright.
 */
package Model;

import java.util.function.Consumer;

public abstract class Card {
    protected final String description;
    protected final Consumer<Player> effect;

    public Card(String description, Consumer<Player> effect) {
        this.description = description;
        this.effect = effect;
    }

    public void apply(Player player) {
        effect.accept(player);
    }

    public String getDescription() {
        return description;
    }
}