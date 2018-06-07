package com.prista.netbanking.web.converter;

import java.util.function.Function;

import com.prista.netbanking.web.dto.BankDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.service.IBankService;

@Component
public class BankFromDTOConverter implements Function<BankDTO, IBank> {

    @Autowired
    private IBankService bankService;

    @Override
    public IBank apply(final BankDTO dto) {
        final IBank entity = bankService.createEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }
}
