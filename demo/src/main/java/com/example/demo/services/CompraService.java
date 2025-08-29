package com.example.demo.services;

import com.example.demo.models.CompraModel;
import com.example.demo.repositories.CompraRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompraService {
    @Autowired
    CompraRepository compraRepository;

    public ArrayList<CompraModel> getCompras(){
        return (ArrayList<CompraModel>) compraRepository.findAll();
    }

    public CompraModel postCompra(CompraModel compra) {
        return compraRepository.save(compra);
    }

    public Optional<CompraModel> getCompraById(Integer id) {
        return compraRepository.findById(id);
    }

    public ArrayList<CompraModel> getCompraByFecha(LocalDateTime fechaCompra) {
        return compraRepository.findByFechaCompra(fechaCompra);
    }

    @Transactional
    public Optional<CompraModel> putCompra(Integer id, CompraModel compra) {
        return compraRepository.findById(id)
                .map(existing -> {
                    compra.setIdcompra(existing.getIdcompra());
                    return compraRepository.save(compra);
                });
    }

    @Transactional
    public Optional<CompraModel> patchCompra(Integer id, CompraModel partial) {
        return compraRepository.findById(id)
                .map(existing -> {
                    copyNonNullProperties(partial, existing);
                    existing.setIdcompra(id);
                    return compraRepository.save(existing);
                });
    }

    public boolean deleteCompra(Integer id) {
        try {
            compraRepository.deleteById(id);
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
