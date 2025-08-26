package com.example.demo.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class CompraModelTest {

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
        // Comprobaciones tolerantes al formato
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
}
