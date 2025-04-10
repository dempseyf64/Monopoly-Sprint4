/**
 * Class Created by Kristian Wright

 * This is the main class for testing the Dice and DiceView classes.
 * It creates a Die object and a DiceView object, rolls the dice until 3 doubles are rolled,
 * and then resets the doubles rolled count.
 */
package View;

import Model.Dice;

public class DiceMain {
    public static void main(String[] args) {
        Dice dice = Dice.getInstance();
        DiceView diceView = new DiceView(dice);

        // Roll the dice until 3 doubles are rolled
        while (dice.getDoublesRolled() < 3) {
            System.out.println(diceView.displayRollResults());
            System.out.println(diceView.displayDoublesRolled());
        }

        // Reset doubles rolled count
        System.out.println(diceView.resetDoublesRolled());
    }
}