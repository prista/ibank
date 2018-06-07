package com.netbanking.dao.orm.model;

import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.ICard;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Card implements ICard {

    @Id
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Account.class)
    @PrimaryKeyJoinColumn
    private IAccount account;

    @Column
    private String cardType;

    @Column
    private Date expirationDate;

    @Column(updatable = false)
    private Date created;

    @Column
    private Date updated;

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

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(final Integer id) {
        this.id = id;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(final Date created) {
        this.created = created;
    }

    @Override
    public Date getUpdated() {
        return updated;
    }

    @Override
    public void setUpdated(final Date updated) {
        this.updated = updated;
    }

}
