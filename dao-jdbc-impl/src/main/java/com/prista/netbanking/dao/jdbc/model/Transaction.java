package com.prista.netbanking.dao.jdbc.model;

import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.IPaymentType;
import com.prista.netbanking.dao.api.model.ITransaction;
import com.prista.netbanking.dao.api.model.enums.TransactionType;

public class Transaction extends BaseEntity implements ITransaction {

    private IAccount fromAccount;

    private IAccount toAccount;

    private Double amount;

    private String note;

    private IPaymentType paymentType;

    private TransactionType transactionType;

    @Override
    public IAccount getFromAccount() {
        return fromAccount;
    }

    @Override
    public void setFromAccount(IAccount fromAccount) {
        this.fromAccount = fromAccount;
    }

    @Override
    public IAccount getToAccount() {
        return toAccount;
    }

    @Override
    public void setToAccount(IAccount toAccount) {
        this.toAccount = toAccount;
    }

    @Override
    public Double getAmount() {
        return amount;
    }

    @Override
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public IPaymentType getPaymentType() {
        return paymentType;
    }

    @Override
    public void setPaymentType(IPaymentType paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public TransactionType getTransactionType() {
        return transactionType;
    }

    @Override
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", amount=" + amount +
                ", note='" + note + '\'' +
                ", paymentType=" + paymentType +
                ", transactionType=" + transactionType +
                '}';
    }
}
