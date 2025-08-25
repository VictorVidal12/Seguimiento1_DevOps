package com.example.demo.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "facturacompraventa", schema = "mydb")
public class FacturaCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFacturaCompra", nullable = false)
    private Integer idFacturaCompra;

    @Column(name = "medio_pago", length = 45, nullable = false)
    private String medioPago;

    @Column(name = "total", nullable = false)
    private Integer total;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_idcompra", nullable = false, referencedColumnName = "idcompra")
    private Compra compra;

    public FacturaCompra() {

    }

    public FacturaCompra(String medioPago, Integer total, Compra compra) {
        this.medioPago = medioPago;
        this.total = total;
        this.compra = compra;
    }

    // Getters y setters
    public Integer getIdFacturaCompra() {
        return idFacturaCompra;
    }

    public void setIdFacturaCompra(Integer idFacturaCompra) {
        this.idFacturaCompra = idFacturaCompra;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }


    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacturaCompra that)) return false;
        return Objects.equals(idFacturaCompra, that.idFacturaCompra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFacturaCompra);
    }

    @Override
    public String toString() {
        return "FacturaCompra{" +
                "idFacturaCompra=" + idFacturaCompra +
                ", medioPago='" + medioPago + '\'' +
                ", total=" + total +
                ", compraId=" + (compra != null ? compra.getIdcompra() : null) +
                '}';
    }
}
