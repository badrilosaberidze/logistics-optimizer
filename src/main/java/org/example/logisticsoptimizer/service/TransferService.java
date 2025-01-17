package org.example.logisticsoptimizer.service;

import org.example.logisticsoptimizer.dto.KnapsackResult;
import org.example.logisticsoptimizer.dto.TransferRequest;
import org.example.logisticsoptimizer.dto.TransferResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferService {
    public TransferResponse findOptimalTransfers(TransferRequest request) {
        KnapsackResult result = KnapsackSolver.solve(request.getAvailableTransfers(), request.getMaxWeight());

        List<TransferResponse.SelectedTransfer> selectedTransfers = result.getSelectedTransfers().stream()
                .map(transfer -> {
                    TransferResponse.SelectedTransfer responseTransfer = new TransferResponse.SelectedTransfer();
                    responseTransfer.setWeight(transfer.getWeight());
                    responseTransfer.setCost(transfer.getCost());
                    return responseTransfer;
                })
                .toList();

        return new TransferResponse(selectedTransfers, result.getTotalCost(),
                selectedTransfers.stream().mapToInt(TransferResponse.SelectedTransfer::getWeight).sum());
    }
}
