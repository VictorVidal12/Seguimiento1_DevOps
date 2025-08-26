package com.example.demo.controllers;


import com.example.demo.services.CompraService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CompraController.class)
class CompraControllerDeleteTest {


    @Autowired
    MockMvc mockMvc;


    @MockitoBean
    CompraService compraService;


    @Test
    void deleteCompra_whenServiceReturnsTrue_shouldReturnSuccessMessage() throws Exception {
        when(compraService.deleteCompra(5)).thenReturn(true);


        mockMvc.perform(delete("/compras/5"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Se eliminó la compra")));
    }


    @Test
    void deleteCompra_whenServiceReturnsFalse_shouldReturnFailureMessage() throws Exception {
        when(compraService.deleteCompra(7)).thenReturn(false);


        mockMvc.perform(delete("/compras/7"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("No se pudo eliminar la compra")));
    }
}