package com.example.demo.services;

import com.example.demo.models.CompraModel;
import com.example.demo.models.FacturaCompraModel;
import com.example.demo.repositories.FacturaCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class FacturaCompraService {
    @Autowired
    FacturaCompraRepository facturaCompraRepository;

    public ArrayList<FacturaCompraModel> getFacturas() {
        return (ArrayList<FacturaCompraModel>) facturaCompraRepository.findAll();
    }

    public Optional<FacturaCompraModel> getFacturaById(Integer id) {
        return facturaCompraRepository.findById(id);
    }

    public Optional<FacturaCompraModel> getFacturaByCompra(CompraModel compra) {
        return facturaCompraRepository.findByCompra(compra);
    }

    public FacturaCompraModel postFactura (FacturaCompraModel factura) {
        return facturaCompraRepository.save(factura);
    }

    public boolean deleteFactura (Integer id) {
        try {
            facturaCompraRepository.deleteById(id);
            return true;
        } catch (Exception err) {
            return false;
        }
    }

}
