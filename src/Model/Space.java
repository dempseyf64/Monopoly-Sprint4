/*
 * Space.java
 *
 * This is an abstract class representing a space on the board in a game.
 * It contains a name and an abstract method landOn which will be implemented by subclasses.
 *
 * Created by Kristian Wright
 */
package Model;

public abstract class Space {
    protected String name;

    public Space(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void landOn(Player player);
}