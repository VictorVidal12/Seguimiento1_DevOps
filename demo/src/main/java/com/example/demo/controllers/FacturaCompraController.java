package com.example.demo.controllers;

import com.example.demo.models.CompraModel;
import com.example.demo.models.FacturaCompraModel;
import com.example.demo.services.FacturaCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/facturacompra")
public class FacturaCompraController {
    @Autowired
    FacturaCompraService facturaCompraService;

    @GetMapping
    public ArrayList<FacturaCompraModel> getFacturas () {
        return facturaCompraService.getFacturas();
    }

    @GetMapping( path = "/{id}")
    public Optional<FacturaCompraModel> getFacturaById (@PathVariable("id") Integer id) {
        return facturaCompraService.getFacturaById(id);
    }

    @GetMapping ("/query")
    public Optional<FacturaCompraModel> getFacturaByIdCompra (@RequestParam("compra_idcompra") CompraModel compra) {
        return facturaCompraService.getFacturaByCompra(compra);
    }

    @PostMapping
    public FacturaCompraModel postFactura (@RequestBody FacturaCompraModel factura) {
        return facturaCompraService.postFactura(factura);
    }

    @DeleteMapping ( path = "/{id}")
    public String deleteFactura (@RequestParam("id") Integer id) {
        boolean ok = facturaCompraService.deleteFactura(id);
        if (ok) {
            return "Se ha eliminado la factura con id " + id + "correctamente";
        } else {
            return "No se ha encontrado factura con id " + id + ", por favor, revisa el id.";
        }
    }


}
