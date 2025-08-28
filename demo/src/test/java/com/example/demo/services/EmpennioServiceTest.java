package com.example.demo.services;

import com.example.demo.models.EmpennioModel;
import com.example.demo.repositories.EmpennioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EmpennioServiceTest {

    @Mock
    private EmpennioRepository empennioRepository;

    @InjectMocks
    private EmpennioService empennioService;
/*
    @Test
    void whenGetEmpennios_thenRepositoryCalled() {
        when(empennioRepository.findAll()).thenReturn(new ArrayList<>());
        var res = empennioService.getEmpennios();
        assertThat(res).isInstanceOf(ArrayList.class);
        verify(empennioRepository).findAll();
    }

    @Test
    void whenGetEmpennioById_thenReturnOptional() {
        EmpennioModel e = new EmpennioModel();
        when(empennioRepository.findById(1)).thenReturn(Optional.of(e));
        Optional<EmpennioModel> res = empennioService.getEmpennioById(1);
        assertThat(res).isPresent();
        verify(empennioRepository).findById(1);
    }
    @Test
    void postEmpennio_and_getById_and_delete() {
        EmpennioModel e = new EmpennioModel();
        when(empennioRepository.save(e)).thenReturn(e);
        EmpennioModel saved = empennioService.postEmpennio(e);
        assertThat(saved).isSameAs(e);


        when(empennioRepository.findById(10)).thenReturn(Optional.of(e));
        Optional<EmpennioModel> found = empennioService.getEmpennioById(10);
        assertThat(found).isPresent();


        doNothing().when(empennioRepository).deleteById(10);
        boolean ok = empennioService.deleteEmpennio(10);
        assertThat(ok).isTrue();


        doThrow(new RuntimeException()).when(empennioRepository).deleteById(11);
        boolean nok = empennioService.deleteEmpennio(11);
        assertThat(nok).isFalse();
    }


    @Test
    void getByEstado_returnsList() {
        String estado = "activo";
        EmpennioModel e1 = new EmpennioModel();
        EmpennioModel e2 = new EmpennioModel();

        when(empennioRepository.findByEstado(estado))
                .thenReturn(new ArrayList<>(Arrays.asList(e1, e2)));

        ArrayList<EmpennioModel> res = empennioService.getEmpenniosByEstado(estado);

        assertThat(res).hasSize(2);
        verify(empennioRepository).findByEstado(estado);
    }

 */
}
