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
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EmpennioServiceTest {

    @Mock
    private EmpennioRepository empennioRepository;

    @InjectMocks
    private EmpennioService empennioService;

    public static class EmpennioBean {
        private Integer id;
        private String descripcion;
        private Double monto;
        private Boolean activo;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        public Double getMonto() { return monto; }
        public void setMonto(Double monto) { this.monto = monto; }

        public Boolean getActivo() { return activo; }
        public void setActivo(Boolean activo) { this.activo = activo; }
    }

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


    @Test
    void copyNonNullProperties_empennio_shouldCopyNonNullAndIgnoreNullsAndId() throws Exception {
        EmpennioBean src = new EmpennioBean();
        src.setId(999);
        src.setDescripcion("Nuevo empeño");
        src.setMonto(null);
        src.setActivo(Boolean.TRUE);

        EmpennioBean target = new EmpennioBean();
        target.setId(1);
        target.setDescripcion("Antiguo empeño");
        target.setMonto(123.45);
        target.setActivo(Boolean.FALSE);

        EmpennioService service = new EmpennioService();
        Method m = EmpennioService.class.getDeclaredMethod("copyNonNullProperties", Object.class, Object.class);
        m.setAccessible(true);

        m.invoke(service, src, target);

        assertThat(target.getDescripcion()).isEqualTo("Nuevo empeño");
        assertThat(target.getActivo()).isTrue();
        assertThat(target.getMonto()).isEqualTo(123.45);
        assertThat(target.getId()).isEqualTo(1);
    }

    @Test
    void copyNonNullProperties_empennio_allNullSource_shouldNotModifyTarget() throws Exception {
        EmpennioBean src = new EmpennioBean();
        src.setId(500);
        src.setDescripcion(null);
        src.setMonto(null);
        src.setActivo(null);

        EmpennioBean target = new EmpennioBean();
        target.setId(2);
        target.setDescripcion("Original");
        target.setMonto(10.0);
        target.setActivo(Boolean.FALSE);

        EmpennioService service = new EmpennioService();
        Method m = EmpennioService.class.getDeclaredMethod("copyNonNullProperties", Object.class, Object.class);
        m.setAccessible(true);

        m.invoke(service, src, target);

        assertThat(target.getDescripcion()).isEqualTo("Original");
        assertThat(target.getMonto()).isEqualTo(10.0);
        assertThat(target.getActivo()).isFalse();
        assertThat(target.getId()).isEqualTo(2);
    }

    /* --------------------------
       Tests añadidos: put & patch
       -------------------------- */

    @Test
    void putEmpennio_existing_shouldSetIdAndSave() {
        // Arrange
        Integer id = 10;
        EmpennioModel existing = new EmpennioModel();
        existing.setIdempennio(id);
        existing.setEstado("OLD");
        existing.setPrecio(null);

        EmpennioModel incoming = new EmpennioModel();
        incoming.setEstado("NEW");
        incoming.setPrecio(null); // other fields can be null

        when(empennioRepository.findById(id)).thenReturn(Optional.of(existing));
        when(empennioRepository.save(any(EmpennioModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<EmpennioModel> resultOpt = empennioService.putEmpennio(id, incoming);

        // Assert
        assertThat(resultOpt).isPresent();
        EmpennioModel saved = resultOpt.get();

        // incoming reference should have been assigned the existing id before saving
        assertThat(incoming.getIdempennio()).isEqualTo(id);
        assertThat(saved.getIdempennio()).isEqualTo(id);
        assertThat(saved.getEstado()).isEqualTo("NEW");

        verify(empennioRepository, times(1)).findById(id);
        verify(empennioRepository, times(1)).save(incoming);
    }

    @Test
    void putEmpennio_notFound_shouldReturnEmptyAndNotSave() {
        Integer id = 99;
        EmpennioModel incoming = new EmpennioModel();
        incoming.setEstado("NEW");

        when(empennioRepository.findById(id)).thenReturn(Optional.empty());

        Optional<EmpennioModel> res = empennioService.putEmpennio(id, incoming);

        assertThat(res).isNotPresent();
        verify(empennioRepository, times(1)).findById(id);
        verify(empennioRepository, never()).save(any());
    }

    @Test
    void patchEmpennio_existing_shouldCopyNonNullAndSave() {
        Integer id = 5;
        EmpennioModel existing = new EmpennioModel();
        existing.setIdempennio(id);
        existing.setEstado("OLD_STATE");
        existing.setPrecio(new java.math.BigDecimal("100.00"));
        existing.setInteres(new java.math.BigDecimal("1.0"));

        // partial: only estado is non-null -> should update estado but keep precio/interes
        EmpennioModel partial = new EmpennioModel();
        partial.setEstado("PATCHED_STATE");
        partial.setPrecio(null);
        partial.setInteres(null);

        when(empennioRepository.findById(id)).thenReturn(Optional.of(existing));
        when(empennioRepository.save(any(EmpennioModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<EmpennioModel> resultOpt = empennioService.patchEmpennio(id, partial);

        assertThat(resultOpt).isPresent();
        EmpennioModel saved = resultOpt.get();

        // existing instance should have been modified: estado updated, precio unchanged
        assertThat(saved.getEstado()).isEqualTo("PATCHED_STATE");
        assertThat(saved.getPrecio()).isEqualTo(new java.math.BigDecimal("100.00"));
        assertThat(saved.getIdempennio()).isEqualTo(id);

        verify(empennioRepository, times(1)).findById(id);
        verify(empennioRepository, times(1)).save(existing); // saved object is the existing one modified
    }

    @Test
    void patchEmpennio_notFound_shouldReturnEmptyAndNotSave() {
        Integer id = 1234;
        EmpennioModel partial = new EmpennioModel();
        partial.setEstado("X");

        when(empennioRepository.findById(id)).thenReturn(Optional.empty());

        Optional<EmpennioModel> res = empennioService.patchEmpennio(id, partial);

        assertThat(res).isNotPresent();
        verify(empennioRepository, times(1)).findById(id);
        verify(empennioRepository, never()).save(any());
    }
}
