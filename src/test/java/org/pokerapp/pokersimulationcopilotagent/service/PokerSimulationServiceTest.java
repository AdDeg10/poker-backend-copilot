package org.pokerapp.pokersimulationcopilotagent.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pokerapp.pokersimulationcopilotagent.dto.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PokerSimulationService
 */
class PokerSimulationServiceTest {

    private PokerSimulationService pokerSimulationService;
    private HandEvaluatorService handEvaluatorService;

    @BeforeEach
    void setUp() {
        handEvaluatorService = new HandEvaluatorService();
        pokerSimulationService = new PokerSimulationService(handEvaluatorService);
    }

    @Test
    void testSimulation_WithAllCommunityCards() {
        // When all 5 community cards are provided, result should be deterministic
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("AS", "KS"));
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("2H", "3H"));

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1, player2),
                Arrays.asList("QS", "JS", "10S", "9H", "8H"),
                100
        );

        SimulationResponse response = pokerSimulationService.runSimulation(request);

        assertNotNull(response);
        assertEquals(100, response.getTotalSimulations());
        assertEquals(2, response.getPlayerResults().size());

        // Alice has royal flush, should win 100%
        PlayerResult aliceResult = response.getPlayerResults().stream()
                .filter(r -> r.getName().equals("Alice"))
                .findFirst()
                .orElse(null);

        assertNotNull(aliceResult);
        assertEquals(100.0, aliceResult.getWinPercentage(), 0.01);
        assertEquals("Royal Flush", aliceResult.getBestHandRank());

        // Bob should have 0% win rate
        PlayerResult bobResult = response.getPlayerResults().stream()
                .filter(r -> r.getName().equals("Bob"))
                .findFirst()
                .orElse(null);

        assertNotNull(bobResult);
        assertEquals(0.0, bobResult.getWinPercentage(), 0.01);
        assertNotNull(bobResult.getBestHandRank());
    }

    @Test
    void testSimulation_WithPartialCommunityCards() {
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("AS", "AH"));
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("KD", "KC"));

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1, player2),
                Arrays.asList("AD", "AC"), // Alice has 4 aces on board
                1000
        );

        SimulationResponse response = pokerSimulationService.runSimulation(request);

        assertNotNull(response);
        assertEquals(1000, response.getTotalSimulations());

        // Alice should win most/all hands with 4 aces
        PlayerResult aliceResult = response.getPlayerResults().stream()
                .filter(r -> r.getName().equals("Alice"))
                .findFirst()
                .orElse(null);

        assertNotNull(aliceResult);
        assertTrue(aliceResult.getWinPercentage() > 95.0,
                "Alice should win >95% with 4 aces, got: " + aliceResult.getWinPercentage());
    }

    @Test
    void testSimulation_WithNoCommunityCards() {
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("AS", "AH"));
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("2D", "3C"));

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1, player2),
                null,
                1000
        );

        SimulationResponse response = pokerSimulationService.runSimulation(request);

        assertNotNull(response);
        assertEquals(1000, response.getTotalSimulations());

        // Alice has pocket aces, should win most hands
        PlayerResult aliceResult = response.getPlayerResults().stream()
                .filter(r -> r.getName().equals("Alice"))
                .findFirst()
                .orElse(null);

        assertNotNull(aliceResult);
        assertTrue(aliceResult.getWinPercentage() > 70.0,
                "Alice should win >70% with pocket aces vs 2-3");
    }

    @Test
    void testSimulation_TieScenario() {
        // Both players have same cards (hole cards differ but board gives both same hand)
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("2H", "3H"));
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("2D", "3D"));

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1, player2),
                Arrays.asList("AS", "KS", "QS", "JS", "10S"), // Royal flush on board
                100
        );

        SimulationResponse response = pokerSimulationService.runSimulation(request);

        assertNotNull(response);
        // Both players use the board's royal flush, should be 100% tie
        assertEquals(100.0, response.getTiePercentage(), 0.01);
    }

    @Test
    void testSimulation_MultiplePlayersWithMixedResults() {
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("AS", "AH"));
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("KS", "KH"));
        PlayerRequest player3 = new PlayerRequest("Charlie", Arrays.asList("QS", "QH"));

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1, player2, player3),
                null,
                500
        );

        SimulationResponse response = pokerSimulationService.runSimulation(request);

        assertNotNull(response);
        assertEquals(3, response.getPlayerResults().size());

        // Total percentages should sum to ~100%
        double totalPercentage = response.getPlayerResults().stream()
                .mapToDouble(PlayerResult::getWinPercentage)
                .sum() + response.getTiePercentage();

        assertEquals(100.0, totalPercentage, 0.5);
    }

    @Test
    void testValidation_NoPlayers() {
        SimulationRequest request = new SimulationRequest(
                Arrays.asList(),
                Arrays.asList("AS", "KS"),
                100
        );

        assertThrows(IllegalArgumentException.class, () -> {
            pokerSimulationService.runSimulation(request);
        });
    }

    @Test
    void testValidation_OnlyOnePlayer() {
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("AS", "AH"));

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1),
                Arrays.asList("KS", "KH"),
                100
        );

        assertThrows(IllegalArgumentException.class, () -> {
            pokerSimulationService.runSimulation(request);
        });
    }

    @Test
    void testValidation_TooManyCommunityCards() {
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("AS", "AH"));
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("KS", "KH"));

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1, player2),
                Arrays.asList("QS", "QH", "JS", "JH", "10S", "10H"), // 6 cards
                100
        );

        assertThrows(IllegalArgumentException.class, () -> {
            pokerSimulationService.runSimulation(request);
        });
    }

    @Test
    void testValidation_DuplicateCards() {
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("AS", "AH"));
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("AS", "KH")); // Duplicate AS

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1, player2),
                null,
                100
        );

        assertThrows(IllegalArgumentException.class, () -> {
            pokerSimulationService.runSimulation(request);
        });
    }

    @Test
    void testValidation_InvalidNumberOfSimulations() {
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("AS", "AH"));
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("KS", "KH"));

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1, player2),
                null,
                0 // Invalid
        );

        assertThrows(IllegalArgumentException.class, () -> {
            pokerSimulationService.runSimulation(request);
        });
    }

    @Test
    void testValidation_PlayerWithInvalidHoleCards() {
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("AS")); // Only 1 card
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("KS", "KH"));

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1, player2),
                null,
                100
        );

        assertThrows(IllegalArgumentException.class, () -> {
            pokerSimulationService.runSimulation(request);
        });
    }

    @Test
    void testBestHandRankOnlyIncludedWith5CommunityCards() {
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("AS", "KS"));
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("2H", "3H"));

        // Test with partial community cards
        SimulationRequest requestPartial = new SimulationRequest(
                Arrays.asList(player1, player2),
                Arrays.asList("QS", "JS", "10S"), // Only 3 cards
                100
        );

        SimulationResponse responsePartial = pokerSimulationService.runSimulation(requestPartial);
        for (PlayerResult result : responsePartial.getPlayerResults()) {
            assertNull(result.getBestHandRank(), "Best hand rank should be null with <5 community cards");
        }

        // Test with full community cards
        SimulationRequest requestFull = new SimulationRequest(
                Arrays.asList(player1, player2),
                Arrays.asList("QS", "JS", "10S", "9H", "8H"), // 5 cards
                100
        );

        SimulationResponse responseFull = pokerSimulationService.runSimulation(requestFull);
        for (PlayerResult result : responseFull.getPlayerResults()) {
            assertNotNull(result.getBestHandRank(), "Best hand rank should be included with 5 community cards");
        }
    }
}

