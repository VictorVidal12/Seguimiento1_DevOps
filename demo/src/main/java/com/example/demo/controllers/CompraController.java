package com.example.demo.controllers;

import com.example.demo.models.CompraModel;
import com.example.demo.services.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/compra")
public class CompraController {
    @Autowired
    CompraService compraService;

    @GetMapping
    public ArrayList<CompraModel> getCompras () {
        return compraService.getCompras();
    }

    @PostMapping
    public CompraModel createCompra (@RequestBody CompraModel compra) {
        return compraService.createCompra(compra);
    }

    @GetMapping( path = "/{id}")
    public Optional<CompraModel> getCompraById(@PathVariable("id") Integer id) {
        return compraService.getCompraById(id);
    }

    @GetMapping ("/query")
    public ArrayList<CompraModel> getCompraByPrioridad(@RequestParam("fecha") LocalDateTime fecha) {
        return compraService.getCompraByFecha(fecha);
    }

    @DeleteMapping ( path = "/{id}")
    public String deleteCompra(@PathVariable("id") Integer id) {
        boolean ok = this.compraService.deleteCompra(id);
        if (ok) {
            return "Se eliminó la compra con id " + id + " correctamente.";
        } else {
            return "No se pudo eliminar el usuario con id " + id + " porfavor, verifica si el id es correcto.";
        }
    }



}
