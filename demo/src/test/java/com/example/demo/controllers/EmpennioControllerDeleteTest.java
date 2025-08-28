package com.example.demo.controllers;


import com.example.demo.services.EmpennioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmpennioController.class)
class EmpennioControllerDeleteTest {


    @Autowired
    MockMvc mockMvc;


    @MockitoBean
    EmpennioService empennioService;


    @Test
    void deleteEmpennio_successAndFailure() throws Exception {
        when(empennioService.deleteEmpennio(3)).thenReturn(true);
        mockMvc.perform(delete("/empennios/3"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Se eliminó el empeño")));


        when(empennioService.deleteEmpennio(4)).thenReturn(false);
        mockMvc.perform(delete("/empennios/4"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("No se pudo eliminar el empeño")));
    }
}