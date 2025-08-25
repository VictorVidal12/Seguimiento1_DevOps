package com.example.demo.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "facturacompraventa", schema = "mydb")
public class FacturaCompraModel {

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
    private CompraModel compra;

    public FacturaCompraModel() {

    }

    public FacturaCompraModel(String medioPago, Integer total, CompraModel compra) {
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


    public CompraModel getCompra() {
        return compra;
    }

    public void setCompra(CompraModel compra) {
        this.compra = compra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacturaCompraModel that)) return false;
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
