package com.example.demo.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EmpennioModelTest {

    @Test
    void basicGettersSettersAndToString() {
        EmpennioModel e = new EmpennioModel();

        e.setIdempennio(7);

        e.setPrecio(new BigDecimal("123.45"));

        e.setEstado("ACTIVO");

        // Las fechas requieren LocalDateTime según la captura
        LocalDateTime inicio = LocalDateTime.of(2024, 1, 1, 9, 30);
        LocalDateTime fin    = LocalDateTime.of(2024, 12, 31, 18, 0);
        e.setFechaInicio(inicio);
        e.setFechaFinal(fin);


        e.setInteres(new BigDecimal("8.5"));

        // Asserts
        assertEquals(Integer.valueOf(7), e.getIdempennio());

        // Para BigDecimal usar compareTo
        assertEquals(0, e.getPrecio().compareTo(new BigDecimal("123.45")),
                "El precio debe ser 123.45");

        assertEquals("ACTIVO", e.getEstado());
        assertEquals(inicio, e.getFechaInicio());
        assertEquals(fin, e.getFechaFinal());

        String s = e.toString() == null ? "" : e.toString();
        assertTrue(s.contains("id") || s.contains("idempennio") || s.contains("7"));
        assertTrue(s.contains("123") || s.contains("123.45"));
        assertTrue(s.toLowerCase().contains("activo"));
    }

    @Test
    void equalsHashCode_consistency() {
        EmpennioModel a = new EmpennioModel();
        a.setIdempennio(2);

        EmpennioModel b = new EmpennioModel();
        b.setIdempennio(2);

        // Si la entidad implementa equals por id, estos deben ser iguales:
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
