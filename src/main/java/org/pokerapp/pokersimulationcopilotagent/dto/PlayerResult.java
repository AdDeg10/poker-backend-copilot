package org.pokerapp.pokersimulationcopilotagent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for individual player results
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerResult {
    private String name;
    private double winPercentage;
    private String bestHandRank; // Only included when 5 community cards provided
}

