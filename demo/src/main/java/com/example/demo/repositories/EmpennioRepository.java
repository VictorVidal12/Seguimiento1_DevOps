package com.example.demo.repositories;

import com.example.demo.models.CompraModel;
import com.example.demo.models.EmpennioModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface EmpennioRepository extends CrudRepository<EmpennioModel, Integer> {
    ArrayList<EmpennioModel> findByEstado(String estado);
}
