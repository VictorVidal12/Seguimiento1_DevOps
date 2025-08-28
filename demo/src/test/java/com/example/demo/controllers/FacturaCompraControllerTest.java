package com.example.demo.controllers;

import com.example.demo.models.CompraModel;
import com.example.demo.models.FacturaCompraModel;
import com.example.demo.services.CompraService;
import com.example.demo.services.FacturaCompraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacturaCompraController.class)
class FacturaCompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacturaCompraService facturaService;

    @MockitoBean
    private CompraService compraService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void whenGetById_thenReturnOkJson() throws Exception {
        FacturaCompraModel f = new FacturaCompraModel();
        f.setMedioPago("EFECTIVO");
        when(facturaService.getFacturaById(1)).thenReturn(Optional.of(f));

        mockMvc.perform(get("/facturas/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.medioPago").value("EFECTIVO"));
    }
    @Test
    void whenGetAll_thenReturnOkJson() throws Exception {
        FacturaCompraModel f = new FacturaCompraModel();
        f.setMedioPago("EFECTIVO");
        ArrayList<FacturaCompraModel> list = new ArrayList<>();
        list.add(f);

        when(facturaService.getFacturas()).thenReturn(list);

        mockMvc.perform(get("/facturas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].medioPago").value("EFECTIVO"));

        verify(facturaService, times(1)).getFacturas();
    }

    @Test
    void whenGetByCompra_thenReturnOkJson() throws Exception {
        FacturaCompraModel f = new FacturaCompraModel();
        f.setMedioPago("EFECTIVO");
        CompraModel compra = new CompraModel();
        compra.setIdcompra(1);

        when(compraService.getCompraById(1)).thenReturn(Optional.of(compra));

        when(facturaService.getFacturaByCompra(any(CompraModel.class))).thenReturn(Optional.of(f));

        mockMvc.perform(get("/facturas/query")
                        .param("compra_idcompra", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.medioPago").value("EFECTIVO"));

        verify(compraService, times(1)).getCompraById(1);
        verify(facturaService, times(1)).getFacturaByCompra(any(CompraModel.class));
    }


    @Test
    void whenPostFactura_thenReturnOkJson() throws Exception {
        FacturaCompraModel f = new FacturaCompraModel();
        f.setMedioPago("EFECTIVO");

        when(facturaService.postFactura(any(FacturaCompraModel.class))).thenReturn(f);

        String json = objectMapper.writeValueAsString(f);

        mockMvc.perform(post("/facturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.medioPago").value("EFECTIVO"));

        verify(facturaService, times(1)).postFactura(any(FacturaCompraModel.class));
    }
}
