package com.prista.netbanking.dao.jdbc.model;

import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.ICard;

import java.util.Date;

public class Card extends BaseEntity implements ICard {

    private IAccount account;

    private String cardType;

    private Date expirationDate;

    @Override
    public IAccount getAccount() {
        return account;
    }

    @Override
    public void setAccount(final IAccount account) {
        this.account = account;
    }

    @Override
    public String getCardType() {
        return cardType;
    }

    @Override
    public void setCardType(final String cardType) {
        this.cardType = cardType;
    }

    @Override
    public Date getExpirationDate() {
        return expirationDate;
    }

    @Override
    public void setExpirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "Card{" +
                "getId()=" + getId() +
                ", cardType='" + cardType + '\'' +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
