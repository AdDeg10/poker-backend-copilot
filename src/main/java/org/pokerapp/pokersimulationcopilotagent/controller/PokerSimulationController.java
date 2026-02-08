package org.pokerapp.pokersimulationcopilotagent.controller;

import org.pokerapp.pokersimulationcopilotagent.dto.SimulationRequest;
import org.pokerapp.pokersimulationcopilotagent.dto.SimulationResponse;
import org.pokerapp.pokersimulationcopilotagent.service.PokerSimulationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for poker simulation endpoints
 */
@RestController
@RequestMapping("/api/poker")
@CrossOrigin(origins = "http://localhost:4200")
public class PokerSimulationController {

    private final PokerSimulationService pokerSimulationService;

    public PokerSimulationController(PokerSimulationService pokerSimulationService) {
        this.pokerSimulationService = pokerSimulationService;
    }

    /**
     * Endpoint to simulate poker hands
     * POST /api/poker/simulate
     */
    @PostMapping("/simulate")
    public ResponseEntity<?> simulatePoker(@RequestBody SimulationRequest request) {
        try {
            SimulationResponse response = pokerSimulationService.runSimulation(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred: " + e.getMessage()));
        }
    }

    /**
     * Simple error response class
     */
    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}

