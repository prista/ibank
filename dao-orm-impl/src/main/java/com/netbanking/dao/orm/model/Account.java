package com.netbanking.dao.orm.model;

import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.dao.api.model.ICard;
import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.dao.api.model.enums.AccountType;
import com.prista.netbanking.dao.api.model.enums.CurrencyType;

import javax.persistence.*;

@Entity
public class Account extends BaseEntity implements IAccount {

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserProfile.class)
    private IUserProfile userProfile;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "account", targetEntity = Card.class)
    private ICard card;

    @Column
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column
    private Double balance;

    @Column
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @Column
    private Boolean locked;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Bank.class)
    private IBank bank;

    @Column
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

    @Override
    public IBank getBank() {
        return bank;
    }

    @Override
    public void setBank(final IBank bank) {
        this.bank = bank;
    }

    @Override
    public Boolean getLocked() {
        return locked;
    }

    @Override
    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    @Override
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
