/**
 * Class Created by Kristian Wright

 * This class is responsible for displaying the results of rolling the dice and managing the doubles rolled count.
 * It uses the Dice class to perform the actual rolling and keeps track of the results.
 * This class also provides methods to display the roll results, the current count of doubles rolled, and to reset the doubles rolled count.
 *
 */
package View;

import Model.Dice;
import java.util.ArrayList;

public class DiceView {

    private final Dice dice;

    public DiceView(Dice dice) {
        this.dice = dice;
    }

    public String displayRollResults() {
        StringBuilder resultMessage = new StringBuilder();
        ArrayList<Integer> results = dice.rollDice();
        int totalRolls = results.size() / 2;
        int sumOfDice = 0;
        for (int i = 0; i < results.size(); i += 2) {
            int roll1 = results.get(i);
            int roll2 = results.get(i + 1);
            sumOfDice += roll1 + roll2;
            resultMessage.append("Rolled: ").append(roll1).append(" and ").append(roll2).append("\n");
            if (roll1 == roll2) {
                resultMessage.append("Rolled doubles! Total doubles rolled: ").append(dice.getDoublesRolled()).append("\n");
            }
        }
        resultMessage.append("Total rolls: ").append(totalRolls).append("\n");
        resultMessage.append("Sum of dice: ").append(sumOfDice).append("\n");
        return resultMessage.toString();
    }

    public String displayDoublesRolled() {
        return "Current doubles rolled: " + dice.getDoublesRolled();
    }

    public String resetDoublesRolled() {
        dice.resetDoublesRolled();
        return "Doubles rolled count reset.";
    }
}