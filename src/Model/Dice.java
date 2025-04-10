/**
 * Created by Finn Dempsey
 * Refactored by Kristian Wright

 * This class represents a Die object used in the game.
 * It provides functionality to roll two dice and keep track of the number of doubles rolled.
 */

package Model;

import java.util.ArrayList;
import java.util.Random;

public class Dice {
    private static Dice uniqueInstance;
    private int doublesRolled;
    private final Random random;

    private Dice() {
        this.doublesRolled = 0;
        this.random = new Random();
    }

    public static Dice getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Dice();
        }
        return uniqueInstance;
    }

    public ArrayList<Integer> rollDice() {
        ArrayList<Integer> results = new ArrayList<>();
        int die1 = random.nextInt(6) + 1;
        int die2 = random.nextInt(6) + 1;
        results.add(die1);
        results.add(die2);

        if (die1 == die2) {
            doublesRolled++;
        }

        return results;
    }

    public int getDoublesRolled() {
        return doublesRolled;
    }

    public void resetDoublesRolled() {
        doublesRolled = 0;
    }
}