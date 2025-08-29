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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CompraServiceTest {

    @Mock
    private CompraRepository compraRepository;

    @InjectMocks
    private CompraService compraService;

    public static class CompraBean {
        private Integer id;
        private String proveedor;
        private Integer cantidad;
        private Double total;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getProveedor() { return proveedor; }
        public void setProveedor(String proveedor) { this.proveedor = proveedor; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public Double getTotal() { return total; }
        public void setTotal(Double total) { this.total = total; }
    }
    /*

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

    @Test
    void copyNonNullProperties_compra_shouldCopyNonNullAndIgnoreNullsAndId() throws Exception {
        CompraBean src = new CompraBean();
        src.setId(777);
        src.setProveedor("Proveedor B");
        src.setCantidad(null);
        src.setTotal(999.99);

        CompraBean target = new CompraBean();
        target.setId(10);
        target.setProveedor("Proveedor A");
        target.setCantidad(5);
        target.setTotal(50.0);

        CompraService service = new CompraService();
        Method m = CompraService.class.getDeclaredMethod("copyNonNullProperties", Object.class, Object.class);
        m.setAccessible(true);

        m.invoke(service, src, target);

        assertThat(target.getProveedor()).isEqualTo("Proveedor B");
        assertThat(target.getTotal()).isEqualTo(999.99);
        assertThat(target.getCantidad()).isEqualTo(5);
        assertThat(target.getId()).isEqualTo(10);
    }

    @Test
    void copyNonNullProperties_compra_allNullSource_shouldNotModifyTarget() throws Exception {
        CompraBean src = new CompraBean();
        src.setId(800);
        src.setProveedor(null);
        src.setCantidad(null);
        src.setTotal(null);

        CompraBean target = new CompraBean();
        target.setId(11);
        target.setProveedor("Orig");
        target.setCantidad(7);
        target.setTotal(77.7);

        CompraService service = new CompraService();
        Method m = CompraService.class.getDeclaredMethod("copyNonNullProperties", Object.class, Object.class);
        m.setAccessible(true);

        m.invoke(service, src, target);

        assertThat(target.getProveedor()).isEqualTo("Orig");
        assertThat(target.getCantidad()).isEqualTo(7);
        assertThat(target.getTotal()).isEqualTo(77.7);
        assertThat(target.getId()).isEqualTo(11);
    }

    @Test
    void putCompra_existing_shouldSetIdAndSave() {
        Integer id = 10;
        CompraModel existing = new CompraModel(200, LocalDateTime.of(2025, 1, 1, 0, 0), "OLD");
        existing.setIdcompra(id);

        CompraModel incoming = new CompraModel(500, LocalDateTime.of(2025, 2, 1, 0, 0), "NEW");

        when(compraRepository.findById(id)).thenReturn(Optional.of(existing));
        when(compraRepository.save(any(CompraModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<CompraModel> resultOpt = compraService.putCompra(id, incoming);

        assertThat(resultOpt).isPresent();
        CompraModel saved = resultOpt.get();

        assertThat(incoming.getIdcompra()).isEqualTo(id);
        assertThat(saved.getIdcompra()).isEqualTo(id);
        assertThat(saved.getTotal()).isEqualTo(500);
        assertThat(saved.getEstado()).isEqualTo("NEW");
        assertThat(saved.getFechaCompra()).isEqualTo(LocalDateTime.of(2025, 2, 1, 0, 0));

        verify(compraRepository, times(1)).findById(id);
        verify(compraRepository, times(1)).save(incoming);
    }

    @Test
    void putCompra_notFound_shouldReturnEmptyAndNotSave() {
        Integer id = 99;
        CompraModel incoming = new CompraModel(1000, LocalDateTime.now(), "NEW");

        when(compraRepository.findById(id)).thenReturn(Optional.empty());

        Optional<CompraModel> res = compraService.putCompra(id, incoming);

        assertThat(res).isNotPresent();
        verify(compraRepository, times(1)).findById(id);
        verify(compraRepository, never()).save(any());
    }

    @Test
    void patchCompra_existing_shouldCopyNonNullAndSave() {
        Integer id = 5;
        CompraModel existing = new CompraModel(300, LocalDateTime.of(2025, 3, 1, 0, 0), "OLD");
        existing.setIdcompra(id);

        CompraModel partial = new CompraModel(null, null, "PATCHED");

        when(compraRepository.findById(id)).thenReturn(Optional.of(existing));
        when(compraRepository.save(any(CompraModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<CompraModel> resultOpt = compraService.patchCompra(id, partial);

        assertThat(resultOpt).isPresent();
        CompraModel saved = resultOpt.get();

        assertThat(saved.getEstado()).isEqualTo("PATCHED");
        assertThat(saved.getTotal()).isEqualTo(300);
        assertThat(saved.getFechaCompra()).isEqualTo(LocalDateTime.of(2025, 3, 1, 0, 0));
        assertThat(saved.getIdcompra()).isEqualTo(id);

        verify(compraRepository, times(1)).findById(id);
        verify(compraRepository, times(1)).save(existing); // save should receive the modified existing instance
    }

    @Test
    void patchCompra_notFound_shouldReturnEmptyAndNotSave() {
        Integer id = 1234;
        CompraModel partial = new CompraModel(null, null, "X");

        when(compraRepository.findById(id)).thenReturn(Optional.empty());

        Optional<CompraModel> res = compraService.patchCompra(id, partial);

        assertThat(res).isNotPresent();
        verify(compraRepository, times(1)).findById(id);
        verify(compraRepository, never()).save(any());
    }
     */
}
