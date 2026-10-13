package com.udea.lab12026p.service;

import com.udea.lab12026p.dto.TransactionDTO;
import com.udea.lab12026p.entity.Customer;
import com.udea.lab12026p.entity.Transaction;
import com.udea.lab12026p.repository.CustomerRepository;
import com.udea.lab12026p.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Customer sender;
    private Customer receiver;

    @BeforeEach
    void setUp() {
        sender = new Customer(1L, "ACC-001", "Ana", "Gomez", 1000.0);
        receiver = new Customer(2L, "ACC-002", "Luis", "Perez", 500.0);
    }

    private TransactionDTO request(String from, String to, double amount) {
        return new TransactionDTO(null, from, to, amount, LocalDateTime.now());
    }

    @Test
    void transferMoney_success_updatesBalancesAndSavesTransaction() {
        when(customerRepository.findByAccountNumber("ACC-001")).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber("ACC-002")).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> {
            Transaction t = inv.getArgument(0);
            t.setId(10L);
            return t;
        });

        TransactionDTO result = transactionService.transferMoney(request("ACC-001", "ACC-002", 200.0));

        assertEquals(800.0, sender.getBalance());
        assertEquals(700.0, receiver.getBalance());
        assertEquals(10L, result.getId());
        assertEquals("ACC-001", result.getSenderAccountNumber());
        assertEquals("ACC-002", result.getReceiverAccountNumber());
        assertEquals(200.0, result.getAmount());
        verify(customerRepository).save(sender);
        verify(customerRepository).save(receiver);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void transferMoney_nullAccounts_throwsIllegalArgument() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request(null, null, 100.0)));

        assertTrue(ex.getMessage().contains("obligatorios"));
        verifyNoInteractions(customerRepository, transactionRepository);
    }

    @Test
    void transferMoney_missingSender_throwsIllegalArgument() {
        when(customerRepository.findByAccountNumber("NOPE")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request("NOPE", "ACC-002", 100.0)));

        assertEquals("La cuenta del remitente no existe.", ex.getMessage());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferMoney_missingReceiver_throwsIllegalArgument() {
        when(customerRepository.findByAccountNumber("ACC-001")).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber("NOPE")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request("ACC-001", "NOPE", 100.0)));

        assertEquals("La cuenta del receptor no existe.", ex.getMessage());
        assertEquals(1000.0, sender.getBalance());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferMoney_insufficientBalance_throwsIllegalArgument() {
        when(customerRepository.findByAccountNumber("ACC-001")).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber("ACC-002")).thenReturn(Optional.of(receiver));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request("ACC-001", "ACC-002", 5000.0)));

        assertEquals("Saldo insuficiente en la cuenta del remitente.", ex.getMessage());
        assertEquals(1000.0, sender.getBalance());
        assertEquals(500.0, receiver.getBalance());
        verify(customerRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void getTransactionsForAccount_returnsMappedHistory() {
        LocalDateTime now = LocalDateTime.now();
        List<Transaction> history = List.of(
                new Transaction(1L, "ACC-001", "ACC-002", 100.0, now),
                new Transaction(2L, "ACC-002", "ACC-001", 50.0, now));
        when(transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber("ACC-001", "ACC-001"))
                .thenReturn(history);

        List<TransactionDTO> result = transactionService.getTransactionsForAccount("ACC-001");

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(100.0, result.get(0).getAmount());
        assertEquals("ACC-002", result.get(1).getSenderAccountNumber());
        assertEquals(now, result.get(1).getTimestamp());
    }
}
