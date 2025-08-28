package com.example.demo.controllers;


import com.example.demo.services.CompraService;
import com.example.demo.services.FacturaCompraService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FacturaCompraController.class)
class FacturaCompraControllerDeleteTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FacturaCompraService facturaService;

    @MockitoBean
    private CompraService compraService;


    @Test
    void deleteFactura_whenServiceReturnsTrue_shouldReturnSuccessMessage() throws Exception {
        when(facturaService.deleteFactura(1)).thenReturn(true);


        mockMvc.perform(delete("/facturas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Se ha eliminado la factura con id " + 1 + "correctamente.")));
    }


    @Test
    void deleteFactura_whenServiceReturnsFalse_shouldReturnFailureMessage() throws Exception {
        when(facturaService.deleteFactura(2)).thenReturn(false);


        mockMvc.perform(delete("/facturas/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("No se ha encontrado factura con id " + 2 + ", por favor, revisa el id.")));
    }
}