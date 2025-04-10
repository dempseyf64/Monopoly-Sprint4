/*
 * IncomeTaxSpace.java
 *
 * This class represents an income tax space in a board game.
 * It extends the TaxSpace class and initializes with specific parameters.
 *
 * Remodeled by Finn Dempsey
 */
package Model;

public class IncomeTaxSpace extends TaxSpace {
    public IncomeTaxSpace(Bank bank) {
        super("Income Tax", 4, 200, bank);
    }
}
