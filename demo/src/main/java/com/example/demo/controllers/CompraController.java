package com.example.demo.controllers;

import com.example.demo.models.CompraModel;
import com.example.demo.services.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/compras")
public class CompraController {
    @Autowired
    CompraService compraService;

    @GetMapping
    public ArrayList<CompraModel> getCompras () {
        return compraService.getCompras();
    }

    @PostMapping
    public CompraModel postCompra (@RequestBody CompraModel compra) {
        return compraService.postCompra(compra);
    }

    @GetMapping( path = "/{id}")
    public Optional<CompraModel> getCompraById(@PathVariable("id") Integer id) {
        return compraService.getCompraById(id);
    }

    @GetMapping ("/query")
    public ArrayList<CompraModel> getCompraByFecha(@RequestParam("fechaCompra") LocalDateTime fechaCompra) {
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

}
