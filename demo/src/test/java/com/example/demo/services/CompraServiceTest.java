package com.example.demo.services;

import com.example.demo.models.CompraModel;
import com.example.demo.repositories.CompraRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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
}
