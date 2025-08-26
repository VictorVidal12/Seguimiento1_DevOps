package com.example.demo.controllers;

import com.example.demo.models.EmpennioModel;
import com.example.demo.services.EmpennioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpennioController.class)
class EmpennioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmpennioService empennioService;

    @Test
    void whenGetById_thenReturnOkJson() throws Exception {
        EmpennioModel e = new EmpennioModel();
        e.setEstado("ACTIVO");
        when(empennioService.getEmpennioById(1)).thenReturn(Optional.of(e));

        mockMvc.perform(get("/empennios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }
}
