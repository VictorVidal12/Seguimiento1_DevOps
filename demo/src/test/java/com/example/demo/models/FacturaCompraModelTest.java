package com.example.demo.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FacturaCompraModelTest {

    @Test
    void gettersAndToString_includesCompraId_whenCompraNotNull() {
        FacturaCompraModel f = new FacturaCompraModel();
        f.setIdFacturaCompra(10);
        f.setMedioPago("EFECTIVO");
        f.setTotal(200);

        CompraModel c = new CompraModel();
        c.setIdcompra(99);
        f.setCompra(c);

        assertEquals(10, f.getIdFacturaCompra());
        assertEquals("EFECTIVO", f.getMedioPago());
        assertEquals(200, f.getTotal());
        assertSame(c, f.getCompra());

        String s = f.toString();
        assertTrue(s.contains("idFacturaCompra=10"));
        assertTrue(s.contains("medioPago='EFECTIVO'"));
        assertTrue(s.contains("compraId=99"));
    }

    @Test
    void equalsHashCode_nullCompra_andDifferentIds() {
        FacturaCompraModel f1 = new FacturaCompraModel();
        f1.setIdFacturaCompra(1);

        FacturaCompraModel f2 = new FacturaCompraModel();
        f2.setIdFacturaCompra(1);

        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());

        FacturaCompraModel f3 = new FacturaCompraModel();
        f3.setIdFacturaCompra(2);
        assertNotEquals(f1, f3);
    }
}
