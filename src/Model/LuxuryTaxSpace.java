/*
 * LuxuryTaxSpace.java
 *
 * This class represents a Luxury Tax space on the Monopoly board.
 * It extends the TaxSpace class with specific values for Luxury Tax.
 *
 * Remodeled by Finn Dempsey
 */
package Model;

public class LuxuryTaxSpace extends TaxSpace {

    public LuxuryTaxSpace(Bank bank) {
        super("Luxury Tax", 38, 75, bank);
    }
}
