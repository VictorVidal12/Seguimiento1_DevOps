package com.example.demo.repositories;

import com.example.demo.models.CompraModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CompraRepositoryTest {

    @Autowired
    private CompraRepository compraRepository;

    @Test
    void whenSaveAndFindByFechaCompra_thenReturnList() {
        CompraModel c = new CompraModel();
        c.setTotal(100);
        c.setFechaCompra(LocalDateTime.now().withNano(0));
        c.setEstado("CREADA");

        compraRepository.save(c);

        ArrayList<CompraModel> found = compraRepository.findByFechaCompra(c.getFechaCompra());
        assertThat(found).isNotNull();
        assertThat(found).isNotEmpty();
        assertThat(found.getFirst().getTotal()).isEqualTo(100);
    }
}
