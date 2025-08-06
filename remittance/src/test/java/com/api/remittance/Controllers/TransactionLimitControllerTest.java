package com.api.remittance.Controllers;

import com.api.remittance.Entities.TransactionLimit;
import com.api.remittance.Exceptions.TransactionNotFoundException;
import com.api.remittance.Repositories.TransactionLimitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransactionLimitControllerTest {

    private TransactionLimitRepository repository;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        repository = mock(TransactionLimitRepository.class);
        TransactionLimitController controller = new TransactionLimitController(repository);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new Object() {
                    // Simple exception handler for TransactionNotFoundException
                    @org.springframework.web.bind.annotation.ExceptionHandler(TransactionNotFoundException.class)
                    public void handle() {}
                })
                .build();
    }

    @Test
    void testGetAllTransactionLimits() throws Exception {
        TransactionLimit limit1 = new TransactionLimit();
        limit1.setId(1L);
        TransactionLimit limit2 = new TransactionLimit();
        limit2.setId(2L);

        when(repository.findAll()).thenReturn(Arrays.asList(limit1, limit2));

        mockMvc.perform(get("/transaction-limits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void testCreateTransactionLimit() throws Exception {
        TransactionLimit limit = new TransactionLimit();
        limit.setId(1L);
        limit.setLimitAmount(1000.0);

        when(repository.save(ArgumentMatchers.any(TransactionLimit.class))).thenReturn(limit);

        String json = "{\"id\":1,\"limitAmount\":1000.0}";

        mockMvc.perform(post("/transaction-limits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.limitAmount").value(1000.0));
    }
}