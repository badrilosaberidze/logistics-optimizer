package org.example.logisticsoptimizer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class TransferResponse {
    private List<SelectedTransfer> selectedTransfers;
    private int totalCost;
    private int totalWeight;

    public TransferResponse(){}

    public TransferResponse(List<SelectedTransfer> selectedTransfers, int totalCost, int totalWeight) {
        this.selectedTransfers = selectedTransfers;
        this.totalCost = totalCost;
        this.totalWeight = totalWeight;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectedTransfer {
        private int weight;
        private int cost;
    }
}
