package com.example.demo.repositories;

import com.example.demo.models.CompraModel;
import com.example.demo.models.EmpennioModel;
import com.example.demo.models.FacturaCompraModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface FacturaCompraRepository extends CrudRepository<FacturaCompraModel, Integer> {
    ArrayList<EmpennioModel> findByCompra(CompraModel compra);

}
