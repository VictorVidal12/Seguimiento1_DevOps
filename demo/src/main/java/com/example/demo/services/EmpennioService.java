package com.example.demo.services;

import com.example.demo.models.EmpennioModel;
import com.example.demo.repositories.EmpennioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class EmpennioService {
    @Autowired
    EmpennioRepository empennioRepository;

    public ArrayList<EmpennioModel> getEmpennios () {
        return (ArrayList<EmpennioModel>) empennioRepository.findAll();
    }

    public Optional<EmpennioModel> getEmpennioById (Integer id) {
        return empennioRepository.findById(id);
    }

    public ArrayList<EmpennioModel> getEmpenniosByEstado (String estado) {
        return empennioRepository.findByEstado(estado);
    }

    public EmpennioModel postEmpennio (EmpennioModel empennioModel) {
        return empennioRepository.save(empennioModel);
    }

    public boolean deleteEmpennio(Integer id) {
        try {
            empennioRepository.deleteById(id);
            return true;
        } catch (Exception err) {
            return false;
        }
    }


}
