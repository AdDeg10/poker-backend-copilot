package org.pokerapp.pokersimulationcopilotagent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for poker simulation request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationRequest {
    private List<PlayerRequest> players;
    private List<String> communityCards; // 0-5 cards in string format
    private int numberOfSimulations;

    public void validate() {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("At least one player is required");
        }
        if (players.size() < 2) {
            throw new IllegalArgumentException("At least 2 players are required for a poker game");
        }
        if (numberOfSimulations <= 0) {
            throw new IllegalArgumentException("Number of simulations must be positive");
        }
        if (numberOfSimulations > 1000000) {
            throw new IllegalArgumentException("Number of simulations cannot exceed 1,000,000");
        }
        if (communityCards != null && communityCards.size() > 5) {
            throw new IllegalArgumentException("Cannot have more than 5 community cards");
        }

        // Validate each player
        for (PlayerRequest player : players) {
            player.validate();
        }
    }
}

