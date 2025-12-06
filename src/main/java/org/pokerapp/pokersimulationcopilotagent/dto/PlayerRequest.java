package org.pokerapp.pokersimulationcopilotagent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for player data in simulation request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRequest {
    private String name;
    private List<String> holeCards; // Two cards in string format (e.g., ["AS", "KH"])

    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty");
        }
        if (holeCards == null || holeCards.size() != 2) {
            throw new IllegalArgumentException("Each player must have exactly 2 hole cards");
        }
    }
}

