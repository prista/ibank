package com.prista.netbanking.dao.api.filter;

public class BranchFilter extends AbstractFilter {

    private String name;

    private String bankName;

    private boolean fetchBank;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public boolean getFetchBank() {
        return fetchBank;
    }

    public void setFetchBank(final boolean fetchBank) {
        this.fetchBank = fetchBank;
    }
}
