package org.example.logisticsoptimizer;

import org.example.logisticsoptimizer.dto.KnapsackResult;
import org.example.logisticsoptimizer.dto.TransferRequest;
import org.example.logisticsoptimizer.dto.TransferResponse;
import org.example.logisticsoptimizer.service.KnapsackSolver;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KnapsackSolverTest {
    @Test
    void testKnapsackSample(){
        TransferRequest.Transfer transfer1 = new TransferRequest.Transfer();
        transfer1.setWeight(5);
        transfer1.setCost(10);

        TransferRequest.Transfer transfer2 = new TransferRequest.Transfer();
        transfer2.setWeight(10);
        transfer2.setCost(20);

        TransferRequest.Transfer transfer3 = new TransferRequest.Transfer();
        transfer3.setWeight(3);
        transfer3.setCost(5);

        List<TransferRequest.Transfer> transfers = List.of(transfer1, transfer2, transfer3);

        int maxWeight = 15;

        KnapsackResult result = KnapsackSolver.solve(transfers, maxWeight);

        assertEquals(2, result.getSelectedTransfers().size());
        assertEquals(30, result.getTotalCost());
    }

    @Test
    void testExactFit() {
        TransferRequest.Transfer t1 = new TransferRequest.Transfer();
        t1.setWeight(5);
        t1.setCost(10);

        TransferRequest.Transfer t2 = new TransferRequest.Transfer();
        t2.setWeight(10);
        t2.setCost(20);

        KnapsackResult response = KnapsackSolver.solve(List.of(t1, t2), 15);

        assertEquals(2 , response.getSelectedTransfers().size());
        assertEquals(30, response.getTotalCost());
    }

    @Test
    void testNoItemsFit() {
        TransferRequest.Transfer t1 = new TransferRequest.Transfer();
        t1.setWeight(50);
        t1.setCost(100);

        KnapsackResult response = KnapsackSolver.solve(List.of(t1), 10);

        assertTrue(response.getSelectedTransfers().isEmpty());
        assertEquals(0, response.getTotalCost());
    }

    @Test
    void testZeroMaxWeight() {
        TransferRequest.Transfer t1 = new TransferRequest.Transfer();
        t1.setWeight(5);
        t1.setCost(10);

        KnapsackResult response = KnapsackSolver.solve(List.of(t1), 0);

        assertTrue(response.getSelectedTransfers().isEmpty());
        assertEquals(0, response.getTotalCost());
    }

    @Test
    void testHighItemDensity() {
        TransferRequest.Transfer t1 = new TransferRequest.Transfer();
        t1.setWeight(1);
        t1.setCost(100);

        TransferRequest.Transfer t2 = new TransferRequest.Transfer();
        t2.setWeight(10);
        t2.setCost(50);

        KnapsackResult response = KnapsackSolver.solve(List.of(t1, t2), 10);

        assertEquals(100, response.getTotalCost());
    }

    void testDuplicateItems() {
        TransferRequest.Transfer t1 = new TransferRequest.Transfer();
        t1.setWeight(5);
        t1.setCost(10);

        TransferRequest.Transfer t2 = new TransferRequest.Transfer();
        t2.setWeight(5);
        t2.setCost(10);

        KnapsackResult response = KnapsackSolver.solve(List.of(t1, t2), 10);

        assertEquals(2, response.getSelectedTransfers().size());
        assertEquals(20, response.getTotalCost());
    }
}
