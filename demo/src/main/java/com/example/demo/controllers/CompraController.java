package com.example.demo.controllers;

import com.example.demo.models.CompraModel;
import com.example.demo.services.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
}
