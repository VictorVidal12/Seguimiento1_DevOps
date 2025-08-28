package com.example.demo.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FacturaCompraModelTest {
    @Test
    void constructorShouldSetAllFields() {
        CompraModel compra = new CompraModel();
        compra.setIdcompra(1);

        FacturaCompraModel factura = new FacturaCompraModel("EFECTIVO", 1500, compra);

        assertAll("Constructor asigna correctamente los campos",
                () -> assertEquals("EFECTIVO", factura.getMedioPago(), "medioPago debe coincidir"),
                () -> assertEquals(1500, factura.getTotal(), "total debe coincidir"),
                () -> assertSame(compra, factura.getCompra(), "compra debe ser la misma referencia pasada al constructor")
        );
    }

    @Test
    void constructorShouldAcceptNulls() {
        FacturaCompraModel factura = new FacturaCompraModel(null, null, null);

        assertAll("Constructor acepta valores null",
                () -> assertNull(factura.getMedioPago(), "medioPago debe ser null"),
                () -> assertNull(factura.getTotal(), "total debe ser null"),
                () -> assertNull(factura.getCompra(), "compra debe ser null")
        );
    }


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

    @Test
    void equals_isReflexive_andNotEqualToNullOrOtherClass() {
        FacturaCompraModel a = new FacturaCompraModel();
        a.setIdFacturaCompra(5);
        assertEquals(a, a);
        assertNotEquals(null, a);
        assertNotEquals(new Object(), a);
    }

    @Test
    void equals_andHashCode_whenBothIdsNull_shouldBeEqualAndHashEqual() {
        FacturaCompraModel x = new FacturaCompraModel();
        FacturaCompraModel y = new FacturaCompraModel();

        // neither has id set (null)
        assertNull(x.getIdFacturaCompra());
        assertNull(y.getIdFacturaCompra());
        assertEquals(x, y, "Dos instancias con id null deberían ser iguales si equals compara por id con Objects.equals");
        assertEquals(x.hashCode(), y.hashCode(), "hashCode debe ser consistente entre iguales");
    }

    @Test
    void toString_handlesNullCompraWithoutThrowing_andContainsFields() {
        FacturaCompraModel f = new FacturaCompraModel();
        f.setIdFacturaCompra(20);
        f.setMedioPago("TARJETA");
        f.setTotal(500);
        f.setCompra(null);

        String s = f.toString();
        assertNotNull(s, "toString() no debe devolver null");
        assertTrue(s.contains("20") || s.contains("idFacturaCompra"), "toString debe contener id");
        assertTrue(s.toLowerCase().contains("tarjeta") || s.contains("medioPago"), "toString debe contener medioPago");
        assertTrue(s.contains("500") || s.contains("total"), "toString debe contener total");
    }
}
