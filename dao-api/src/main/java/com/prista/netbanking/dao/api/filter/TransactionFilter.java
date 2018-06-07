package com.prista.netbanking.dao.api.filter;

public class TransactionFilter extends AbstractFilter {

    private String note;

    private Boolean fetchAccount;

    private Boolean fetchPaymentType;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getFetchAccount() {
        return fetchAccount;
    }

    public void setFetchAccount(Boolean fetchAccount) {
        this.fetchAccount = fetchAccount;
    }

    public Boolean getFetchPaymentType() {
        return fetchPaymentType;
    }

    public void setFetchPaymentType(Boolean fetchPaymentType) {
        this.fetchPaymentType = fetchPaymentType;
    }
}
