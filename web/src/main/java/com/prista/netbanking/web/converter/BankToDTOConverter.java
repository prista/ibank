package com.prista.netbanking.web.converter;

import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.web.dto.BankDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class BankToDTOConverter implements Function<IBank, BankDTO> {

    @Override
    public BankDTO apply(final IBank entity) {
        final BankDTO BankDTO = new BankDTO();
        BankDTO.setId(entity.getId());
        BankDTO.setName(entity.getName());
        BankDTO.setCreated(entity.getCreated());
        BankDTO.setUpdated(entity.getUpdated());
        return BankDTO;
    }

}
