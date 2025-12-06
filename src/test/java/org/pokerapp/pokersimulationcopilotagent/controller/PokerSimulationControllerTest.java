package org.pokerapp.pokersimulationcopilotagent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.pokerapp.pokersimulationcopilotagent.dto.PlayerRequest;
import org.pokerapp.pokersimulationcopilotagent.dto.SimulationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for PokerSimulationController
 */
@SpringBootTest
@AutoConfigureMockMvc
class PokerSimulationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSimulatePoker_ValidRequest() throws Exception {
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("AS", "AH"));
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("KD", "KC"));

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1, player2),
                Arrays.asList("AD", "AC", "KH"),
                100
        );

        mockMvc.perform(post("/api/poker/simulate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSimulations").value(100))
                .andExpect(jsonPath("$.playerResults").isArray())
                .andExpect(jsonPath("$.playerResults.length()").value(2))
                .andExpect(jsonPath("$.tiePercentage").isNumber());
    }

    @Test
    void testSimulatePoker_WithFullCommunityCards() throws Exception {
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("AS", "KS"));
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("2H", "3H"));

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1, player2),
                Arrays.asList("QS", "JS", "10S", "9S", "8S"),
                100
        );

        mockMvc.perform(post("/api/poker/simulate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerResults[0].bestHandRank").exists())
                .andExpect(jsonPath("$.playerResults[1].bestHandRank").exists());
    }

    @Test
    void testSimulatePoker_InvalidRequest_NoPlayers() throws Exception {
        SimulationRequest request = new SimulationRequest(
                Arrays.asList(),
                Arrays.asList("AS", "KS"),
                100
        );

        mockMvc.perform(post("/api/poker/simulate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testSimulatePoker_InvalidRequest_TooManyCommunityCards() throws Exception {
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("AS", "AH"));
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("KD", "KC"));

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1, player2),
                Arrays.asList("QS", "JS", "10S", "9S", "8S", "7S"), // 6 cards
                100
        );

        mockMvc.perform(post("/api/poker/simulate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testSimulatePoker_InvalidRequest_DuplicateCards() throws Exception {
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("AS", "AH"));
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("AS", "KC")); // Duplicate AS

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1, player2),
                null,
                100
        );

        mockMvc.perform(post("/api/poker/simulate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testSimulatePoker_InvalidRequest_InvalidCardFormat() throws Exception {
        PlayerRequest player1 = new PlayerRequest("Alice", Arrays.asList("XX", "YY")); // Invalid cards
        PlayerRequest player2 = new PlayerRequest("Bob", Arrays.asList("KD", "KC"));

        SimulationRequest request = new SimulationRequest(
                Arrays.asList(player1, player2),
                null,
                100
        );

        mockMvc.perform(post("/api/poker/simulate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}

