package com.udea.lab12026p.controller;

import com.udea.lab12026p.dto.TransactionDTO;
import com.udea.lab12026p.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    private static final String TRANSFER_JSON =
            "{\"senderAccountNumber\":\"ACC-001\",\"receiverAccountNumber\":\"ACC-002\",\"amount\":200.0}";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void transferMoney_valid_returnsTransaction() throws Exception {
        when(transactionService.transferMoney(any(TransactionDTO.class)))
                .thenReturn(new TransactionDTO(10L, "ACC-001", "ACC-002", 200.0, LocalDateTime.now()));

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TRANSFER_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.amount").value(200.0));
    }

    @Test
    void transferMoney_invalid_returnsBadRequest() throws Exception {
        when(transactionService.transferMoney(any(TransactionDTO.class)))
                .thenThrow(new IllegalArgumentException("Saldo insuficiente en la cuenta del remitente."));

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TRANSFER_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Saldo insuficiente")));
    }

    @Test
    void getTransactionsByAccount_returnsHistory() throws Exception {
        when(transactionService.getTransactionsForAccount("ACC-001")).thenReturn(List.of(
                new TransactionDTO(1L, "ACC-001", "ACC-002", 100.0, LocalDateTime.now())));

        mockMvc.perform(get("/api/transactions/ACC-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].receiverAccountNumber").value("ACC-002"));
    }
}
