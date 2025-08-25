package com.example.demo.controllers;

import com.example.demo.models.EmpennioModel;
import com.example.demo.services.EmpennioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/empennio")
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
