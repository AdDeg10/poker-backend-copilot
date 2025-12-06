package org.pokerapp.pokersimulationcopilotagent.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pokerapp.pokersimulationcopilotagent.model.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HandEvaluatorService
 */
class HandEvaluatorServiceTest {

    private HandEvaluatorService handEvaluatorService;

    @BeforeEach
    void setUp() {
        handEvaluatorService = new HandEvaluatorService();
    }

    @Test
    void testRoyalFlush() {
        List<Card> holeCards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS)
        );
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.TEN, Suit.HEARTS),
                new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.THREE, Suit.DIAMONDS)
        );

        HandEvaluation evaluation = handEvaluatorService.evaluateHand(holeCards, communityCards);
        assertEquals(HandRank.ROYAL_FLUSH, evaluation.getHandRank());
    }

    @Test
    void testStraightFlush() {
        List<Card> holeCards = Arrays.asList(
                new Card(Rank.NINE, Suit.SPADES),
                new Card(Rank.EIGHT, Suit.SPADES)
        );
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.SEVEN, Suit.SPADES),
                new Card(Rank.SIX, Suit.SPADES),
                new Card(Rank.FIVE, Suit.SPADES),
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS)
        );

        HandEvaluation evaluation = handEvaluatorService.evaluateHand(holeCards, communityCards);
        assertEquals(HandRank.STRAIGHT_FLUSH, evaluation.getHandRank());
    }

    @Test
    void testFourOfAKind() {
        List<Card> holeCards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.ACE, Suit.DIAMONDS)
        );
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.THREE, Suit.DIAMONDS)
        );

        HandEvaluation evaluation = handEvaluatorService.evaluateHand(holeCards, communityCards);
        assertEquals(HandRank.FOUR_OF_A_KIND, evaluation.getHandRank());
    }

    @Test
    void testFullHouse() {
        List<Card> holeCards = Arrays.asList(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS)
        );
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.KING, Suit.CLUBS),
                new Card(Rank.QUEEN, Suit.SPADES),
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.THREE, Suit.DIAMONDS)
        );

        HandEvaluation evaluation = handEvaluatorService.evaluateHand(holeCards, communityCards);
        assertEquals(HandRank.FULL_HOUSE, evaluation.getHandRank());
    }

    @Test
    void testFlush() {
        List<Card> holeCards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS)
        );
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.NINE, Suit.HEARTS),
                new Card(Rank.FIVE, Suit.HEARTS),
                new Card(Rank.THREE, Suit.HEARTS),
                new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.SEVEN, Suit.DIAMONDS)
        );

        HandEvaluation evaluation = handEvaluatorService.evaluateHand(holeCards, communityCards);
        assertEquals(HandRank.FLUSH, evaluation.getHandRank());
    }

    @Test
    void testStraight() {
        List<Card> holeCards = Arrays.asList(
                new Card(Rank.NINE, Suit.HEARTS),
                new Card(Rank.EIGHT, Suit.DIAMONDS)
        );
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.SEVEN, Suit.CLUBS),
                new Card(Rank.SIX, Suit.SPADES),
                new Card(Rank.FIVE, Suit.HEARTS),
                new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.KING, Suit.DIAMONDS)
        );

        HandEvaluation evaluation = handEvaluatorService.evaluateHand(holeCards, communityCards);
        assertEquals(HandRank.STRAIGHT, evaluation.getHandRank());
    }

    @Test
    void testWheelStraight() {
        // A-2-3-4-5 straight (wheel)
        List<Card> holeCards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.TWO, Suit.DIAMONDS)
        );
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.THREE, Suit.CLUBS),
                new Card(Rank.FOUR, Suit.SPADES),
                new Card(Rank.FIVE, Suit.HEARTS),
                new Card(Rank.KING, Suit.CLUBS),
                new Card(Rank.QUEEN, Suit.DIAMONDS)
        );

        HandEvaluation evaluation = handEvaluatorService.evaluateHand(holeCards, communityCards);
        assertEquals(HandRank.STRAIGHT, evaluation.getHandRank());
    }

    @Test
    void testThreeOfAKind() {
        List<Card> holeCards = Arrays.asList(
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.JACK, Suit.DIAMONDS)
        );
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.JACK, Suit.CLUBS),
                new Card(Rank.NINE, Suit.SPADES),
                new Card(Rank.SEVEN, Suit.HEARTS),
                new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.THREE, Suit.DIAMONDS)
        );

        HandEvaluation evaluation = handEvaluatorService.evaluateHand(holeCards, communityCards);
        assertEquals(HandRank.THREE_OF_A_KIND, evaluation.getHandRank());
    }

    @Test
    void testTwoPair() {
        List<Card> holeCards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.ACE, Suit.DIAMONDS)
        );
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.KING, Suit.CLUBS),
                new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.THREE, Suit.DIAMONDS)
        );

        HandEvaluation evaluation = handEvaluatorService.evaluateHand(holeCards, communityCards);
        assertEquals(HandRank.TWO_PAIR, evaluation.getHandRank());
    }

    @Test
    void testOnePair() {
        List<Card> holeCards = Arrays.asList(
                new Card(Rank.TEN, Suit.HEARTS),
                new Card(Rank.TEN, Suit.DIAMONDS)
        );
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.THREE, Suit.DIAMONDS)
        );

        HandEvaluation evaluation = handEvaluatorService.evaluateHand(holeCards, communityCards);
        assertEquals(HandRank.ONE_PAIR, evaluation.getHandRank());
    }

    @Test
    void testHighCard() {
        List<Card> holeCards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS)
        );
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.QUEEN, Suit.CLUBS),
                new Card(Rank.JACK, Suit.SPADES),
                new Card(Rank.NINE, Suit.HEARTS),
                new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.THREE, Suit.DIAMONDS)
        );

        HandEvaluation evaluation = handEvaluatorService.evaluateHand(holeCards, communityCards);
        assertEquals(HandRank.HIGH_CARD, evaluation.getHandRank());
    }

    @Test
    void testHandComparison_HigherRankWins() {
        HandEvaluation flush = new HandEvaluation(HandRank.FLUSH, Arrays.asList(14, 13, 11, 9, 7));
        HandEvaluation straight = new HandEvaluation(HandRank.STRAIGHT, Arrays.asList(9));

        assertTrue(flush.compareTo(straight) > 0);
        assertTrue(straight.compareTo(flush) < 0);
    }

    @Test
    void testHandComparison_SameRankDifferentKickers() {
        HandEvaluation pair1 = new HandEvaluation(HandRank.ONE_PAIR, Arrays.asList(14, 13, 11, 9));
        HandEvaluation pair2 = new HandEvaluation(HandRank.ONE_PAIR, Arrays.asList(14, 13, 11, 8));

        assertTrue(pair1.compareTo(pair2) > 0);
    }

    @Test
    void testHandComparison_ExactTie() {
        HandEvaluation hand1 = new HandEvaluation(HandRank.TWO_PAIR, Arrays.asList(14, 13, 11));
        HandEvaluation hand2 = new HandEvaluation(HandRank.TWO_PAIR, Arrays.asList(14, 13, 11));

        assertEquals(0, hand1.compareTo(hand2));
    }
}

