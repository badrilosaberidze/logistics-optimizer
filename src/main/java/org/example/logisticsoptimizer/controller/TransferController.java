package org.example.logisticsoptimizer.controller;

import jakarta.validation.Valid;
import org.example.logisticsoptimizer.dto.TransferRequest;
import org.example.logisticsoptimizer.dto.TransferResponse;
import org.example.logisticsoptimizer.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/transfers")
public class TransferController {
    private final TransferService transferService;

    public TransferController() {
        this(null);
    }

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping()
    public ResponseEntity<TransferResponse> calculateRoute(@RequestBody @Valid TransferRequest request) {
        TransferResponse response = transferService.findOptimalTransfers(request);
        return ResponseEntity.ok(response);
    }
}
