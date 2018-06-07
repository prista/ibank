package com.prista.netbanking.web.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.prista.netbanking.service.impl.TransactionServiceImpl.validateTransaction;

public class TransactionDTO {

    private Integer id;

    @NotNull(message = "Account should not be empty or equal TO name")
    private Integer accountFromId;

    private String accountFromName;

    @NotNull(message = "Account should not be empty or equal FROM name")
    private Integer accountToId;

    private String accountToName;

    @NotNull
    @Min(value = 0, message = "Amount should not be negative")
    private Double amount;

    private String note;

    private Integer paymentTypeId;

    private String paymentTypeName;

    @NotNull
    private String transactionType;

    private Date created;

    private Date updated;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(Integer accountFromId) {
        if (!validateTransaction(accountFromId, this.accountToId)) {
            this.accountFromId = accountFromId;
        }
    }

    public String getAccountFromName() {
        return accountFromName;
    }

    public void setAccountFromName(String accountFromName) {
        this.accountFromName = accountFromName;
    }

    public Integer getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(Integer accountToId) {
        if (!validateTransaction(accountToId, this.accountFromId)) {
            this.accountToId = accountToId;
        }
    }

    public String getAccountToName() {
        return accountToName;
    }

    public void setAccountToName(String accountToName) {
        this.accountToName = accountToName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(Integer paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
