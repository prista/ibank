package com.netbanking.dao.orm.model;

import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.IPaymentType;
import com.prista.netbanking.dao.api.model.ITransaction;
import com.prista.netbanking.dao.api.model.enums.TransactionType;

import javax.persistence.*;

@Entity
public class Transaction extends BaseEntity implements ITransaction {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
    private IAccount fromAccount;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
    private IAccount toAccount;

    @Column
    private Double amount;

    @Column
    private String note;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = PaymentType.class)
    private IPaymentType paymentType;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Override
    public IAccount getFromAccount() {
        return fromAccount;
    }

    @Override
    public void setFromAccount(final IAccount fromAccount) {
        this.fromAccount = fromAccount;
    }

    @Override
    public IAccount getToAccount() {
        return toAccount;
    }

    @Override
    public void setToAccount(final IAccount toAccount) {
        this.toAccount = toAccount;
    }

    @Override
    public Double getAmount() {
        return amount;
    }

    @Override
    public void setAmount(final Double amount) {
        this.amount = amount;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(final String note) {
        this.note = note;
    }

    @Override
    public IPaymentType getPaymentType() {
        return paymentType;
    }

    @Override
    public void setPaymentType(final IPaymentType paymentType) {
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
                '}';
    }
}
