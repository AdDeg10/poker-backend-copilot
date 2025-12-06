package org.pokerapp.pokersimulationcopilotagent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for simulation response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResponse {
    private List<PlayerResult> playerResults;
    private double tiePercentage;
    private int totalSimulations;
}

