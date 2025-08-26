package com.example.demo.controllers;

import com.example.demo.models.EmpennioModel;
import com.example.demo.services.EmpennioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpennioController.class)
class EmpennioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmpennioService empennioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetAll_thenReturnJsonArray() throws Exception {
        EmpennioModel e1 = new EmpennioModel();
        e1.setIdempennio(1);
        e1.setEstado("ACTIVO");

        ArrayList<EmpennioModel> list = new ArrayList<>();
        list.add(e1);

        when(empennioService.getEmpennios()).thenReturn(list);

        mockMvc.perform(get("/empennios"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idempennio").value(1))
                .andExpect(jsonPath("$[0].estado").value("ACTIVO"));
    }

    @Test
    void whenPostEmpennio_thenReturnSavedObject() throws Exception {
        EmpennioModel request = new EmpennioModel();
        request.setEstado("ACTIVO");

        EmpennioModel saved = new EmpennioModel();
        saved.setIdempennio(7);
        saved.setEstado("ACTIVO");

        when(empennioService.postEmpennio(any(EmpennioModel.class))).thenReturn(saved);

        mockMvc.perform(post("/empennios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // tu controller devuelve el objeto (200 OK)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idempennio").value(7))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test
    void whenGetById_found_thenReturnOkAndBody() throws Exception {
        EmpennioModel e = new EmpennioModel();
        e.setIdempennio(1);
        e.setEstado("ACTIVO");

        when(empennioService.getEmpennioById(1)).thenReturn(Optional.of(e));

        mockMvc.perform(get("/empennios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test
    void whenGetById_notFound_thenReturnEmptyBodyCompatible() throws Exception {
        when(empennioService.getEmpennioById(999)).thenReturn(Optional.empty());
        mockMvc.perform(get("/empennios/999"))
                .andExpect(status().isOk())
                .andExpect(content().string(anyOf(equalTo(""), nullValue(String.class), equalTo("null"))));
    }

    @Test
    void whenGetByEstado_thenReturnList() throws Exception {
        EmpennioModel e = new EmpennioModel();
        e.setIdempennio(2);
        e.setEstado("ACTIVO");

        ArrayList<EmpennioModel> list = new ArrayList<>();
        list.add(e);

        when(empennioService.getEmpenniosByEstado("ACTIVO")).thenReturn(list);

        mockMvc.perform(get("/empennios/query")
                        .param("estado", "ACTIVO"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idempennio").value(2))
                .andExpect(jsonPath("$[0].estado").value("ACTIVO"));
    }

    @Test
    void whenGetByEstado_missingParam_thenBadRequest() throws Exception {
        mockMvc.perform(get("/empennios/query"))
                .andExpect(status().isBadRequest());
    }
}
