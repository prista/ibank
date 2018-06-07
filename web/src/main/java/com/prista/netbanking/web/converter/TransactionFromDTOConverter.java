package com.prista.netbanking.web.converter;

import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.IPaymentType;
import com.prista.netbanking.dao.api.model.ITransaction;
import com.prista.netbanking.dao.api.model.enums.TransactionType;
import com.prista.netbanking.service.IAccountService;
import com.prista.netbanking.service.IPaymentTypeService;
import com.prista.netbanking.service.ITransactionService;
import com.prista.netbanking.web.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TransactionFromDTOConverter implements Function<TransactionDTO, ITransaction> {

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IPaymentTypeService paymentTypeService;

    @Override
    public ITransaction apply(TransactionDTO dto) {
        ITransaction entity = transactionService.createEntity();
        entity.setId(dto.getId());

        final IAccount from = accountService.createEntity();
        from.setId(dto.getAccountFromId());
        entity.setFromAccount(from);

        final IAccount to = accountService.createEntity();
        to.setId(dto.getAccountToId());
        entity.setToAccount(to);

        entity.setAmount(dto.getAmount());
        entity.setNote(dto.getNote());

        final IPaymentType paymentType = paymentTypeService.createEntity();
        paymentType.setId(dto.getPaymentTypeId());
        entity.setPaymentType(paymentType);

        entity.setTransactionType(TransactionType.valueOf(dto.getTransactionType()));

        return entity;
    }

}
