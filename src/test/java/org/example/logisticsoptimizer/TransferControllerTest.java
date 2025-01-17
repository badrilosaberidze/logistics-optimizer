package org.example.logisticsoptimizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.logisticsoptimizer.controller.TransferController;
import org.example.logisticsoptimizer.dto.TransferRequest;
import org.example.logisticsoptimizer.dto.TransferResponse;
import org.example.logisticsoptimizer.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TransferService logisticsService;

    @InjectMocks
    private TransferController transferController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transferController).build();
    }

    @Test
    void testExactFit() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setMaxWeight(15);
        request.setAvailableTransfers(List.of(
                new TransferRequest.Transfer(5, 10),
                new TransferRequest.Transfer(10, 20)
        ));

        TransferResponse response = new TransferResponse(
                List.of(
                        new TransferResponse.SelectedTransfer(5, 10),
                        new TransferResponse.SelectedTransfer(10, 20)
                ),
                30,
                15
        );

        when(logisticsService.findOptimalTransfers(any(TransferRequest.class))).thenReturn(response);

        mockMvc.perform(post("/transfers")
                        .contentType("application/json")
                        .content("{\"maxWeight\":15,\"availableTransfers\":[{\"weight\":5,\"cost\":10},{\"weight\":10,\"cost\":20}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost").value(30))
                .andExpect(jsonPath("$.totalWeight").value(15))
                .andExpect(jsonPath("$.selectedTransfers", hasSize(2)));
    }

    @Test
    void testEmptyTransfers() throws Exception {
        TransferResponse response = new TransferResponse(List.of(), 0, 0);

        when(logisticsService.findOptimalTransfers(any(TransferRequest.class))).thenReturn(response);

        mockMvc.perform(post("/transfers")
                        .contentType("application/json")
                        .content("{\"maxWeight\":15,\"availableTransfers\":[]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost").value(0))
                .andExpect(jsonPath("$.totalWeight").value(0))
                .andExpect(jsonPath("$.selectedTransfers", hasSize(0)));
    }

    @Test
    void testExceedingWeight() throws Exception {
        TransferRequest.Transfer transfer1 = new TransferRequest.Transfer();
        transfer1.setWeight(20);
        transfer1.setCost(15);

        TransferRequest.Transfer transfer2 = new TransferRequest.Transfer();
        transfer2.setWeight(25);
        transfer2.setCost(30);

        TransferResponse response = new TransferResponse(List.of(), 0, 0);

        when(logisticsService.findOptimalTransfers(any(TransferRequest.class))).thenReturn(response);

        mockMvc.perform(post("/transfers")
                        .contentType("application/json")
                        .content("{\"maxWeight\":10,\"availableTransfers\":[{\"weight\":20,\"cost\":15},{\"weight\":25,\"cost\":30}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost").value(0))
                .andExpect(jsonPath("$.totalWeight").value(0))
                .andExpect(jsonPath("$.selectedTransfers", hasSize(0)));
    }

    @Test
    void testSingleOptimalTransfer() throws Exception {
        TransferRequest.Transfer transfer1 = new TransferRequest.Transfer();
        transfer1.setWeight(10);
        transfer1.setCost(50);

        TransferResponse response = new TransferResponse(
                List.of(new TransferResponse.SelectedTransfer(10, 50)),
                50,
                10
        );

        when(logisticsService.findOptimalTransfers(any(TransferRequest.class))).thenReturn(response);

        mockMvc.perform(post("/transfers")
                        .contentType("application/json")
                        .content("{\"maxWeight\":15,\"availableTransfers\":[{\"weight\":10,\"cost\":50}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost").value(50))
                .andExpect(jsonPath("$.totalWeight").value(10))
                .andExpect(jsonPath("$.selectedTransfers", hasSize(1)));
    }

    @Test
    void testLargeAndTrickyData() throws Exception {
        List<TransferRequest.Transfer> availableTransfers = List.of(
                new TransferRequest.Transfer(5, 10),
                new TransferRequest.Transfer(8, 15),
                new TransferRequest.Transfer(10, 40),
                new TransferRequest.Transfer(4, 5),
                new TransferRequest.Transfer(7, 25),
                new TransferRequest.Transfer(3, 8),
                new TransferRequest.Transfer(6, 18),
                new TransferRequest.Transfer(9, 30),
                new TransferRequest.Transfer(12, 50),
                new TransferRequest.Transfer(2, 1)
        );

        TransferResponse expectedResponse = new TransferResponse(
                List.of(
                        new TransferResponse.SelectedTransfer(7, 25),
                        new TransferResponse.SelectedTransfer(8, 15),
                        new TransferResponse.SelectedTransfer(6, 18)
                ),
                58,
                21
        );

        when(logisticsService.findOptimalTransfers(any(TransferRequest.class)))
                .thenReturn(expectedResponse);

        StringBuilder jsonInputBuilder = new StringBuilder("{\"maxWeight\":21,\"availableTransfers\":[");
        for (TransferRequest.Transfer transfer : availableTransfers) {
            jsonInputBuilder.append(String.format(
                    "{\"weight\":%d,\"cost\":%d},", transfer.getWeight(), transfer.getCost()
            ));
        }
        String jsonInput = jsonInputBuilder.substring(0, jsonInputBuilder.length() - 1) + "]}";

        mockMvc.perform(post("/transfers")
                        .contentType("application/json")
                        .content(jsonInput))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost").value(58))
                .andExpect(jsonPath("$.totalWeight").value(21))
                .andExpect(jsonPath("$.selectedTransfers", hasSize(3)));
    }
}
