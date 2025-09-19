package com.example.demo.controllers;

import com.example.demo.models.CompraModel;
import com.example.demo.services.CompraService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import jakarta.validation.Valid;
import com.example.demo.services.FacturaCompraService;
import com.example.demo.dto.FacturaCompraDTO;
import com.example.demo.models.FacturaCompraModel;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/compras")
public class CompraController {
    @Autowired
    private CompraService compraService;

    @Autowired
    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();
    private final DateTimeFormatter isoDate = DateTimeFormatter.ISO_DATE;
    private final DateTimeFormatter isoDateTime = DateTimeFormatter.ISO_DATE_TIME;

    @Value("${external.facturas.url:https://demo-276672580331.us-central1.run.app/facturas}")
    private String externalFacturasUrl;

    @Autowired
    private FacturaCompraService facturaCompraService;

    @GetMapping
    public ArrayList<CompraModel> getCompras () {
        return compraService.getCompras();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postCompra(@RequestBody JsonNode payload) {
        ObjectNode result = objectMapper.createObjectNode();

        try {
            JsonNode compraNode = payload.path("compra");
            if (compraNode.isMissingNode() || compraNode.isNull()) {
                return ResponseEntity.badRequest()
                        .body(objectMapper.createObjectNode().put("error", "Falta nodo 'compra' en el JSON"));
            }

            Integer total = compraNode.has("total") && !compraNode.path("total").isNull()
                    ? compraNode.path("total").asInt()
                    : null;
            String fechaStr = compraNode.has("fecha_compra") && !compraNode.path("fecha_compra").isNull()
                    ? compraNode.path("fecha_compra").asText()
                    : null;
            String estado = compraNode.has("estado") && !compraNode.path("estado").isNull()
                    ? compraNode.path("estado").asText()
                    : null;

            LocalDateTime fechaCompra = parseToLocalDateTime(fechaStr);

            CompraModel compraToSave = new CompraModel(total, fechaCompra, estado);
            CompraModel savedCompra = compraService.postCompra(compraToSave);

            result.set("compraCreada", objectMapper.valueToTree(savedCompra));

            ObjectNode externalInfo = objectMapper.createObjectNode();
            JsonNode facturaNode = payload.path("factura");

            if (facturaNode != null && facturaNode.isObject()) {
                try {
                    FacturaCompraDTO facturaDto = objectMapper.treeToValue(facturaNode, FacturaCompraDTO.class);

                    facturaDto.setCompraId(savedCompra.getIdcompra());

                    if (facturaDto.getMedioPago() == null || facturaDto.getMedioPago().isBlank()) {
                        ObjectNode err = objectMapper.createObjectNode();
                        err.put("message", "Falta campo requerido 'medio_pago' dentro de 'factura'.");
                        err.set("compraCreada", result.get("compraCreada"));
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
                    }

                    FacturaCompraModel entidad = facturaDto.toEntity(savedCompra);
                    FacturaCompraModel savedFactura = facturaCompraService.postFactura(entidad);

                    externalInfo.put("statusCode", HttpStatus.CREATED.value());
                    externalInfo.set("body", objectMapper.valueToTree(FacturaCompraDTO.fromEntity(savedFactura)));

                } catch (Exception ex) {
                    externalInfo.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    ObjectNode errNode = objectMapper.createObjectNode();
                    errNode.put("message", "Error interno al crear la factura localmente");
                    errNode.put("detail", ex.getMessage());
                    externalInfo.set("body", errNode);
                }
            } else {
                externalInfo.put("statusCode", HttpStatus.NO_CONTENT.value());
                externalInfo.putNull("body");
            }

            result.set("respuestaFactura", externalInfo);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (Exception e) {
            ObjectNode err = objectMapper.createObjectNode();
            err.put("message", "Error interno procesando la compra");
            err.put("detail", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

    @GetMapping( path = "/{id}")
    public Optional<CompraModel> getCompraById(@PathVariable("id") Integer id) {
        return compraService.getCompraById(id);
    }

    @GetMapping ("/query")
    public ArrayList<CompraModel> getCompraByFecha(@RequestParam("fechaCompra") String fechaCompraStr) {
        LocalDateTime fechaCompra = parseToLocalDateTime(fechaCompraStr);
        return compraService.getCompraByFecha(fechaCompra);
    }

    @DeleteMapping ( path = "/{id}")
    public String deleteCompra(@PathVariable("id") Integer id) {
        boolean ok = this.compraService.deleteCompra(id);
        if (ok) {
            return "Se eliminó la compra con id " + id + " correctamente.";
        } else {
            return "No se pudo eliminar la compra con id " + id + " porfavor, verifica si el id es correcto.";
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraModel> putCompra(@PathVariable Integer id, @RequestBody CompraModel compra) {
        return compraService.putCompra(id, compra)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CompraModel> patchCompra(@PathVariable Integer id, @RequestBody CompraModel partial) {
        return compraService.patchCompra(id, partial)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private LocalDateTime parseToLocalDateTime(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return LocalDateTime.parse(s, isoDateTime);
        } catch (Exception ignored) { }
        try {
            LocalDate d = LocalDate.parse(s, isoDate);
            return d.atStartOfDay();
        } catch (Exception ignored) { }
        return null;
    }
}
