package org.example.logisticsoptimizer;

import org.example.logisticsoptimizer.dto.KnapsackResult;
import org.example.logisticsoptimizer.dto.TransferRequest;
import org.example.logisticsoptimizer.dto.TransferResponse;
import org.example.logisticsoptimizer.service.KnapsackSolver;
import org.example.logisticsoptimizer.service.TransferService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TransferServiceTest {
    TransferService logisticsService;

    @BeforeEach
    void setUp() {
        logisticsService = new TransferService();
    }

    @Test
    void testExactFit() {
        try (MockedStatic<KnapsackSolver> mockedStatic = Mockito.mockStatic(KnapsackSolver.class)) {
            TransferRequest.Transfer transfer1 = new TransferRequest.Transfer();
            transfer1.setWeight(5);
            transfer1.setCost(10);

            TransferRequest.Transfer transfer2 = new TransferRequest.Transfer();
            transfer2.setWeight(10);
            transfer2.setCost(20);

            TransferRequest request = new TransferRequest();
            request.setMaxWeight(15);
            request.setAvailableTransfers(List.of(transfer1, transfer2));

            KnapsackResult result = new KnapsackResult(List.of(transfer1, transfer2), 30);

            mockedStatic.when(() -> KnapsackSolver.solve(request.getAvailableTransfers(), request.getMaxWeight()))
                    .thenReturn(result);

            TransferResponse response = logisticsService.findOptimalTransfers(request);

            assertEquals(30, response.getTotalCost());
            assertEquals(15, response.getTotalWeight());
            assertEquals(2, response.getSelectedTransfers().size());
        }
    }

    @Test
    void testEmptyTransfers() {
        try (MockedStatic<KnapsackSolver> mockedStatic = Mockito.mockStatic(KnapsackSolver.class)) {
            TransferRequest request = new TransferRequest();
            request.setMaxWeight(15);
            request.setAvailableTransfers(List.of());

            KnapsackResult result = new KnapsackResult(List.of(), 0);

            mockedStatic.when(() -> KnapsackSolver.solve(request.getAvailableTransfers(), request.getMaxWeight()))
                    .thenReturn(result);

            TransferResponse response = logisticsService.findOptimalTransfers(request);

            assertEquals(0, response.getTotalCost());
            assertEquals(0, response.getTotalWeight());
            assertEquals(0, response.getSelectedTransfers().size());
        }
    }

    @Test
    void testSingleTransferExceedsMaxWeight() {
        try (MockedStatic<KnapsackSolver> mockedStatic = Mockito.mockStatic(KnapsackSolver.class)) {
            TransferRequest.Transfer transfer = new TransferRequest.Transfer();
            transfer.setWeight(20);
            transfer.setCost(50);

            TransferRequest request = new TransferRequest();
            request.setMaxWeight(15);
            request.setAvailableTransfers(List.of(transfer));

            KnapsackResult result = new KnapsackResult(List.of(), 0);

            mockedStatic.when(() -> KnapsackSolver.solve(request.getAvailableTransfers(), request.getMaxWeight()))
                    .thenReturn(result);

            TransferResponse response = logisticsService.findOptimalTransfers(request);

            assertEquals(0, response.getTotalCost());
            assertEquals(0, response.getTotalWeight());
            assertEquals(0, response.getSelectedTransfers().size());
        }
    }

    @Test
    void testZeroCostTransfers() {
        try (MockedStatic<KnapsackSolver> mockedStatic = Mockito.mockStatic(KnapsackSolver.class)) {
            TransferRequest.Transfer transfer1 = new TransferRequest.Transfer();
            transfer1.setWeight(5);
            transfer1.setCost(0);

            TransferRequest.Transfer transfer2 = new TransferRequest.Transfer();
            transfer2.setWeight(10);
            transfer2.setCost(0);

            TransferRequest request = new TransferRequest();
            request.setMaxWeight(15);
            request.setAvailableTransfers(List.of(transfer1, transfer2));

            KnapsackResult result = new KnapsackResult(List.of(transfer1, transfer2), 0);

            mockedStatic.when(() -> KnapsackSolver.solve(request.getAvailableTransfers(), request.getMaxWeight()))
                    .thenReturn(result);

            TransferResponse response = logisticsService.findOptimalTransfers(request);

            assertEquals(0, response.getTotalCost());
            assertEquals(15, response.getTotalWeight());
            assertEquals(2, response.getSelectedTransfers().size());
        }
    }

    @Test
    void testEqualWeightsAndCosts() {
        try (MockedStatic<KnapsackSolver> mockedStatic = Mockito.mockStatic(KnapsackSolver.class)) {
            TransferRequest.Transfer transfer1 = new TransferRequest.Transfer();
            transfer1.setWeight(10);
            transfer1.setCost(20);

            TransferRequest.Transfer transfer2 = new TransferRequest.Transfer();
            transfer2.setWeight(10);
            transfer2.setCost(20);

            TransferRequest request = new TransferRequest();
            request.setMaxWeight(15);
            request.setAvailableTransfers(List.of(transfer1, transfer2));

            KnapsackResult result = new KnapsackResult(List.of(transfer1), 20);

            mockedStatic.when(() -> KnapsackSolver.solve(request.getAvailableTransfers(), request.getMaxWeight()))
                    .thenReturn(result);

            TransferResponse response = logisticsService.findOptimalTransfers(request);

            assertEquals(20, response.getTotalCost());
            assertEquals(10, response.getTotalWeight());
            assertEquals(1, response.getSelectedTransfers().size());
        }
    }
}
