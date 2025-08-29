package com.example.demo.services;

import com.example.demo.models.EmpennioModel;
import com.example.demo.repositories.EmpennioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
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


    @Transactional
    public Optional<EmpennioModel> putEmpennio(Integer id, EmpennioModel empennio) {
        return empennioRepository.findById(id)
                .map(existing -> {
                    empennio.setIdempennio(existing.getIdempennio());
                    return empennioRepository.save(empennio);
                });
    }

    @Transactional
    public Optional<EmpennioModel> patchEmpennio(Integer id, EmpennioModel partial) {
        return empennioRepository.findById(id)
                .map(existing -> {
                    copyNonNullProperties(partial, existing);
                    existing.setIdempennio(id);
                    return empennioRepository.save(existing);
                });
    }

    public boolean deleteEmpennio(Integer id) {
        try {
            empennioRepository.deleteById(id);
            return true;
        } catch (Exception err) {
            return false;
        }
    }

    private void copyNonNullProperties(Object src, Object target) {
        BeanWrapper srcWrap = new BeanWrapperImpl(src);
        PropertyDescriptor[] pds = srcWrap.getPropertyDescriptors();

        List<String> emptyNames = new ArrayList<>();
        for (PropertyDescriptor pd : pds) {
            String name = pd.getName();
            if ("class".equals(name)) continue;
            Object srcValue = srcWrap.getPropertyValue(name);
            if (srcValue == null) {
                emptyNames.add(name);
            }
        }
        if (!emptyNames.contains("id")) {
            emptyNames.add("id");
        }
        String[] ignoreProperties = emptyNames.toArray(new String[0]);
        BeanUtils.copyProperties(src, target, ignoreProperties);
    }

}
