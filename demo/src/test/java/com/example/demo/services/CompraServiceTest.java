package com.example.demo.services;

import com.example.demo.models.CompraModel;
import com.example.demo.repositories.CompraRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CompraServiceTest {

    @Mock
    private CompraRepository compraRepository;

    @InjectMocks
    private CompraService compraService;

    @Test
    void whenGetCompras_thenRepositoryCalled() {
        when(compraRepository.findAll()).thenReturn(new ArrayList<>());
        var res = compraService.getCompras();
        assertThat(res).isInstanceOf(ArrayList.class);
        verify(compraRepository, times(1)).findAll();
    }

    @Test
    void whenGetCompraById_thenReturnOptional() {
        CompraModel c = new CompraModel();
        when(compraRepository.findById(1)).thenReturn(Optional.of(c));
        Optional<CompraModel> res = compraService.getCompraById(1);
        assertThat(res).isPresent();
        verify(compraRepository).findById(1);
    }

    @Test
    void whenPostCompra_thenRepositorySaveCalled() {
        CompraModel c = new CompraModel();
        when(compraRepository.save(any())).thenReturn(c);
        CompraModel created = compraService.postCompra(c);
        assertThat(created).isNotNull();
        verify(compraRepository).save(c);
    }

    @Test
    void postCompra_savesAndReturns() {
        CompraModel c = new CompraModel();
        when(compraRepository.save(c)).thenReturn(c);

        CompraModel res = compraService.postCompra(c);

        assertThat(res).isSameAs(c);
        verify(compraRepository).save(c);
    }

    @Test
    void getCompraById_foundAndNotFound() {
        CompraModel c = new CompraModel();
        when(compraRepository.findById(2)).thenReturn(Optional.of(c));
        Optional<CompraModel> present = compraService.getCompraById(2);
        assertThat(present).isPresent().contains(c);
        verify(compraRepository).findById(2);

        when(compraRepository.findById(99)).thenReturn(Optional.empty());
        Optional<CompraModel> empty = compraService.getCompraById(99);
        assertThat(empty).isNotPresent();
    }

    @Test
    void getCompraByFecha_returnsList() {
        LocalDateTime fecha = LocalDateTime.now();
        CompraModel c1 = new CompraModel();
        CompraModel c2 = new CompraModel();
        when(compraRepository.findByFechaCompra(fecha)).thenReturn(new ArrayList<>(Arrays.asList(c1, c2)));

        ArrayList<CompraModel> res = compraService.getCompraByFecha(fecha);
        assertThat(res).hasSize(2);
        verify(compraRepository).findByFechaCompra(fecha);
    }

    @Test
    void deleteCompra_successAndFailure() {
        doNothing().when(compraRepository).deleteById(1);
        boolean ok = compraService.deleteCompra(1);
        assertThat(ok).isTrue();
        verify(compraRepository).deleteById(1);

        doThrow(new RuntimeException("fail-delete")).when(compraRepository).deleteById(2);
        boolean nok = compraService.deleteCompra(2);
        assertThat(nok).isFalse();
    }
}
