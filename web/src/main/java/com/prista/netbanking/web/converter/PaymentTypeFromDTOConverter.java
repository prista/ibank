package com.prista.netbanking.web.converter;

import java.util.function.Function;

import com.prista.netbanking.web.dto.PaymentTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prista.netbanking.dao.api.model.IPaymentType;
import com.prista.netbanking.service.IPaymentTypeService;

@Component
public class PaymentTypeFromDTOConverter implements Function<PaymentTypeDTO, IPaymentType> {

    @Autowired
    private IPaymentTypeService paymentTypeService;

    @Override
    public IPaymentType apply(final PaymentTypeDTO dto) {
        final IPaymentType entity = paymentTypeService.createEntity();
        entity.setId(dto.getId());

        if (dto.getParentId() != null) {
            final IPaymentType parent = paymentTypeService.createEntity();
            parent.setId(dto.getParentId());
            entity.setParent(parent);           
        }


        entity.setName(dto.getName());


        return entity;
    }
}
