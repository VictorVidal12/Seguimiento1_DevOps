package com.example.demo.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "empennio", schema = "mydb")
public class EmpennioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idempennio", nullable = false)
    private Integer idempennio;

    @Column(name = "precio", nullable = false)
    private BigDecimal precio;

    @Column(name = "estado", length = 50, nullable = false)
    private String estado;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_final")
    private LocalDateTime fechaFinal;

    @Column(name = "interes", nullable = false)
    private BigDecimal interes;

    public EmpennioModel() {
    }

    public EmpennioModel(BigDecimal precio, String estado, LocalDateTime fechaInicio, LocalDateTime fechaFinal, BigDecimal interes) {
        this.precio = precio;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.interes = interes;
    }

    // Getters y setters
    public Integer getIdempennio() {
        return idempennio;
    }

    public void setIdempennio(Integer idempennio) {
        this.idempennio = idempennio;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(LocalDateTime fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmpennioModel empennio)) return false;
        return Objects.equals(idempennio, empennio.idempennio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idempennio);
    }

    @Override
    public String toString() {
        return "Empennio{" +
                "idempennio=" + idempennio +
                ", precio=" + precio +
                ", estado='" + estado + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFinal=" + fechaFinal +
                ", interes=" + interes +
                '}';
    }
}
