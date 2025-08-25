package com.example.demo.repositories;

import com.example.demo.models.CompraModel;
import com.example.demo.models.FacturaCompraModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaCompraRepository extends CrudRepository<FacturaCompraModel, Integer> {
    Optional<FacturaCompraModel> findByCompra(CompraModel compra);

}
