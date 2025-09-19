package com.example.demo.dto;

import com.example.demo.models.FacturaCompraModel;
import com.example.demo.models.CompraModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;

public class FacturaCompraDTO {

    @JsonProperty("idFacturaCompra")
    private Integer idFacturaCompra;

    @JsonProperty("medio_pago")
    private String medioPago;

    private Integer total;

    @JsonProperty("compra_idcompra")
    private Integer compraId;

    @JsonSetter("compra")
    public void unpackCompra(JsonNode compraNode) {
        if (compraNode != null && compraNode.has("idcompra") && !compraNode.get("idcompra").isNull()) {
            this.compraId = compraNode.get("idcompra").asInt();
        }
    }

    public FacturaCompraDTO() {}

    public Integer getIdFacturaCompra() { return idFacturaCompra; }
    public void setIdFacturaCompra(Integer idFacturaCompra) { this.idFacturaCompra = idFacturaCompra; }

    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }

    public Integer getCompraId() { return compraId; }
    public void setCompraId(Integer compraId) { this.compraId = compraId; }

    public FacturaCompraModel toEntity(CompraModel compraRelacionado) {
        return new FacturaCompraModel(
                this.medioPago,
                this.total,
                compraRelacionado
        );
    }

    public static FacturaCompraDTO fromEntity(FacturaCompraModel e) {
        if (e == null) return null;
        FacturaCompraDTO dto = new FacturaCompraDTO();
        dto.setIdFacturaCompra(e.getIdFacturaCompra());
        dto.setMedioPago(e.getMedioPago());
        dto.setTotal(e.getTotal());
        if (e.getCompra() != null) dto.setCompraId(e.getCompra().getIdcompra());
        return dto;
    }
}
