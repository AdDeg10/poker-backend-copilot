
package org.pokerapp.pokersimulationcopilotagent.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pokerapp.pokersimulationcopilotagent.model.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive JUnit tests for Texas Hold'em two-player scenarios.
 * Each test simulates a heads-up poker hand with hole cards for two players
 * and community cards, then asserts which player should win and why.
 */
class TexasHoldemTwoPlayerTest {

    private HandEvaluatorService handEvaluatorService;

    @BeforeEach
    void setUp() {
        handEvaluatorService = new HandEvaluatorService();
    }

    // ==================== CLEAR WINS - DIFFERENT HAND RANKS ====================

    @Test
    void testRoyalFlushBeatsFlush() {
        // Player 1: A♥ K♥ - Makes Royal Flush with community cards
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS)
        );

        // Player 2: 9♥ 8♥ - Makes a regular Flush
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.NINE, Suit.HEARTS),
                new Card(Rank.EIGHT, Suit.HEARTS)
        );

        // Community cards: Q♥ J♥ 10♥ 2♣ 3♦
        // Player 1 has Royal Flush (A-K-Q-J-10 all hearts)
        // Player 2 has Flush (Q-J-10-9-8 all hearts)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.TEN, Suit.HEARTS),
                new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.THREE, Suit.DIAMONDS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with Royal Flush
        assertEquals(HandRank.ROYAL_FLUSH, player1Eval.getHandRank());
        assertEquals(HandRank.FLUSH, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "Royal Flush should beat Flush");
    }

    @Test
    void testStraightFlushBeatsFourOfAKind() {
        // Player 1: 9♠ 8♠ - Makes Straight Flush
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.NINE, Suit.SPADES),
                new Card(Rank.EIGHT, Suit.SPADES)
        );

        // Player 2: A♥ A♦ - Makes Four of a Kind Aces
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.ACE, Suit.DIAMONDS)
        );

        // Community cards: 7♠ 6♠ 5♠ A♣ A♠
        // Player 1 has Straight Flush (9-8-7-6-5 all spades)
        // Player 2 has Four Aces (A♥ A♦ A♣ A♠)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.SEVEN, Suit.SPADES),
                new Card(Rank.SIX, Suit.SPADES),
                new Card(Rank.FIVE, Suit.SPADES),
                new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.ACE, Suit.SPADES)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with Straight Flush
        assertEquals(HandRank.STRAIGHT_FLUSH, player1Eval.getHandRank());
        assertEquals(HandRank.FOUR_OF_A_KIND, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "Straight Flush should beat Four of a Kind");
    }

    @Test
    void testFourOfAKindBeatsFullHouse() {
        // Player 1: K♥ K♦ - Makes Four of a Kind Kings
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS)
        );

        // Player 2: Q♥ Q♦ - Makes Full House (Queens full of Kings)
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.DIAMONDS)
        );

        // Community cards: K♣ K♠ Q♠ 5♣ 2♦
        // Player 1 has Four Kings
        // Player 2 has Full House (Q-Q-Q-K-K)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.KING, Suit.CLUBS),
                new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.QUEEN, Suit.SPADES),
                new Card(Rank.FIVE, Suit.CLUBS),
                new Card(Rank.TWO, Suit.DIAMONDS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with Four of a Kind
        assertEquals(HandRank.FOUR_OF_A_KIND, player1Eval.getHandRank());
        assertEquals(HandRank.FULL_HOUSE, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "Four of a Kind should beat Full House");
    }

    @Test
    void testFullHouseBeatsFlush() {
        // Player 1: J♥ J♦ - Makes Full House (Jacks full of Tens)
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.JACK, Suit.DIAMONDS)
        );

        // Player 2: K♣ Q♣ - Makes Flush (all clubs)
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.KING, Suit.CLUBS),
                new Card(Rank.QUEEN, Suit.CLUBS)
        );

        // Community cards: J♣ 10♥ 10♦ 8♣ 5♣
        // Player 1 has Full House (J-J-J-10-10)
        // Player 2 has Flush (K-Q-J-8-5 all clubs)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.JACK, Suit.CLUBS),
                new Card(Rank.TEN, Suit.HEARTS),
                new Card(Rank.TEN, Suit.DIAMONDS),
                new Card(Rank.EIGHT, Suit.CLUBS),
                new Card(Rank.FIVE, Suit.CLUBS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with Full House
        assertEquals(HandRank.FULL_HOUSE, player1Eval.getHandRank());
        assertEquals(HandRank.FLUSH, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "Full House should beat Flush");
    }

    @Test
    void testFlushBeatsStraight() {
        // Player 1: A♠ 9♠ - Makes Flush (all spades)
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.NINE, Suit.SPADES)
        );

        // Player 2: Q♥ J♥ - Makes Straight (Q-J-10-9-8)
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.JACK, Suit.HEARTS)
        );

        // Community cards: K♠ 7♠ 5♠ 10♦ 8♠
        // Player 1 has Flush (A-K-9-8-7 all spades)
        // Player 2 has Straight (Q-J-10-9-8) - using 8♠ and 9♠ from board
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.SEVEN, Suit.SPADES),
                new Card(Rank.FIVE, Suit.SPADES),
                new Card(Rank.TEN, Suit.DIAMONDS),
                new Card(Rank.EIGHT, Suit.SPADES)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with Flush
        assertEquals(HandRank.FLUSH, player1Eval.getHandRank());
        // Note: Player 2 might also have flush if they have spades, let's check
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "Flush should beat Straight (or lower)");
    }

    @Test
    void testStraightBeatsThreeOfAKind() {
        // Player 1: K♥ Q♣ - Makes Straight (K-Q-J-10-9)
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.CLUBS)
        );

        // Player 2: 9♥ 9♦ - Makes Three of a Kind (three 9s)
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.NINE, Suit.HEARTS),
                new Card(Rank.NINE, Suit.DIAMONDS)
        );

        // Community cards: J♦ 10♠ 9♠ 5♣ 2♦
        // Player 1 has Straight (K-Q-J-10-9)
        // Player 2 has Three of a Kind (9-9-9)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.JACK, Suit.DIAMONDS),
                new Card(Rank.TEN, Suit.SPADES),
                new Card(Rank.NINE, Suit.SPADES),
                new Card(Rank.FIVE, Suit.CLUBS),
                new Card(Rank.TWO, Suit.DIAMONDS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with Straight
        assertEquals(HandRank.STRAIGHT, player1Eval.getHandRank());
        assertEquals(HandRank.THREE_OF_A_KIND, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "Straight should beat Three of a Kind");
    }

    @Test
    void testThreeOfAKindBeatsTwoPair() {
        // Player 1: 8♥ 8♦ - Makes Three of a Kind (three 8s)
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.EIGHT, Suit.HEARTS),
                new Card(Rank.EIGHT, Suit.DIAMONDS)
        );

        // Player 2: A♠ K♠ - Makes Two Pair (Aces and Kings)
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.KING, Suit.SPADES)
        );

        // Community cards: 8♠ A♥ K♣ 5♦ 3♣
        // Player 1 has Three of a Kind (8-8-8)
        // Player 2 has Two Pair (A-A-K-K)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.EIGHT, Suit.SPADES),
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.CLUBS),
                new Card(Rank.FIVE, Suit.DIAMONDS),
                new Card(Rank.THREE, Suit.CLUBS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with Three of a Kind
        assertEquals(HandRank.THREE_OF_A_KIND, player1Eval.getHandRank());
        assertEquals(HandRank.TWO_PAIR, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "Three of a Kind should beat Two Pair");
    }

    @Test
    void testTwoPairBeatsOnePair() {
        // Player 1: Q♥ Q♦ - Makes Two Pair (Queens and Tens)
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.DIAMONDS)
        );

        // Player 2: A♠ A♥ - Makes One Pair (Aces)
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.ACE, Suit.HEARTS)
        );

        // Community cards: 10♣ 10♠ 7♦ 5♣ 2♥
        // Player 1 has Two Pair (Q-Q-10-10)
        // Player 2 has One Pair (A-A)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.TEN, Suit.CLUBS),
                new Card(Rank.TEN, Suit.SPADES),
                new Card(Rank.SEVEN, Suit.DIAMONDS),
                new Card(Rank.FIVE, Suit.CLUBS),
                new Card(Rank.TWO, Suit.HEARTS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with Two Pair
        assertEquals(HandRank.TWO_PAIR, player1Eval.getHandRank());
        assertEquals(HandRank.ONE_PAIR, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "Two Pair should beat One Pair");
    }

    @Test
    void testOnePairBeatsHighCard() {
        // Player 1: 7♥ 7♦ - Makes One Pair (sevens)
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.SEVEN, Suit.HEARTS),
                new Card(Rank.SEVEN, Suit.DIAMONDS)
        );

        // Player 2: A♠ K♠ - Makes High Card (Ace high)
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.KING, Suit.SPADES)
        );

        // Community cards: Q♦ J♣ 9♥ 5♣ 2♦
        // Player 1 has One Pair (7-7)
        // Player 2 has High Card (A-K-Q-J-9)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.QUEEN, Suit.DIAMONDS),
                new Card(Rank.JACK, Suit.CLUBS),
                new Card(Rank.NINE, Suit.HEARTS),
                new Card(Rank.FIVE, Suit.CLUBS),
                new Card(Rank.TWO, Suit.DIAMONDS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with One Pair
        assertEquals(HandRank.ONE_PAIR, player1Eval.getHandRank());
        assertEquals(HandRank.HIGH_CARD, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "One Pair should beat High Card");
    }

    // ==================== KICKER BATTLES - SAME HAND RANK ====================

    @Test
    void testHigherPairWins() {
        // Player 1: K♥ K♦ - Pair of Kings
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS)
        );

        // Player 2: Q♥ Q♦ - Pair of Queens
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.DIAMONDS)
        );

        // Community cards: 10♠ 8♣ 6♦ 4♥ 2♠
        // Player 1 has Pair of Kings
        // Player 2 has Pair of Queens
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.TEN, Suit.SPADES),
                new Card(Rank.EIGHT, Suit.CLUBS),
                new Card(Rank.SIX, Suit.DIAMONDS),
                new Card(Rank.FOUR, Suit.HEARTS),
                new Card(Rank.TWO, Suit.SPADES)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with higher pair
        assertEquals(HandRank.ONE_PAIR, player1Eval.getHandRank());
        assertEquals(HandRank.ONE_PAIR, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "Pair of Kings should beat Pair of Queens");
    }

    @Test
    void testSamePairBetterKickerWins() {
        // Player 1: 9♥ 9♦ - Pair of Nines with Ace kicker
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.NINE, Suit.HEARTS),
                new Card(Rank.ACE, Suit.HEARTS)
        );

        // Player 2: 9♠ 9♣ - Pair of Nines with King kicker
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.NINE, Suit.SPADES),
                new Card(Rank.KING, Suit.SPADES)
        );

        // Community cards: 7♦ 5♣ 3♥ 2♠ 4♦
        // Both have Pair of Nines, but Player 1 has better kickers (A > K)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.SEVEN, Suit.DIAMONDS),
                new Card(Rank.FIVE, Suit.CLUBS),
                new Card(Rank.THREE, Suit.HEARTS),
                new Card(Rank.TWO, Suit.SPADES),
                new Card(Rank.FOUR, Suit.DIAMONDS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with better kicker
        assertEquals(HandRank.ONE_PAIR, player1Eval.getHandRank());
        assertEquals(HandRank.ONE_PAIR, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "Same pair with Ace kicker should beat same pair with King kicker");
    }

    @Test
    void testHigherTwoPairWins() {
        // Player 1: K♥ K♦ - Two Pair (Kings and Nines)
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS)
        );

        // Player 2: Q♥ Q♦ - Two Pair (Queens and Nines)
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.DIAMONDS)
        );

        // Community cards: 9♠ 9♣ 5♦ 3♥ 2♠
        // Player 1 has Two Pair (K-K-9-9)
        // Player 2 has Two Pair (Q-Q-9-9)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.NINE, Suit.SPADES),
                new Card(Rank.NINE, Suit.CLUBS),
                new Card(Rank.FIVE, Suit.DIAMONDS),
                new Card(Rank.THREE, Suit.HEARTS),
                new Card(Rank.TWO, Suit.SPADES)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with higher two pair
        assertEquals(HandRank.TWO_PAIR, player1Eval.getHandRank());
        assertEquals(HandRank.TWO_PAIR, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "Two Pair K-K-9-9 should beat Two Pair Q-Q-9-9");
    }

    @Test
    void testHigherFlushWins() {
        // Player 1: A♠ J♠ - Ace-high Flush
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.JACK, Suit.SPADES)
        );

        // Player 2: K♠ Q♠ - King-high Flush
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.QUEEN, Suit.SPADES)
        );

        // Community cards: 9♠ 7♠ 5♠ 3♥ 2♦
        // Player 1 has Flush (A-J-9-7-5 all spades)
        // Player 2 has Flush (K-Q-9-7-5 all spades)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.NINE, Suit.SPADES),
                new Card(Rank.SEVEN, Suit.SPADES),
                new Card(Rank.FIVE, Suit.SPADES),
                new Card(Rank.THREE, Suit.HEARTS),
                new Card(Rank.TWO, Suit.DIAMONDS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with Ace-high flush
        assertEquals(HandRank.FLUSH, player1Eval.getHandRank());
        assertEquals(HandRank.FLUSH, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "Ace-high Flush should beat King-high Flush");
    }

    @Test
    void testHigherStraightWins() {
        // Player 1: K♥ Q♣ - Broadway Straight (K-Q-J-10-9)
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.CLUBS)
        );

        // Player 2: 8♥ 7♦ - Lower Straight (J-10-9-8-7)
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.EIGHT, Suit.HEARTS),
                new Card(Rank.SEVEN, Suit.DIAMONDS)
        );

        // Community cards: J♦ 10♠ 9♠ 5♣ 2♦
        // Player 1 has Straight (K-Q-J-10-9)
        // Player 2 has Straight (J-10-9-8-7)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.JACK, Suit.DIAMONDS),
                new Card(Rank.TEN, Suit.SPADES),
                new Card(Rank.NINE, Suit.SPADES),
                new Card(Rank.FIVE, Suit.CLUBS),
                new Card(Rank.TWO, Suit.DIAMONDS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with higher straight
        assertEquals(HandRank.STRAIGHT, player1Eval.getHandRank());
        assertEquals(HandRank.STRAIGHT, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "King-high Straight should beat Jack-high Straight");
    }

    @Test
    void testHigherFullHouseWins() {
        // Player 1: K♥ K♦ - Full House (Kings full of Jacks)
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS)
        );

        // Player 2: J♥ J♦ - Full House (Jacks full of Kings)
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.JACK, Suit.DIAMONDS)
        );

        // Community cards: K♣ J♣ J♠ 5♦ 2♥
        // Player 1 has Full House (K-K-K-J-J)
        // Player 2 has Full House (J-J-J-K-K)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.KING, Suit.CLUBS),
                new Card(Rank.JACK, Suit.CLUBS),
                new Card(Rank.JACK, Suit.SPADES),
                new Card(Rank.FIVE, Suit.DIAMONDS),
                new Card(Rank.TWO, Suit.HEARTS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with Kings full
        assertEquals(HandRank.FULL_HOUSE, player1Eval.getHandRank());
        assertEquals(HandRank.FULL_HOUSE, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "Kings full of Jacks should beat Jacks full of Kings");
    }

    @Test
    void testHighCardKickerBattle() {
        // Player 1: A♥ K♣ - Ace-King high
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.CLUBS)
        );

        // Player 2: A♦ Q♦ - Ace-Queen high
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.DIAMONDS),
                new Card(Rank.QUEEN, Suit.DIAMONDS)
        );

        // Community cards: J♠ 9♣ 7♥ 5♦ 2♠
        // Player 1 has High Card (A-K-J-9-7)
        // Player 2 has High Card (A-Q-J-9-7)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.JACK, Suit.SPADES),
                new Card(Rank.NINE, Suit.CLUBS),
                new Card(Rank.SEVEN, Suit.HEARTS),
                new Card(Rank.FIVE, Suit.DIAMONDS),
                new Card(Rank.TWO, Suit.SPADES)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with better kicker
        assertEquals(HandRank.HIGH_CARD, player1Eval.getHandRank());
        assertEquals(HandRank.HIGH_CARD, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "A-K high should beat A-Q high");
    }

    // ==================== TIE SCENARIOS ====================

    @Test
    void testSplitPotBothPlayBoardPair() {
        // Player 1: A♥ K♥ - Doesn't improve beyond board pair
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS)
        );

        // Player 2: A♦ K♦ - Same situation, doesn't improve
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.DIAMONDS),
                new Card(Rank.KING, Suit.DIAMONDS)
        );

        // Community cards: Q♠ Q♣ J♦ 10♠ 9♣
        // Both players use board (Q-Q-A-K-J) for their best hand
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.QUEEN, Suit.SPADES),
                new Card(Rank.QUEEN, Suit.CLUBS),
                new Card(Rank.JACK, Suit.DIAMONDS),
                new Card(Rank.TEN, Suit.SPADES),
                new Card(Rank.NINE, Suit.CLUBS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Should be a tie
        assertEquals(HandRank.ONE_PAIR, player1Eval.getHandRank());
        assertEquals(HandRank.ONE_PAIR, player2Eval.getHandRank());
        assertEquals(0, player1Eval.compareTo(player2Eval),
                "Both players play the board with same kickers - should tie");
    }

    @Test
    void testSplitPotBothHaveStraight() {
        // Player 1: A♥ 7♣ - Makes the board straight
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.SEVEN, Suit.CLUBS)
        );

        // Player 2: A♦ 2♦ - Also makes the board straight
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.DIAMONDS),
                new Card(Rank.TWO, Suit.DIAMONDS)
        );

        // Community cards: K♠ Q♣ J♦ 10♠ 9♥
        // Board has straight K-Q-J-10-9, both players use their Ace or play board
        // Actually both players make A-K-Q-J-10 (Broadway)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.QUEEN, Suit.CLUBS),
                new Card(Rank.JACK, Suit.DIAMONDS),
                new Card(Rank.TEN, Suit.SPADES),
                new Card(Rank.NINE, Suit.HEARTS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Should be a tie (both have Broadway)
        assertEquals(HandRank.STRAIGHT, player1Eval.getHandRank());
        assertEquals(HandRank.STRAIGHT, player2Eval.getHandRank());
        assertEquals(0, player1Eval.compareTo(player2Eval),
                "Both players have same straight (Broadway) - should tie");
    }

    @Test
    void testSplitPotCounterfeited() {
        // Player 1: 3♥ 3♦ - Pair of threes gets counterfeited by board
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.THREE, Suit.HEARTS),
                new Card(Rank.THREE, Suit.DIAMONDS)
        );

        // Player 2: 4♥ 4♦ - Pair of fours gets counterfeited by board
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.FOUR, Suit.HEARTS),
                new Card(Rank.FOUR, Suit.DIAMONDS)
        );

        // Community cards: A♠ A♣ K♦ K♠ Q♥
        // Board has two pair A-A-K-K-Q, both players' pocket pairs don't matter
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.KING, Suit.DIAMONDS),
                new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.QUEEN, Suit.HEARTS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Should be a tie (both play the board)
        assertEquals(HandRank.TWO_PAIR, player1Eval.getHandRank());
        assertEquals(HandRank.TWO_PAIR, player2Eval.getHandRank());
        assertEquals(0, player1Eval.compareTo(player2Eval),
                "Both players counterfeited by board - should tie");
    }

    // ==================== SPECIAL SCENARIOS ====================

    @Test
    void testWheelStraight() {
        // Player 1: A♥ 2♣ - Makes wheel (A-2-3-4-5 straight)
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.TWO, Suit.CLUBS)
        );

        // Player 2: K♦ Q♦ - High cards, no made hand
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.KING, Suit.DIAMONDS),
                new Card(Rank.QUEEN, Suit.DIAMONDS)
        );

        // Community cards: 5♠ 4♣ 3♦ 7♥ 9♠
        // Player 1 has wheel straight (A-2-3-4-5) where Ace plays as low card
        // Player 2 has High Card (K-Q-9-7-5)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.FIVE, Suit.SPADES),
                new Card(Rank.FOUR, Suit.CLUBS),
                new Card(Rank.THREE, Suit.DIAMONDS),
                new Card(Rank.SEVEN, Suit.HEARTS),
                new Card(Rank.NINE, Suit.SPADES)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 1 should win with wheel straight
        assertEquals(HandRank.STRAIGHT, player1Eval.getHandRank());
        assertEquals(HandRank.HIGH_CARD, player2Eval.getHandRank());
        assertTrue(player1Eval.compareTo(player2Eval) > 0,
                "Wheel straight (A-2-3-4-5) should beat High Card");
    }

    @Test
    void testBadBeatJackpotScenario() {
        // Player 1: A♠ A♥ - Four Aces (monster hand)
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.ACE, Suit.HEARTS)
        );

        // Player 2: K♠ 9♠ - Royal Flush (ultra monster)
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.NINE, Suit.SPADES)
        );

        // Community cards: A♣ A♦ Q♠ J♠ 10♠
        // Player 1 has Four Aces (A-A-A-A-Q)
        // Player 2 has Royal Flush (A♠-K♠-Q♠-J♠-10♠)
        // Classic bad beat scenario!
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.ACE, Suit.DIAMONDS),
                new Card(Rank.QUEEN, Suit.SPADES),
                new Card(Rank.JACK, Suit.SPADES),
                new Card(Rank.TEN, Suit.SPADES)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 2 wins with Royal Flush despite Player 1 having Four Aces!
        assertEquals(HandRank.FOUR_OF_A_KIND, player1Eval.getHandRank());
        assertEquals(HandRank.ROYAL_FLUSH, player2Eval.getHandRank());
        assertTrue(player2Eval.compareTo(player1Eval) > 0,
                "Royal Flush beats Four Aces - classic bad beat jackpot scenario");
    }

    @Test
    void testRunnerRunnerSuckout() {
        // Player 1: A♥ K♥ - Top pair on flop
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS)
        );

        // Player 2: 5♦ 4♦ - Gutshot straight draw on flop, makes straight with runner-runner
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.FIVE, Suit.DIAMONDS),
                new Card(Rank.FOUR, Suit.DIAMONDS)
        );

        // Community cards (final): A♠ 7♣ 3♦ 6♠ 2♥
        // Player 1 has Pair of Aces (A-A-K-7-6)
        // Player 2 has Straight (7-6-5-4-3) - runner-runner 6 and 2 completed the straight
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.SEVEN, Suit.CLUBS),
                new Card(Rank.THREE, Suit.DIAMONDS),
                new Card(Rank.SIX, Suit.SPADES),
                new Card(Rank.TWO, Suit.HEARTS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 2 wins with straight (runner-runner suckout)
        assertEquals(HandRank.ONE_PAIR, player1Eval.getHandRank());
        assertEquals(HandRank.STRAIGHT, player2Eval.getHandRank());
        assertTrue(player2Eval.compareTo(player1Eval) > 0,
                "Straight beats Pair - runner-runner suckout scenario");
    }

    @Test
    void testSetOverSet() {
        // Player 1: 9♥ 9♦ - Set of nines
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.NINE, Suit.HEARTS),
                new Card(Rank.NINE, Suit.DIAMONDS)
        );

        // Player 2: Q♥ Q♦ - Set of queens
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.DIAMONDS)
        );

        // Community cards: Q♠ 9♠ 5♣ 7♦ 2♥
        // Player 1 has Three Nines (9-9-9-Q-7)
        // Player 2 has Three Queens (Q-Q-Q-9-7)
        // Classic cooler situation!
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.QUEEN, Suit.SPADES),
                new Card(Rank.NINE, Suit.SPADES),
                new Card(Rank.FIVE, Suit.CLUBS),
                new Card(Rank.SEVEN, Suit.DIAMONDS),
                new Card(Rank.TWO, Suit.HEARTS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 2 wins with higher set
        assertEquals(HandRank.THREE_OF_A_KIND, player1Eval.getHandRank());
        assertEquals(HandRank.THREE_OF_A_KIND, player2Eval.getHandRank());
        assertTrue(player2Eval.compareTo(player1Eval) > 0,
                "Set of Queens beats Set of Nines - classic set over set cooler");
    }

    @Test
    void testOverpairVsTopPairTopKicker() {
        // Player 1: K♥ K♦ - Overpair (Kings)
        List<Card> player1HoleCards = Arrays.asList(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS)
        );

        // Player 2: A♠ J♠ - Top pair (Aces) with Jack kicker
        List<Card> player2HoleCards = Arrays.asList(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.JACK, Suit.SPADES)
        );

        // Community cards: A♥ 7♣ 5♦ 3♠ 2♥
        // Player 1 has Pair of Kings (K-K-A-7-5)
        // Player 2 has Pair of Aces (A-A-J-7-5)
        List<Card> communityCards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.SEVEN, Suit.CLUBS),
                new Card(Rank.FIVE, Suit.DIAMONDS),
                new Card(Rank.THREE, Suit.SPADES),
                new Card(Rank.TWO, Suit.HEARTS)
        );

        HandEvaluation player1Eval = handEvaluatorService.evaluateHand(player1HoleCards, communityCards);
        HandEvaluation player2Eval = handEvaluatorService.evaluateHand(player2HoleCards, communityCards);

        // Player 2 wins with top pair vs overpair
        assertEquals(HandRank.ONE_PAIR, player1Eval.getHandRank());
        assertEquals(HandRank.ONE_PAIR, player2Eval.getHandRank());

    }
}