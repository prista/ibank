package com.prista.netbanking.web.converter;

import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.IPaymentType;
import com.prista.netbanking.dao.api.model.ITransaction;
import com.prista.netbanking.web.dto.TransactionDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TransactionToDTOConverter implements Function<ITransaction, TransactionDTO> {

    @Override
    public TransactionDTO apply(ITransaction entity) {
        final TransactionDTO dto = new TransactionDTO();
        dto.setId(entity.getId());

        final IAccount from = entity.getFromAccount();
        if (from != null) {
            dto.setAccountFromId(from.getId());
            dto.setAccountFromName(from.getName());
        }

        final IAccount to = entity.getToAccount();
        if (to != null) {
            dto.setAccountToId(to.getId());
            dto.setAccountToName(to.getName());
        }

        dto.setAmount(entity.getAmount());
        dto.setNote(entity.getNote());

        final IPaymentType paymentType = entity.getPaymentType();
        if (paymentType != null) {
            dto.setPaymentTypeId(paymentType.getId());
            dto.setPaymentTypeName(paymentType.getName());
        }

        dto.setTransactionType(entity.getTransactionType().name());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());

        return dto;
    }

}
