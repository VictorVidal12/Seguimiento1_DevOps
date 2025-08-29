package com.example.demo.controllers;

import com.example.demo.models.CompraModel;
import com.example.demo.services.CompraService;
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

@WebMvcTest(CompraController.class)
class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompraService compraService;

    @Autowired
    private ObjectMapper objectMapper;

    /*
    @Test
    void whenGetAll_thenReturnJsonArray() throws Exception {
        CompraModel c1 = new CompraModel();
        c1.setIdcompra(1);
        c1.setTotal(10);
        c1.setFechaCompra(LocalDateTime.now().withNano(0));
        c1.setEstado("CREADA");

        ArrayList<CompraModel> list = new ArrayList<>();
        list.add(c1);

        when(compraService.getCompras()).thenReturn(list);

        mockMvc.perform(get("/compras"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idcompra").value(1))
                .andExpect(jsonPath("$[0].total").value(10))
                .andExpect(jsonPath("$[0].estado").value("CREADA"));
    }

    @Test
    void whenPostCompra_thenReturnCreatedObject() throws Exception {
        CompraModel request = new CompraModel();
        request.setTotal(20);
        request.setFechaCompra(LocalDateTime.of(2024,1,1,10,0));
        request.setEstado("CREADA");

        CompraModel saved = new CompraModel();
        saved.setIdcompra(7);
        saved.setTotal(20);
        saved.setFechaCompra(request.getFechaCompra());
        saved.setEstado("CREADA");

        when(compraService.postCompra(any(CompraModel.class))).thenReturn(saved);

        mockMvc.perform(post("/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // tu controller devuelve el objeto (200 OK)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idcompra").value(7))
                .andExpect(jsonPath("$.total").value(20))
                .andExpect(jsonPath("$.estado").value("CREADA"));
    }

    @Test
    void whenGetById_found_thenReturnOkAndBody() throws Exception {
        CompraModel c = new CompraModel();
        c.setIdcompra(1);
        c.setTotal(10);
        c.setFechaCompra(LocalDateTime.now().withNano(0));
        c.setEstado("CREADA");

        when(compraService.getCompraById(1)).thenReturn(Optional.of(c));

        mockMvc.perform(get("/compras/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.total").value(10));
    }

    @Test
    void whenGetById_notFound_thenReturnOkEmptyBody() throws Exception {
        mockMvc.perform(get("/compras/999"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    byte[] bytes = result.getResponse().getContentAsByteArray();
                    String text = result.getResponse().getContentAsString();
                    boolean ok = bytes.length == 0 || text.isEmpty() || "null".equals(text);
                    assertTrue(ok, () -> "Response body unexpected. length=" + bytes.length + " text=" + text);
                });
    }

    @Test
    void whenGetByFecha_validDateParam_thenReturnList() throws Exception {
        LocalDateTime fecha = LocalDateTime.of(2024,2,2,12,0);
        CompraModel c = new CompraModel();
        c.setIdcompra(2);
        c.setTotal(5);
        c.setFechaCompra(fecha);
        c.setEstado("CREADA");

        ArrayList<CompraModel> list = new ArrayList<>();
        list.add(c);

        when(compraService.getCompraByFecha(fecha)).thenReturn(list);

        String fechaStr = fecha.toString(); // ISO-8601

        mockMvc.perform(get("/compras/query")
                        .param("fechaCompra", fechaStr))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idcompra").value(2))
                .andExpect(jsonPath("$[0].fechaCompra").exists());
    }

    @Test
    void whenGetByFecha_invalidDateParam_thenBadRequest() throws Exception {
        mockMvc.perform(get("/compras/query")
                        .param("fechaCompra", "not-a-date"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPutCompra_existing_thenReturnOkAndBody() throws Exception {
        Integer id = 10;
        CompraModel incoming = new CompraModel(500, LocalDateTime.of(2025, 2, 1, 0, 0), "ACTUALIZADA");
        CompraModel saved = new CompraModel(500, LocalDateTime.of(2025, 2, 1, 0, 0), "ACTUALIZADA");
        saved.setIdcompra(id);

        when(compraService.putCompra(eq(id), any(CompraModel.class))).thenReturn(Optional.of(saved));

        mockMvc.perform(put("/compras/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incoming)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idcompra").value(id))
                .andExpect(jsonPath("$.total").value(500))
                .andExpect(jsonPath("$.estado").value("ACTUALIZADA"));

        verify(compraService, times(1)).putCompra(eq(id), any(CompraModel.class));
    }

    @Test
    void whenPutCompra_notFound_thenReturnNotFound() throws Exception {
        Integer id = 99;
        CompraModel incoming = new CompraModel(100, LocalDateTime.now(), "X");

        when(compraService.putCompra(eq(id), any(CompraModel.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/compras/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incoming)))
                .andExpect(status().isNotFound());

        verify(compraService, times(1)).putCompra(eq(id), any(CompraModel.class));
    }


    @Test
    void whenPatchCompra_existing_thenReturnOkAndBody() throws Exception {
        Integer id = 5;
        CompraModel partial = new CompraModel(null, null, "PATCHED");
        CompraModel existing = new CompraModel(300, LocalDateTime.of(2025, 3, 1, 0, 0), "OLD");
        existing.setIdcompra(id);
        CompraModel saved = new CompraModel(300, LocalDateTime.of(2025, 3, 1, 0, 0), "PATCHED");
        saved.setIdcompra(id);

        when(compraService.patchCompra(eq(id), any(CompraModel.class))).thenReturn(Optional.of(saved));

        mockMvc.perform(patch("/compras/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partial)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idcompra").value(id))
                .andExpect(jsonPath("$.estado").value("PATCHED"));

        verify(compraService, times(1)).patchCompra(eq(id), any(CompraModel.class));
    }

    @Test
    void whenPatchCompra_notFound_thenReturnNotFound() throws Exception {
        Integer id = 1234;
        CompraModel partial = new CompraModel(null, null, "X");

        when(compraService.patchCompra(eq(id), any(CompraModel.class))).thenReturn(Optional.empty());

        mockMvc.perform(patch("/compras/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partial)))
                .andExpect(status().isNotFound());

        verify(compraService, times(1)).patchCompra(eq(id), any(CompraModel.class));
    }

    */
}
