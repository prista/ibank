package com.prista.netbanking.dao.jdbc.model;

import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.dao.api.model.ICard;
import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.dao.api.model.enums.AccountType;
import com.prista.netbanking.dao.api.model.enums.CurrencyType;

public class Account extends BaseEntity implements IAccount {

    private String name;

    private IUserProfile userProfile;

    private ICard card;

    private AccountType accountType;

    private Double balance;

    private CurrencyType currency;

    private Boolean locked;

    private IBank bank;

    private Boolean deleted;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public IUserProfile getUserProfile() {
        return userProfile;
    }

    @Override
    public void setUserProfile(final IUserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public ICard getCard() {
        return card;
    }

    @Override
    public void setCard(final ICard card) {
        this.card = card;
    }

    @Override
    public AccountType getAccountType() {
        return accountType;
    }

    @Override
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public Double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(final Double balance) {
        this.balance = balance;
    }

    @Override
    public CurrencyType getCurrency() {
        return currency;
    }

    @Override
    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public Boolean getLocked() {
        return locked;
    }

    @Override
    public void setLocked(final Boolean locked) {
        this.locked = locked;
    }

    @Override
    public IBank getBank() {
        return bank;
    }

    @Override
    public void setBank(IBank bank) {
        this.bank = bank;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Account{" +
                "getId()='" + getId() + '\'' +
                "name='" + name + '\'' +
                ", userProfile=" + userProfile +
                ", card=" + card +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                ", locked=" + locked +
                ", bank=" + bank +
                ", deleted=" + deleted +
                '}';
    }
}
