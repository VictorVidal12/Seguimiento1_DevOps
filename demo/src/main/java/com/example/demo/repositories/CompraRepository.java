package com.example.demo.repositories;

import com.example.demo.models.CompraModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;


@Repository
public interface CompraRepository extends CrudRepository<CompraModel, Integer> {
    public abstract ArrayList<CompraModel> findByFecha(LocalDateTime fecha);
}
