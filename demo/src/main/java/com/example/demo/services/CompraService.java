package com.example.demo.services;

import com.example.demo.models.CompraModel;
import com.example.demo.repositories.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class CompraService {
    @Autowired
    CompraRepository compraRepository;

    public ArrayList<CompraModel> getCompras(){
        return (ArrayList<CompraModel>) compraRepository.findAll();
    }

    public CompraModel postCompra(CompraModel compra) {
        return compraRepository.save(compra);
    }

    public Optional<CompraModel> getCompraById(Integer id) {
        return compraRepository.findById(id);
    }

    public ArrayList<CompraModel> getCompraByFecha(LocalDateTime fechaCompra) {
        return compraRepository.findByFechaCompra(fechaCompra);
    }

    public boolean deleteCompra(Integer id) {
        try {
            compraRepository.deleteById(id);
            return true;
        } catch (Exception err) {
            return false;
        }
    }

}
