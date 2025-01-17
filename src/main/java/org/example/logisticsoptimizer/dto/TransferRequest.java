package org.example.logisticsoptimizer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class TransferRequest {
    @NotNull(message = "Maximum weight cannot be null.")
    @Positive(message = "Maximum weight must be positive.")
    private Integer maxWeight;

    @NotNull(message = "Available transfers cannot be null.")
    @Valid
    private List<Transfer> availableTransfers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Transfer {
        @NotNull(message = "Weight cannot be null.")
        @Positive(message = "Weight must be positive.")
        private Integer weight;

        @NotNull(message = "Cost cannot be null.")
        @Positive(message = "Cost must be positive.")
        private Integer cost;
    }
}
