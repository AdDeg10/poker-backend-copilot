package org.pokerapp.pokersimulationcopilotagent.service;

import org.pokerapp.pokersimulationcopilotagent.dto.*;
import org.pokerapp.pokersimulationcopilotagent.model.Card;
import org.pokerapp.pokersimulationcopilotagent.model.HandEvaluation;
import org.pokerapp.pokersimulationcopilotagent.model.Rank;
import org.pokerapp.pokersimulationcopilotagent.model.Suit;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for running poker simulations
 */
@Service
public class PokerSimulationService {

    private final HandEvaluatorService handEvaluatorService;
    private final Random random = new Random();

    public PokerSimulationService(HandEvaluatorService handEvaluatorService) {
        this.handEvaluatorService = handEvaluatorService;
    }

    /**
     * Run poker simulations based on the request
     */
    public SimulationResponse runSimulation(SimulationRequest request) {
        request.validate();

        // Parse cards
        Map<String, List<Card>> playerHoleCards = new HashMap<>();
        for (PlayerRequest player : request.getPlayers()) {
            List<Card> holeCards = player.getHoleCards().stream()
                    .map(Card::fromString)
                    .collect(Collectors.toList());
            playerHoleCards.put(player.getName(), holeCards);
        }

        List<Card> fixedCommunityCards = request.getCommunityCards() != null
                ? request.getCommunityCards().stream()
                .map(Card::fromString)
                .collect(Collectors.toList())
                : new ArrayList<>();

        // Validate no duplicate cards
        validateNoDuplicates(playerHoleCards, fixedCommunityCards);

        // Track wins
        Map<String, Integer> wins = new HashMap<>();
        int ties = 0;

        for (String playerName : playerHoleCards.keySet()) {
            wins.put(playerName, 0);
        }

        // Run simulations
        for (int i = 0; i < request.getNumberOfSimulations(); i++) {
            // Create a deck without used cards
            List<Card> deck = createDeck();
            removeUsedCards(deck, playerHoleCards, fixedCommunityCards);

            // Complete community cards if needed
            List<Card> communityCards = new ArrayList<>(fixedCommunityCards);
            while (communityCards.size() < 5) {
                communityCards.add(drawCard(deck));
            }

            // Evaluate each player's hand
            Map<String, HandEvaluation> evaluations = new HashMap<>();
            for (Map.Entry<String, List<Card>> entry : playerHoleCards.entrySet()) {
                HandEvaluation eval = handEvaluatorService.evaluateHand(entry.getValue(), communityCards);
                evaluations.put(entry.getKey(), eval);
            }

            // Find winner(s)
            List<String> winners = findWinners(evaluations);
            if (winners.size() == 1) {
                wins.put(winners.get(0), wins.get(winners.get(0)) + 1);
            } else {
                ties++;
            }
        }

        // Calculate percentages
        List<PlayerResult> playerResults = new ArrayList<>();
        for (String playerName : playerHoleCards.keySet()) {
            double winPercentage = (wins.get(playerName) * 100.0) / request.getNumberOfSimulations();
            PlayerResult result = new PlayerResult();
            result.setName(playerName);
            result.setWinPercentage(winPercentage);

            // If 5 community cards provided, include best hand rank
            if (fixedCommunityCards.size() == 5) {
                HandEvaluation eval = handEvaluatorService.evaluateHand(
                        playerHoleCards.get(playerName), fixedCommunityCards);
                result.setBestHandRank(eval.getHandRank().getDisplayName());
            }

            playerResults.add(result);
        }

        double tiePercentage = (ties * 100.0) / request.getNumberOfSimulations();

        return new SimulationResponse(playerResults, tiePercentage, request.getNumberOfSimulations());
    }

    private List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        return deck;
    }

    private void removeUsedCards(List<Card> deck, Map<String, List<Card>> playerHoleCards,
                                 List<Card> communityCards) {
        Set<String> usedCards = new HashSet<>();

        for (List<Card> holeCards : playerHoleCards.values()) {
            for (Card card : holeCards) {
                usedCards.add(card.toString());
            }
        }

        for (Card card : communityCards) {
            usedCards.add(card.toString());
        }

        deck.removeIf(card -> usedCards.contains(card.toString()));
    }

    private Card drawCard(List<Card> deck) {
        int index = random.nextInt(deck.size());
        return deck.remove(index);
    }

    private List<String> findWinners(Map<String, HandEvaluation> evaluations) {
        HandEvaluation bestEvaluation = null;
        List<String> winners = new ArrayList<>();

        for (Map.Entry<String, HandEvaluation> entry : evaluations.entrySet()) {
            if (bestEvaluation == null) {
                bestEvaluation = entry.getValue();
                winners.add(entry.getKey());
            } else {
                int comparison = entry.getValue().compareTo(bestEvaluation);
                if (comparison > 0) {
                    bestEvaluation = entry.getValue();
                    winners.clear();
                    winners.add(entry.getKey());
                } else if (comparison == 0) {
                    winners.add(entry.getKey());
                }
            }
        }

        return winners;
    }

    private void validateNoDuplicates(Map<String, List<Card>> playerHoleCards,
                                     List<Card> communityCards) {
        Set<String> allCards = new HashSet<>();

        for (List<Card> holeCards : playerHoleCards.values()) {
            for (Card card : holeCards) {
                String cardStr = card.toString();
                if (allCards.contains(cardStr)) {
                    throw new IllegalArgumentException("Duplicate card found: " + cardStr);
                }
                allCards.add(cardStr);
            }
        }

        for (Card card : communityCards) {
            String cardStr = card.toString();
            if (allCards.contains(cardStr)) {
                throw new IllegalArgumentException("Duplicate card found: " + cardStr);
            }
            allCards.add(cardStr);
        }
    }
}

