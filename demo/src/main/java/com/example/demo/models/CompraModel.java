package com.example.demo.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "compra", schema = "mydb")
public class CompraModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcompra", nullable = false)
    private Integer idcompra;

    @Column(name = "total", nullable = false)
    private Integer total;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;

    @Column(name = "estado", length = 30, nullable = false)
    private String estado;

    public CompraModel() {
        // JPA
    }

    public CompraModel(Integer total, LocalDateTime fechaCompra, String estado) {
        this.total = total;
        this.fechaCompra = fechaCompra;
        this.estado = estado;
    }

    // Getters y setters
    public Integer getIdcompra() {
        return idcompra;
    }

    public void setIdcompra(Integer idcompra) {
        this.idcompra = idcompra;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompraModel compra)) return false;
        return Objects.equals(idcompra, compra.idcompra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idcompra);
    }

    @Override
    public String toString() {
        return "Compra{" +
                "idcompra=" + idcompra +
                ", total=" + total +
                ", fechaCompra=" + fechaCompra +
                ", estado='" + estado + '\'' +
                '}';
    }
}
