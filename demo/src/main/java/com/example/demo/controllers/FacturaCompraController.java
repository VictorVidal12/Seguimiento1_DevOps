package com.example.demo.controllers;

import com.example.demo.models.CompraModel;
import com.example.demo.models.FacturaCompraModel;
import com.example.demo.services.CompraService;
import com.example.demo.services.FacturaCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/facturas")
public class FacturaCompraController {
    @Autowired
    FacturaCompraService facturaCompraService;

    @Autowired
    CompraService compraService;

    @GetMapping
    public ArrayList<FacturaCompraModel> getFacturas () {
        return facturaCompraService.getFacturas();
    }

    @GetMapping( path = "/{id}")
    public Optional<FacturaCompraModel> getFacturaById (@PathVariable("id") Integer id) {
        return facturaCompraService.getFacturaById(id);
    }

    @GetMapping ("/query")
    public Optional<FacturaCompraModel> getFacturaByIdCompra (@RequestParam("compra_idcompra") Integer compraId) {
        Optional<CompraModel> compra = compraService.getCompraById(compraId);
        if (compra.isPresent()) {
            return facturaCompraService.getFacturaByCompra(compra.get());
        } else {
            return Optional.empty();
        }
    }


    @PostMapping
    public FacturaCompraModel postFactura (@RequestBody FacturaCompraModel factura) {
        return facturaCompraService.postFactura(factura);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacturaCompraModel> putFacturaCompra( @PathVariable Integer id, @Valid @RequestBody FacturaCompraModel factura) {
        return facturaCompraService.putFacturaCompra(id, factura)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FacturaCompraModel> patchFacturaCompra (@PathVariable Integer id, @RequestBody FacturaCompraModel partial) {
        return facturaCompraService.patchFacturaCompra(id, partial)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping ( path = "/{id}")
    public String deleteFactura (@PathVariable("id") Integer id) {
        boolean ok = facturaCompraService.deleteFactura(id);
        if (ok) {
            return "Se ha eliminado la factura con id " + id + "correctamente.";
        } else {
            return "No se ha encontrado factura con id " + id + ", por favor, revisa el id.";
        }
    }
}
