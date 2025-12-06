package org.pokerapp.pokersimulationcopilotagent.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Represents the evaluation of a poker hand with its rank and tiebreaker values
 */
@Data
@AllArgsConstructor
public class HandEvaluation implements Comparable<HandEvaluation> {
    private HandRank handRank;
    private List<Integer> tiebreakers; // Values used to break ties (e.g., kickers)

    @Override
    public int compareTo(HandEvaluation other) {
        // First compare by hand rank
        int rankCompare = Integer.compare(this.handRank.getValue(), other.handRank.getValue());
        if (rankCompare != 0) {
            return rankCompare;
        }

        // If same rank, compare tiebreakers in order
        int minSize = Math.min(this.tiebreakers.size(), other.tiebreakers.size());
        for (int i = 0; i < minSize; i++) {
            int compare = Integer.compare(this.tiebreakers.get(i), other.tiebreakers.get(i));
            if (compare != 0) {
                return compare;
            }
        }

        return 0; // Exact tie
    }
}

