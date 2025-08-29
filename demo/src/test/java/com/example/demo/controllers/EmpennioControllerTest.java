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


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Optional;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

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

    @Test
    void whenPutEmpennio_existing_thenReturnOkAndBody() throws Exception {
        Integer id = 42;

        EmpennioModel incoming = new EmpennioModel(
                new BigDecimal("150.50"),
                "ACTIVO",
                LocalDateTime.of(2025, 8, 25, 22, 0).withNano(0),
                LocalDateTime.of(2025, 9, 25, 22, 0).withNano(0),
                new BigDecimal("2.5")
        );

        EmpennioModel saved = new EmpennioModel(
                incoming.getPrecio(),
                incoming.getEstado(),
                incoming.getFechaInicio(),
                incoming.getFechaFinal(),
                incoming.getInteres()
        );
        saved.setIdempennio(id);

        when(empennioService.putEmpennio(eq(id), any(EmpennioModel.class)))
                .thenReturn(Optional.of(saved));

        mockMvc.perform(put("/empennios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incoming)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idempennio").value(id))
                .andExpect(jsonPath("$.estado").value("ACTIVO"))
                .andExpect(jsonPath("$.precio").value(150.50))
                .andExpect(jsonPath("$.fechaInicio").value("2025-08-25T22:00:00"))
                .andExpect(jsonPath("$.fechaFinal").value("2025-09-25T22:00:00"));

        verify(empennioService, times(1)).putEmpennio(eq(id), any(EmpennioModel.class));
    }

    @Test
    void whenPutEmpennio_notFound_thenReturnNotFound() throws Exception {
        Integer id = 999;
        EmpennioModel incoming = new EmpennioModel(
                new BigDecimal("10.0"),
                "INACTIVO",
                LocalDateTime.now().withNano(0),
                LocalDateTime.now().plusDays(1).withNano(0),
                BigDecimal.ZERO
        );

        when(empennioService.putEmpennio(eq(id), any(EmpennioModel.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/empennios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incoming)))
                .andExpect(status().isNotFound());

        verify(empennioService, times(1)).putEmpennio(eq(id), any(EmpennioModel.class));
    }

    @Test
    void whenPatchEmpennio_existing_thenReturnOkAndBody() throws Exception {
        Integer id = 7;

        EmpennioModel partial = new EmpennioModel(
                null,
                "PARCHEADO",
                null,
                null,
                new BigDecimal("3.14")
        );

        EmpennioModel saved = new EmpennioModel(
                new BigDecimal("200.00"),
                "PARCHEADO",
                LocalDateTime.of(2025, 6, 1, 12, 0).withNano(0),
                LocalDateTime.of(2025, 7, 1, 12, 0).withNano(0),
                new BigDecimal("3.14")
        );
        saved.setIdempennio(id);

        when(empennioService.patchEmpennio(eq(id), any(EmpennioModel.class)))
                .thenReturn(Optional.of(saved));

        mockMvc.perform(patch("/empennios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partial)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idempennio").value(id))
                .andExpect(jsonPath("$.estado").value("PARCHEADO"))
                .andExpect(jsonPath("$.interes").value(3.14));

        verify(empennioService, times(1)).patchEmpennio(eq(id), any(EmpennioModel.class));
    }

    @Test
    void whenPatchEmpennio_notFound_thenReturnNotFound() throws Exception {
        Integer id = 555;
        EmpennioModel partial = new EmpennioModel();
        partial.setEstado("X");

        when(empennioService.patchEmpennio(eq(id), any(EmpennioModel.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(patch("/empennios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partial)))
                .andExpect(status().isNotFound());

        verify(empennioService, times(1)).patchEmpennio(eq(id), any(EmpennioModel.class));
    }
}
