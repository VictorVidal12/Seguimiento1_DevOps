package com.example.demo.repositories;

import com.example.demo.models.EmpennioModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class EmpennioRepositoryTest {

    @Autowired
    private EmpennioRepository empennioRepository;

    @Test
    void whenSaveAndFindByEstado_thenReturnList() {
        EmpennioModel e = new EmpennioModel();
        e.setPrecio(new BigDecimal("100.00"));
        e.setEstado("ACTIVO");
        e.setFechaInicio(LocalDateTime.now().withNano(0));
        e.setInteres(new BigDecimal("1.5"));

        empennioRepository.save(e);

        ArrayList<EmpennioModel> found = empennioRepository.findByEstado("ACTIVO");
        assertThat(found).isNotNull();
        assertThat(found).isNotEmpty();
        assertThat(found.getFirst().getEstado()).isEqualTo("ACTIVO");
    }
}
