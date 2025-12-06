package org.pokerapp.pokersimulationcopilotagent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a playing card with a rank and suit
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card implements Comparable<Card> {
    private Rank rank;
    private Suit suit;

    /**
     * Parse a card from string format (e.g., "AS", "KH", "10D", "2C")
     */
    public static Card fromString(String cardStr) {
        if (cardStr == null || cardStr.length() < 2) {
            throw new IllegalArgumentException("Invalid card format: " + cardStr);
        }

        // Last character is suit
        String suitSymbol = cardStr.substring(cardStr.length() - 1);
        // Everything before is rank
        String rankSymbol = cardStr.substring(0, cardStr.length() - 1);

        Rank rank = Rank.fromSymbol(rankSymbol);
        Suit suit = Suit.fromSymbol(suitSymbol);

        return new Card(rank, suit);
    }

    @Override
    public String toString() {
        return rank.getSymbol() + suit.getSymbol();
    }

    @Override
    public int compareTo(Card other) {
        return Integer.compare(this.rank.getValue(), other.rank.getValue());
    }
}

