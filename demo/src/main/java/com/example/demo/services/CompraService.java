package com.example.demo.services;

import com.example.demo.models.CompraModel;
import com.example.demo.repositories.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CompraService {
    @Autowired
    CompraRepository compraRepository;

    public ArrayList<CompraModel> getCompras(){
        return (ArrayList<CompraModel>) compraRepository.findAll();
    }

    public CompraModel createCompra(CompraModel compra) {
        return compraRepository.save(compra);
    }

}
