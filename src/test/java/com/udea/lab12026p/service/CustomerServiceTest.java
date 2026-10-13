package com.udea.lab12026p.service;

import com.udea.lab12026p.dto.CustomerDTO;
import com.udea.lab12026p.entity.Customer;
import com.udea.lab12026p.mapper.CustomerMapper;
import com.udea.lab12026p.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "ACC-001", "Ana", "Gomez", 1000.0);
        customerDTO = new CustomerDTO(1L, "Ana", "Gomez", "ACC-001", 1000.0);
    }

    @Test
    void getAllCustomers_returnsMappedList() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));
        when(customerMapper.toDTO(customer)).thenReturn(customerDTO);

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertEquals(1, result.size());
        assertEquals("ACC-001", result.get(0).getAccountNumber());
        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById_existingId_returnsCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toDTO(customer)).thenReturn(customerDTO);

        CustomerDTO result = customerService.getCustomerById(1L);

        assertEquals("Ana", result.getFirstName());
        assertEquals(1000.0, result.getBalance());
    }

    @Test
    void getCustomerById_missingId_throwsException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> customerService.getCustomerById(99L));

        assertEquals("Cliente no encontrado", ex.getMessage());
        verify(customerMapper, never()).toDTO(any());
    }

    @Test
    void createCustomer_savesAndReturnsDTO() {
        CustomerDTO input = new CustomerDTO(null, "Ana", "Gomez", "ACC-001", 1000.0);
        Customer toSave = new Customer(null, "ACC-001", "Ana", "Gomez", 1000.0);
        when(customerMapper.toEntity(input)).thenReturn(toSave);
        when(customerRepository.save(toSave)).thenReturn(customer);
        when(customerMapper.toDTO(customer)).thenReturn(customerDTO);

        CustomerDTO result = customerService.createCustomer(input);

        assertEquals(1L, result.getId());
        verify(customerRepository).save(toSave);
    }
}
