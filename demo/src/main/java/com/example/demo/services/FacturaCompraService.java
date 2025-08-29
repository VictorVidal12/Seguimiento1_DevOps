package com.example.demo.services;

import com.example.demo.models.CompraModel;
import com.example.demo.models.FacturaCompraModel;
import com.example.demo.repositories.FacturaCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
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

    @Transactional
    public Optional<FacturaCompraModel> putFacturaCompra(Integer id, FacturaCompraModel factura) {
        return facturaCompraRepository.findById(id)
                .map(existing -> {
                    factura.setIdFacturaCompra(existing.getIdFacturaCompra());
                    return facturaCompraRepository.save(factura);
                });
    }

    @Transactional
    public Optional<FacturaCompraModel> patchFacturaCompra(Integer id, FacturaCompraModel partial) {
        return facturaCompraRepository.findById(id)
                .map(existing -> {
                    copyNonNullProperties(partial, existing);
                    existing.setIdFacturaCompra(id);
                    return facturaCompraRepository.save(existing);
                });
    }

    public boolean deleteFactura (Integer id) {
        try {
            facturaCompraRepository.deleteById(id);
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
