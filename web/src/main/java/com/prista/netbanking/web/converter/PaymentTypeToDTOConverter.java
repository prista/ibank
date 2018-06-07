package com.prista.netbanking.web.converter;

import com.prista.netbanking.dao.api.model.IPaymentType;
import com.prista.netbanking.web.dto.PaymentTypeDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PaymentTypeToDTOConverter implements Function<IPaymentType, PaymentTypeDTO> {

    @Override
    public PaymentTypeDTO apply(IPaymentType entity) {
        PaymentTypeDTO dto = new PaymentTypeDTO();
        dto.setId(entity.getId());

        IPaymentType parent = entity.getParent();
        if (parent != null) {
            dto.setParentId(parent.getId());
            dto.setParentName(parent.getName());
        }

        dto.setName(entity.getName());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        return dto;
    }
}
