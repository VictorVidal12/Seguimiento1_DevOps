package com.example.demo.controllers;

import com.example.demo.dto.FacturaCompraDTO;
import com.example.demo.models.CompraModel;
import com.example.demo.models.FacturaCompraModel;
import com.example.demo.services.CompraService;
import com.example.demo.services.FacturaCompraService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/facturas")
public class FacturaCompraController {

    @Autowired
    private FacturaCompraService facturaCompraService;

    @Autowired
    private CompraService compraService;

    @GetMapping
    public ResponseEntity<List<FacturaCompraDTO>> getFacturas() {
        try {
            List<FacturaCompraModel> entidades = facturaCompraService.getFacturas();
            List<FacturaCompraDTO> dtos = entidades.stream()
                    .map(FacturaCompraDTO::fromEntity)
                    .collect(Collectors.toCollection(ArrayList::new));
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            ObjectNode err = com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.objectNode();
            err.put("message", "Error interno al recuperar facturas");
            err.put("detail", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFacturaById(@PathVariable("id") Integer id) {
        try {
            Optional<FacturaCompraModel> opt = facturaCompraService.getFacturaById(id);
            if (opt.isPresent()) {
                return ResponseEntity.ok(FacturaCompraDTO.fromEntity(opt.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(java.util.Map.of("error", "Factura con id " + id + " no encontrada."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("message", "Error interno al recuperar factura", "detail", e.getMessage()));
        }
    }

    @GetMapping("/query")
    public ResponseEntity<?> getFacturaByIdCompra(@RequestParam("compra_idcompra") Integer compraId) {
        try {
            Optional<CompraModel> compra = compraService.getCompraById(compraId);
            if (compra.isPresent()) {
                Optional<FacturaCompraModel> factOpt = facturaCompraService.getFacturaByCompra(compra.get());
                if (factOpt.isPresent()) {
                    return ResponseEntity.ok(FacturaCompraDTO.fromEntity(factOpt.get()));
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(java.util.Map.of("error", "No se encontró factura para la compra id " + compraId));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(java.util.Map.of("error", "Compra con id " + compraId + " no encontrada."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("message", "Error interno al consultar factura", "detail", e.getMessage()));
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> postFactura(@RequestBody FacturaCompraDTO dto) {
        try {
            Integer compraId = dto.getCompraId();
            if (compraId == null) {
                return ResponseEntity.badRequest()
                        .body(java.util.Map.of("error", "Falta 'compra_idcompra' o 'compra.idcompra' en el JSON"));
            }

            Optional<CompraModel> compOpt = compraService.getCompraById(compraId);
            if (compOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(java.util.Map.of("error", "Compra con id " + compraId + " no encontrada."));
            }

            FacturaCompraModel entidad = dto.toEntity(compOpt.get());
            FacturaCompraModel saved = facturaCompraService.postFactura(entidad);

            return ResponseEntity.status(HttpStatus.CREATED).body(FacturaCompraDTO.fromEntity(saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("message", "Error interno al procesar la factura", "detail", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putFacturaCompra(@PathVariable Integer id, @Valid @RequestBody FacturaCompraDTO dto) {
        try {
            Integer compraId = dto.getCompraId();
            if (compraId == null) {
                return ResponseEntity.badRequest()
                        .body(java.util.Map.of("error", "Falta 'compra_idcompra' o 'compra.idcompra' en el JSON"));
            }

            Optional<CompraModel> compOpt = compraService.getCompraById(compraId);
            if (compOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(java.util.Map.of("error", "Compra con id " + compraId + " no encontrada."));
            }

            FacturaCompraModel entidad = dto.toEntity(compOpt.get());

            return facturaCompraService.putFacturaCompra(id, entidad)
                    .map(updated -> ResponseEntity.ok(FacturaCompraDTO.fromEntity(updated)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("message", "Error interno al actualizar la factura", "detail", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchFacturaCompra(@PathVariable Integer id, @RequestBody FacturaCompraDTO dto) {
        try {
            CompraModel compraRelacionado = null;
            if (dto.getCompraId() != null) {
                Optional<CompraModel> compOpt = compraService.getCompraById(dto.getCompraId());
                if (compOpt.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(java.util.Map.of("error", "Compra con id " + dto.getCompraId() + " no encontrada."));
                }
                compraRelacionado = compOpt.get();
            }

            FacturaCompraModel partial = new FacturaCompraModel();
            partial.setMedioPago(dto.getMedioPago());
            partial.setTotal(dto.getTotal());
            if (compraRelacionado != null) partial.setCompra(compraRelacionado);

            return facturaCompraService.patchFacturaCompra(id, partial)
                    .map(updated -> ResponseEntity.ok(FacturaCompraDTO.fromEntity(updated)))
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("message", "Error interno al aplicar patch a la factura", "detail", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFactura(@PathVariable("id") Integer id) {
        try {
            boolean ok = facturaCompraService.deleteFactura(id);
            if (ok) {
                return ResponseEntity.ok(java.util.Map.of("message", "Se ha eliminado la factura con id " + id + " correctamente."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(java.util.Map.of("error", "No se ha encontrado factura con id " + id + ", por favor, revisa el id."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("message", "Error interno al eliminar factura", "detail", e.getMessage()));
        }
    }
}
