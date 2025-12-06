package org.pokerapp.pokersimulationcopilotagent.model;

/**
 * Enum representing card suits in poker
 */
public enum Suit {
    HEARTS("H", "Hearts"),
    DIAMONDS("D", "Diamonds"),
    CLUBS("C", "Clubs"),
    SPADES("S", "Spades");

    private final String symbol;
    private final String name;

    Suit(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    /**
     * Parse a suit from its symbol (e.g., "H", "D", "C", "S")
     */
    public static Suit fromSymbol(String symbol) {
        for (Suit suit : values()) {
            if (suit.symbol.equalsIgnoreCase(symbol)) {
                return suit;
            }
        }
        throw new IllegalArgumentException("Invalid suit symbol: " + symbol);
    }
}

