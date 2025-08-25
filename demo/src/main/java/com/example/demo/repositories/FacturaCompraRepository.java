package com.example.demo.repositories;

import com.example.demo.models.FacturaCompraModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaCompraRepository extends CrudRepository<FacturaCompraModel, Integer> {

}
