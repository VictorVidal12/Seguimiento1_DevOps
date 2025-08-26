package com.example.demo.repositories;

import com.example.demo.models.CompraModel;
import com.example.demo.models.FacturaCompraModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class FacturaCompraRepositoryTest {

    @Autowired
    private FacturaCompraRepository facturaCompraRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void whenSaveFacturaAndFindByCompra_thenReturnFactura() {
        CompraModel c = new CompraModel();
        c.setTotal(200);
        c.setFechaCompra(java.time.LocalDateTime.now().withNano(0));
        c.setEstado("PAGADA");
        compraRepository.save(c);

        FacturaCompraModel f = new FacturaCompraModel();
        f.setMedioPago("TARJETA");
        f.setTotal(200);
        f.setCompra(c);
        facturaCompraRepository.save(f);

        Optional<FacturaCompraModel> found = facturaCompraRepository.findByCompra(c);
        assertThat(found).isPresent();
        assertThat(found.get().getMedioPago()).isEqualTo("TARJETA");
    }

    @Test
    void save_and_delete_factura_removes_entity() {
        CompraModel c = new CompraModel();
        c.setTotal(50);
        c.setFechaCompra(LocalDateTime.now().withNano(0));
        c.setEstado("CREADA");
        compraRepository.save(c);

        FacturaCompraModel f = new FacturaCompraModel();
        f.setMedioPago("EFECTIVO");
        f.setTotal(50);
        f.setCompra(c);
        facturaCompraRepository.save(f);

        Integer id = f.getIdFacturaCompra();
        assertThat(id).isNotNull();

        facturaCompraRepository.deleteById(id);
        Optional<FacturaCompraModel> found = facturaCompraRepository.findById(id);
        assertThat(found).isNotPresent();
    }

    @Test
    void findByCompra_returns_empty_when_none_exists() {
        CompraModel c = new CompraModel();
        c.setTotal(60);
        c.setFechaCompra(LocalDateTime.now().withNano(0));
        c.setEstado("PENDIENTE");
        compraRepository.save(c);

        Optional<FacturaCompraModel> found = facturaCompraRepository.findByCompra(c);
        assertThat(found).isNotPresent();
    }

    @Test
    void saving_factura_with_null_medioPago_throws() {
        FacturaCompraModel f = new FacturaCompraModel();
        f.setMedioPago(null);
        f.setTotal(10);

        assertThrows(Exception.class, () -> {
            em.persistAndFlush(f);
        });
    }
}
