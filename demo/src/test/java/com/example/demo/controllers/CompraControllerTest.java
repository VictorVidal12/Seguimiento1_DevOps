package com.example.demo.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.example.demo.models.CompraModel;
import com.example.demo.services.CompraService;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;




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

     */
}
