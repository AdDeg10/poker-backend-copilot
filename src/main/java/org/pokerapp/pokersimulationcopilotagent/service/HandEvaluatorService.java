package org.pokerapp.pokersimulationcopilotagent.service;

import org.pokerapp.pokersimulationcopilotagent.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for evaluating poker hands according to Texas Hold'em rules
 */
@Service
public class HandEvaluatorService {

    /**
     * Evaluate the best 5-card hand from a combination of hole cards and community cards
     */
    public HandEvaluation evaluateHand(List<Card> holeCards, List<Card> communityCards) {
        List<Card> allCards = new ArrayList<>(holeCards);
        allCards.addAll(communityCards);

        if (allCards.size() < 5) {
            throw new IllegalArgumentException("Need at least 5 cards to evaluate a hand");
        }

        // Generate all possible 5-card combinations
        List<List<Card>> combinations = generateCombinations(allCards, 5);

        // Find the best hand
        HandEvaluation bestEvaluation = null;
        for (List<Card> combination : combinations) {
            HandEvaluation evaluation = evaluateFiveCardHand(combination);
            if (bestEvaluation == null || evaluation.compareTo(bestEvaluation) > 0) {
                bestEvaluation = evaluation;
            }
        }

        return bestEvaluation;
    }

    /**
     * Evaluate a specific 5-card hand
     */
    private HandEvaluation evaluateFiveCardHand(List<Card> cards) {
        if (cards.size() != 5) {
            throw new IllegalArgumentException("Must have exactly 5 cards");
        }

        List<Card> sortedCards = cards.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        boolean isFlush = isFlush(sortedCards);
        boolean isStraight = isStraight(sortedCards);
        boolean isWheel = isWheel(sortedCards);
        Map<Rank, Integer> rankCounts = countRanks(sortedCards);

        // Royal Flush
        if (isFlush && isStraight && sortedCards.get(0).getRank() == Rank.ACE && !isWheel) {
            return new HandEvaluation(HandRank.ROYAL_FLUSH, List.of(Rank.ACE.getValue()));
        }

        // Straight Flush
        if (isFlush && isStraight) {
            int straightHighCard = isWheel ? Rank.FIVE.getValue() : sortedCards.get(0).getRank().getValue();
            return new HandEvaluation(HandRank.STRAIGHT_FLUSH, List.of(straightHighCard));
        }

        // Four of a Kind
        List<Rank> fourOfAKind = findNOfAKind(rankCounts, 4);
        if (!fourOfAKind.isEmpty()) {
            Rank quad = fourOfAKind.get(0);
            Rank kicker = sortedCards.stream()
                    .map(Card::getRank)
                    .filter(r -> r != quad)
                    .findFirst()
                    .orElse(null);
            return new HandEvaluation(HandRank.FOUR_OF_A_KIND,
                    List.of(quad.getValue(), kicker.getValue()));
        }

        // Full House
        List<Rank> threeOfAKind = findNOfAKind(rankCounts, 3);
        List<Rank> pair = findNOfAKind(rankCounts, 2);
        if (!threeOfAKind.isEmpty() && !pair.isEmpty()) {
            return new HandEvaluation(HandRank.FULL_HOUSE,
                    List.of(threeOfAKind.get(0).getValue(), pair.get(0).getValue()));
        }

        // Flush
        if (isFlush) {
            List<Integer> kickers = sortedCards.stream()
                    .map(c -> c.getRank().getValue())
                    .collect(Collectors.toList());
            return new HandEvaluation(HandRank.FLUSH, kickers);
        }

        // Straight
        if (isStraight) {
            int straightHighCard = isWheel ? Rank.FIVE.getValue() : sortedCards.get(0).getRank().getValue();
            return new HandEvaluation(HandRank.STRAIGHT, List.of(straightHighCard));
        }

        // Three of a Kind
        if (!threeOfAKind.isEmpty()) {
            Rank trips = threeOfAKind.get(0);
            List<Integer> kickers = sortedCards.stream()
                    .map(Card::getRank)
                    .filter(r -> r != trips)
                    .sorted(Comparator.reverseOrder())
                    .map(Rank::getValue)
                    .limit(2)
                    .collect(Collectors.toList());
            List<Integer> tiebreakers = new ArrayList<>();
            tiebreakers.add(trips.getValue());
            tiebreakers.addAll(kickers);
            return new HandEvaluation(HandRank.THREE_OF_A_KIND, tiebreakers);
        }

        // Two Pair
        if (pair.size() >= 2) {
            List<Rank> sortedPairs = pair.stream()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
            Rank highPair = sortedPairs.get(0);
            Rank lowPair = sortedPairs.get(1);
            Rank kicker = sortedCards.stream()
                    .map(Card::getRank)
                    .filter(r -> r != highPair && r != lowPair)
                    .findFirst()
                    .orElse(null);
            return new HandEvaluation(HandRank.TWO_PAIR,
                    List.of(highPair.getValue(), lowPair.getValue(), kicker.getValue()));
        }

        // One Pair
        if (!pair.isEmpty()) {
            Rank pairRank = pair.get(0);
            List<Integer> kickers = sortedCards.stream()
                    .map(Card::getRank)
                    .filter(r -> r != pairRank)
                    .sorted(Comparator.reverseOrder())
                    .map(Rank::getValue)
                    .limit(3)
                    .collect(Collectors.toList());
            List<Integer> tiebreakers = new ArrayList<>();
            tiebreakers.add(pairRank.getValue());
            tiebreakers.addAll(kickers);
            return new HandEvaluation(HandRank.ONE_PAIR, tiebreakers);
        }

        // High Card
        List<Integer> kickers = sortedCards.stream()
                .map(c -> c.getRank().getValue())
                .collect(Collectors.toList());
        return new HandEvaluation(HandRank.HIGH_CARD, kickers);
    }

    private boolean isFlush(List<Card> cards) {
        Suit firstSuit = cards.get(0).getSuit();
        return cards.stream().allMatch(c -> c.getSuit() == firstSuit);
    }

    private boolean isStraight(List<Card> cards) {
        List<Card> sorted = cards.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        // Check regular straight
        boolean regularStraight = true;
        for (int i = 0; i < sorted.size() - 1; i++) {
            if (sorted.get(i).getRank().getValue() - sorted.get(i + 1).getRank().getValue() != 1) {
                regularStraight = false;
                break;
            }
        }

        // Check for A-2-3-4-5 (wheel)
        boolean wheelStraight = isWheel(sorted);

        return regularStraight || wheelStraight;
    }

    private boolean isWheel(List<Card> cards) {
        return cards.get(0).getRank() == Rank.ACE &&
                cards.get(1).getRank() == Rank.FIVE &&
                cards.get(2).getRank() == Rank.FOUR &&
                cards.get(3).getRank() == Rank.THREE &&
                cards.get(4).getRank() == Rank.TWO;
    }

    private Map<Rank, Integer> countRanks(List<Card> cards) {
        Map<Rank, Integer> counts = new HashMap<>();
        for (Card card : cards) {
            counts.put(card.getRank(), counts.getOrDefault(card.getRank(), 0) + 1);
        }
        return counts;
    }

    private List<Rank> findNOfAKind(Map<Rank, Integer> rankCounts, int n) {
        return rankCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == n)
                .map(Map.Entry::getKey)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    private List<List<Card>> generateCombinations(List<Card> cards, int k) {
        List<List<Card>> result = new ArrayList<>();
        generateCombinationsHelper(cards, k, 0, new ArrayList<>(), result);
        return result;
    }

    private void generateCombinationsHelper(List<Card> cards, int k, int start,
                                           List<Card> current, List<List<Card>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < cards.size(); i++) {
            current.add(cards.get(i));
            generateCombinationsHelper(cards, k, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}
