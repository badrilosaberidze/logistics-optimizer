package org.example.logisticsoptimizer.dto;

import lombok.Data;

import java.util.List;

@Data
public class KnapsackResult {
    private final List<TransferRequest.Transfer> selectedTransfers;
    private final int totalCost;
}