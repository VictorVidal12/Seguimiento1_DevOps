package com.example.demo.repositories;

import com.example.demo.models.EmpennioModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpennioRepository extends CrudRepository<EmpennioModel, Integer> {

}
