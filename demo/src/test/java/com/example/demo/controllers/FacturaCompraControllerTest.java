package com.example.demo.controllers;

import com.example.demo.models.FacturaCompraModel;
import com.example.demo.models.CompraModel;
import com.example.demo.services.CompraService;
import com.example.demo.services.FacturaCompraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

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

    @Test
    void whenPutFactura_existing_thenReturnOkAndBody() throws Exception {
        Integer id = 11;

        CompraModel compra = new CompraModel();
        compra.setIdcompra(2);
        FacturaCompraModel incoming = new FacturaCompraModel();
        incoming.setMedioPago("TARJETA");
        incoming.setTotal(1500);
        incoming.setCompra(compra);

        FacturaCompraModel saved = new FacturaCompraModel();
        saved.setMedioPago("TARJETA");
        saved.setTotal(1500);
        saved.setCompra(compra);

        when(facturaService.putFacturaCompra(eq(id), any(FacturaCompraModel.class)))
                .thenReturn(Optional.of(saved));

        mockMvc.perform(put("/facturas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incoming)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.medioPago").value("TARJETA"))
                .andExpect(jsonPath("$.total").value(1500))
                .andExpect(jsonPath("$.compra.idcompra").value(2));

        verify(facturaService, times(1)).putFacturaCompra(eq(id), any(FacturaCompraModel.class));
    }

    @Test
    void whenPutFactura_notFound_thenReturnNotFound() throws Exception {
        Integer id = 99;
        FacturaCompraModel incoming = new FacturaCompraModel();
        incoming.setMedioPago("EFECTIVO");
        incoming.setTotal(100);

        when(facturaService.putFacturaCompra(eq(id), any(FacturaCompraModel.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/facturas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incoming)))
                .andExpect(status().isNotFound());

        verify(facturaService, times(1)).putFacturaCompra(eq(id), any(FacturaCompraModel.class));
    }

    @Test
    void whenPatchFactura_existing_thenReturnOkAndBody() throws Exception {
        Integer id = 7;

        FacturaCompraModel partial = new FacturaCompraModel();
        partial.setMedioPago("TRANSFERENCIA");

        FacturaCompraModel saved = new FacturaCompraModel();
        saved.setMedioPago("TRANSFERENCIA");
        saved.setTotal(2000);
        FacturaCompraModel compra = new FacturaCompraModel();
        CompraModel compraModel = new CompraModel();
        compraModel.setIdcompra(3);
        saved.setCompra(compraModel);

        when(facturaService.patchFacturaCompra(eq(id), any(FacturaCompraModel.class)))
                .thenReturn(Optional.of(saved));

        mockMvc.perform(patch("/facturas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partial)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.medioPago").value("TRANSFERENCIA"))
                .andExpect(jsonPath("$.total").value(2000))
                .andExpect(jsonPath("$.compra.idcompra").value(3));

        verify(facturaService, times(1)).patchFacturaCompra(eq(id), any(FacturaCompraModel.class));
    }

    @Test
    void whenPatchFactura_notFound_thenReturnNotFound() throws Exception {
        Integer id = 1234;
        FacturaCompraModel partial = new FacturaCompraModel();
        partial.setMedioPago("X");

        when(facturaService.patchFacturaCompra(eq(id), any(FacturaCompraModel.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(patch("/facturas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partial)))
                .andExpect(status().isNotFound());

        verify(facturaService, times(1)).patchFacturaCompra(eq(id), any(FacturaCompraModel.class));
    }

}
