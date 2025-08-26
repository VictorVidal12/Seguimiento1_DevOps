package com.example.demo.services;

import com.example.demo.models.FacturaCompraModel;
import com.example.demo.models.CompraModel;
import com.example.demo.repositories.FacturaCompraRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FacturaCompraServiceTest {

    @Mock
    private FacturaCompraRepository facturaRepository;

    @InjectMocks
    private FacturaCompraService facturaService;

    @Test
    void whenGetFacturaByCompra_thenReturnOptional() {
        CompraModel c = new CompraModel();
        FacturaCompraModel f = new FacturaCompraModel();
        when(facturaRepository.findByCompra(c)).thenReturn(Optional.of(f));
        Optional<FacturaCompraModel> res = facturaService.getFacturaByCompra(c);
        assertThat(res).isPresent();
        verify(facturaRepository).findByCompra(c);
    }

    @Test
    void postAndGetByCompra_andDelete() {
        FacturaCompraModel f = new FacturaCompraModel();
        when(facturaRepository.save(f)).thenReturn(f);
        FacturaCompraModel saved = facturaService.postFactura(f);
        assertThat(saved).isSameAs(f);


        CompraModel c = new CompraModel();
        when(facturaRepository.findByCompra(c)).thenReturn(Optional.of(f));
        Optional<FacturaCompraModel> found = facturaService.getFacturaByCompra(c);
        assertThat(found).isPresent();


        doNothing().when(facturaRepository).deleteById(1);
        boolean ok = facturaService.deleteFactura(1);
        assertThat(ok).isTrue();


        doThrow(new RuntimeException()).when(facturaRepository).deleteById(2);
        boolean nok = facturaService.deleteFactura(2);
        assertThat(nok).isFalse();
    }
}
