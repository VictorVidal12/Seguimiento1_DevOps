package com.example.demo.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class CompraModelTest {

    @Test
    void constructorShouldSetAllFields() {
        LocalDateTime fecha = LocalDateTime.of(2024, 8, 25, 15, 30).withNano(0);
        Integer total = 150;
        String estado = "CREADA";
        CompraModel c = new CompraModel(total, fecha, estado);

        assertNull(c.getIdcompra(), "idcompra no debe estar inicializado por ese constructor");
        assertEquals(total, c.getTotal(), "total debe coincidir con el pasado al constructor");
        assertEquals(fecha, c.getFechaCompra(), "fechaCompra debe coincidir con la pasada al constructor");
        assertEquals(estado, c.getEstado(), "estado debe coincidir con la pasada al constructor");
    }

    @Test
    void constructorShouldAcceptNulls() {
        CompraModel c = new CompraModel(null, null, null);

        assertNull(c.getIdcompra(), "idcompra debe ser null");
        assertNull(c.getTotal(), "total debe ser null");
        assertNull(c.getFechaCompra(), "fechaCompra debe ser null");
        assertNull(c.getEstado(), "estado debe ser null");

        String s = c.toString();
        assertNotNull(s, "toString no debe devolver null aunque los campos sean null");
    }

    @Test
    void toString_includesFields_whenValuesPresent() {
        LocalDateTime fecha = LocalDateTime.of(2024, 12, 31, 23, 59).withNano(0);
        CompraModel c = new CompraModel(999, fecha, "PAGADA");

        String s = c.toString() == null ? "" : c.toString();

        assertTrue(s.contains("999") || s.toLowerCase().contains("total"), "toString debe incluir el total");
        assertTrue(s.contains("PAGADA") || s.toLowerCase().contains("estado"), "toString debe incluir el estado");
        assertTrue(s.contains("2024") || s.contains("12") || s.contains("23:59"), "toString debe incluir la fecha (o parte de ella)");
    }

    @Test
    void equals_andHashCode_reflexive_andHandlesNullId() {
        CompraModel a = new CompraModel(10, LocalDateTime.now().withNano(0), "CREADA");
        assertEquals(a, a, "equals debe ser true para la misma referencia");

        int h = a.hashCode();
        assertEquals(h, a.hashCode(), "hashCode debe ser estable entre llamadas consecutivas");
    }



    @Test
    void gettersAndSetters_andToString() {
        CompraModel c = new CompraModel();
        c.setIdcompra(1);
        c.setTotal(150);
        LocalDateTime now = LocalDateTime.now();
        c.setFechaCompra(now);
        c.setEstado("PAGADO");

        assertEquals(Integer.valueOf(1), c.getIdcompra());

        assertEquals(Integer.valueOf(150), c.getTotal());

        assertEquals(now, c.getFechaCompra());
        assertEquals("PAGADO", c.getEstado());

        String s = c.toString() == null ? "" : c.toString();
        assertTrue(s.contains("1") || s.toLowerCase().contains("id"));
        assertTrue(s.contains("150") || s.toLowerCase().contains("total"));
        assertTrue(s.contains("PAGADO"));
    }

    @Test
    void equalsAndHashCode_basedOnId() {
        CompraModel a = new CompraModel();
        a.setIdcompra(5);
        a.setEstado("X");

        CompraModel b = new CompraModel();
        b.setIdcompra(5);
        b.setEstado("Y");

        assertEquals(a, b, "Entities with same id should be equal (if equals implemented by id)");
        assertEquals(a.hashCode(), b.hashCode());

        CompraModel c = new CompraModel();
        c.setIdcompra((6));
        assertNotEquals(a, c);

        assertNotEquals(null, a);
        assertNotEquals(new Object(), a);
    }

    @Test
    void equals_returnsFalse_whenComparedWithDifferentClass() {
        CompraModel a = new CompraModel(10, LocalDateTime.now().withNano(0), "CREADA");
        assertNotEquals("una cadena", a, "equals debe devolver false si el objeto comparado no es CompraModel");
        assertNotEquals(new Object(), a, "equals debe devolver false si el objeto comparado es otra clase");
    }

    @Test
    void equals_returnsFalse_whenComparedWithNull() {
        CompraModel a = new CompraModel(10, LocalDateTime.now().withNano(0), "CREADA");
        assertNotEquals(null, a, "equals debe devolver false al comparar con null");
    }
}
