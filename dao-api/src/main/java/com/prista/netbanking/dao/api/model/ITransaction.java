package com.prista.netbanking.dao.api.model;

import com.prista.netbanking.dao.api.model.base.IBaseEntity;
import com.prista.netbanking.dao.api.model.enums.TransactionType;

public interface ITransaction extends IBaseEntity {

    IAccount getFromAccount();

    void setFromAccount(IAccount from);

    IAccount getToAccount();

    void setToAccount(IAccount to);

    Double getAmount();

    void setAmount(Double amount);

    String getNote();

    void setNote(String note);

    IPaymentType getPaymentType();

    void setPaymentType(IPaymentType paymentType);

    TransactionType getTransactionType();

    void setTransactionType(TransactionType transactionType);
}
