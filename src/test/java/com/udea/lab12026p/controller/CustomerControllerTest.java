package com.udea.lab12026p.controller;

import com.udea.lab12026p.dto.CustomerDTO;
import com.udea.lab12026p.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void getAllCustomers_returnsList() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(
                new CustomerDTO(1L, "Ana", "Gomez", "ACC-001", 1000.0),
                new CustomerDTO(2L, "Luis", "Perez", "ACC-002", 500.0)));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].accountNumber").value("ACC-001"))
                .andExpect(jsonPath("$[1].firstName").value("Luis"));
    }

    @Test
    void getCustomerById_returnsCustomer() throws Exception {
        when(customerService.getCustomerById(1L))
                .thenReturn(new CustomerDTO(1L, "Ana", "Gomez", "ACC-001", 1000.0));

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.balance").value(1000.0));
    }

    @Test
    void createCustomer_returnsCreatedCustomer() throws Exception {
        when(customerService.createCustomer(any(CustomerDTO.class)))
                .thenReturn(new CustomerDTO(3L, "Sara", "Lopez", "ACC-003", 250.0));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Sara\",\"lastName\":\"Lopez\",\"accountNumber\":\"ACC-003\",\"balance\":250.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.accountNumber").value("ACC-003"));
    }
}
