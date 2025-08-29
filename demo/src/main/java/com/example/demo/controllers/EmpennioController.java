package com.example.demo.controllers;

import com.example.demo.models.EmpennioModel;
import com.example.demo.services.EmpennioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/empennios")
public class EmpennioController {
    @Autowired
    EmpennioService empennioService;

    @GetMapping
    public ArrayList<EmpennioModel> getEmpennios () {
        return empennioService.getEmpennios();
    }

    @PostMapping
    public EmpennioModel postEmpennio (@RequestBody EmpennioModel empennio) {
        return empennioService.postEmpennio(empennio);
    }

    @GetMapping( path = "/{id}")
    public Optional<EmpennioModel> getEmpennioById(@PathVariable("id") Integer id) {
        return empennioService.getEmpennioById(id);
    }

    @GetMapping ("/query")
    public ArrayList<EmpennioModel> getEmpennioByEstado(@RequestParam("estado") String estado) {
        return empennioService.getEmpenniosByEstado(estado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpennioModel> putEmpennio( @PathVariable Integer id, @Valid @RequestBody EmpennioModel empennio) {
        return empennioService.putEmpennio(id, empennio)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmpennioModel> patchEmpennio (@PathVariable Integer id, @RequestBody EmpennioModel partial) {
        return empennioService.patchEmpennio(id, partial)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping ( path = "/{id}")
    public String deleteEmpennio(@PathVariable("id") Integer id) {
        boolean ok = this.empennioService.deleteEmpennio(id);
        if (ok) {
            return "Se eliminó el empeño con id " + id + " correctamente.";
        } else {
            return "No se pudo eliminar el empeño con id " + id + " porfavor, verifica si el id es correcto.";
        }
    }
}
