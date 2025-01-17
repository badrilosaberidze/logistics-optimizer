package org.example.logisticsoptimizer.service;

import org.example.logisticsoptimizer.dto.KnapsackResult;
import org.example.logisticsoptimizer.dto.TransferRequest;

import java.util.ArrayList;
import java.util.List;

public class KnapsackSolver {
    public static KnapsackResult solve(List<TransferRequest.Transfer> transfers, int maxWeight) {
        int n = transfers.size();
        int[][] dp = new int[n + 1][maxWeight + 1];

        for (int i = 1; i <= n; i++) {
            int weight = transfers.get(i - 1).getWeight();
            int cost = transfers.get(i - 1).getCost();
            for (int w = 0; w <= maxWeight; w++) {
                if (weight <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - weight] + cost);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        List<TransferRequest.Transfer> selectedTransfers = new ArrayList<>();
        int w = maxWeight;
        for (int i = n; i > 0 && w > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                TransferRequest.Transfer transfer = transfers.get(i - 1);
                selectedTransfers.add(transfer);
                w -= transfer.getWeight();
            }
        }

        return new KnapsackResult(selectedTransfers, dp[n][maxWeight]);
    }
}
