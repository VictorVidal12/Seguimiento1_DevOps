package com.example.demo.services;

import com.example.demo.models.FacturaCompraModel;
import com.example.demo.models.CompraModel;
import com.example.demo.repositories.FacturaCompraRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeanUtils;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;
import java.lang.reflect.Field;

@ExtendWith(MockitoExtension.class)
class FacturaCompraServiceTest {

    @Mock
    private FacturaCompraRepository facturaCompraRepository;

    @InjectMocks
    private FacturaCompraService facturaCompraService;

    private FacturaCompraModel existing;
    private FacturaCompraModel updated;
    private FacturaCompraModel partial;

    @BeforeEach
    void setUp() {
        existing = new FacturaCompraModel();
        existing.setIdFacturaCompra(1);
        existing.setMedioPago("TRANSFERENCIA");
        existing.setTotal(100);

        updated = new FacturaCompraModel();
        updated.setMedioPago("TARJETA");
        updated.setTotal(200);

        partial = new FacturaCompraModel();
        partial.setMedioPago("TRANSFERENCIA");
        partial.setTotal(null);

        // NO dejamos stubbing global aquí para evitar UnnecessaryStubbingException.
        // Cada test define los `when(...)` que necesita.
    }

    @Test
    void whenGetFacturaByCompra_thenReturnOptional() {
        CompraModel c = new CompraModel();
        FacturaCompraModel f = new FacturaCompraModel();
        when(facturaCompraRepository.findByCompra(c)).thenReturn(Optional.of(f));
        Optional<FacturaCompraModel> res = facturaCompraService.getFacturaByCompra(c);
        assertThat(res).isPresent();
        verify(facturaCompraRepository).findByCompra(c);
    }

    @Test
    void postAndGetByCompra_andDelete() {
        FacturaCompraModel f = new FacturaCompraModel();
        when(facturaCompraRepository.save(f)).thenReturn(f);
        FacturaCompraModel saved = facturaCompraService.postFactura(f);
        assertThat(saved).isSameAs(f);

        CompraModel c = new CompraModel();
        when(facturaCompraRepository.findByCompra(c)).thenReturn(Optional.of(f));
        Optional<FacturaCompraModel> found = facturaCompraService.getFacturaByCompra(c);
        assertThat(found).isPresent();

        doNothing().when(facturaCompraRepository).deleteById(1);
        boolean ok = facturaCompraService.deleteFactura(1);
        assertThat(ok).isTrue();

        doThrow(new RuntimeException()).when(facturaCompraRepository).deleteById(2);
        boolean nok = facturaCompraService.deleteFactura(2);
        assertThat(nok).isFalse();
    }

    @Test
    void putFacturaCompra_whenEntityExists_shouldReplaceAndReturnUpdated() {
        when(facturaCompraRepository.findById(1)).thenReturn(Optional.of(existing));
        when(facturaCompraRepository.save(any(FacturaCompraModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<FacturaCompraModel> result = facturaCompraService.putFacturaCompra(1, updated);

        assertThat(result).isPresent();
        FacturaCompraModel saved = result.get();

        assertThat(saved.getIdFacturaCompra()).isEqualTo(existing.getIdFacturaCompra());
        assertThat(saved.getMedioPago()).isEqualTo("TARJETA");
        assertThat(saved.getTotal()).isEqualTo(200);

        ArgumentCaptor<FacturaCompraModel> captor = ArgumentCaptor.forClass(FacturaCompraModel.class);
        verify(facturaCompraRepository, times(1)).save(captor.capture());
        FacturaCompraModel argToSave = captor.getValue();
        assertThat(argToSave.getIdFacturaCompra()).isEqualTo(existing.getIdFacturaCompra());
        assertThat(argToSave.getMedioPago()).isEqualTo("TARJETA");
    }

    @Test
    void putFacturaCompra_whenEntityNotFound_shouldReturnEmpty() {
        when(facturaCompraRepository.findById(2)).thenReturn(Optional.empty());

        Optional<FacturaCompraModel> result = facturaCompraService.putFacturaCompra(2, updated);

        assertThat(result).isNotPresent();
        verify(facturaCompraRepository, never()).save(any());
    }

    @Test
    void patchFacturaCompra_whenEntityExists_shouldCopyNonNullAndReturnUpdated() {
        when(facturaCompraRepository.findById(1)).thenReturn(Optional.of(existing));
        when(facturaCompraRepository.save(any(FacturaCompraModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<FacturaCompraModel> result = facturaCompraService.patchFacturaCompra(1, partial);

        assertThat(result).isPresent();
        FacturaCompraModel saved = result.get();

        assertThat(saved.getIdFacturaCompra()).isEqualTo(1);
        assertThat(saved.getMedioPago()).isEqualTo("TRANSFERENCIA");
        assertThat(saved.getTotal()).isEqualTo(100);

        verify(facturaCompraRepository, times(1)).save(any(FacturaCompraModel.class));
    }

    @Test
    void patchFacturaCompra_whenEntityNotFound_shouldReturnEmpty() {
        when(facturaCompraRepository.findById(99)).thenReturn(Optional.empty());

        Optional<FacturaCompraModel> result = facturaCompraService.patchFacturaCompra(99, partial);

        assertThat(result).isNotPresent();
        verify(facturaCompraRepository, never()).save(any());
    }

    private void copyNonNullProperties(Object src, Object target) {
        if (src == null || target == null) return;

        Class<?> srcClass = src.getClass();
        while (srcClass != null && srcClass != Object.class) {
            Field[] fields = srcClass.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();

                if ("id".equalsIgnoreCase(name) || "idFacturaCompra".equalsIgnoreCase(name)) {
                    continue;
                }

                field.setAccessible(true);
                try {
                    Object value = field.get(src);
                    if (value == null) continue;

                    Field targetField = findFieldInHierarchy(target.getClass(), name);
                    if (targetField == null) continue;

                    targetField.setAccessible(true);
                    targetField.set(target, value);

                } catch (IllegalAccessException ignored) {

                }
            }
            srcClass = srcClass.getSuperclass();
        }
    }

    private Field findFieldInHierarchy(Class<?> clazz, String name) {
        Class<?> cur = clazz;
        while (cur != null && cur != Object.class) {
            try {
                return cur.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                cur = cur.getSuperclass();
            }
        }
        return null;
    }
}
