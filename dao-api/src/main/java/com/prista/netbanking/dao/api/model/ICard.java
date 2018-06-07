package com.prista.netbanking.dao.api.model;

import com.prista.netbanking.dao.api.model.base.IBaseEntity;

import java.util.Date;

public interface ICard extends IBaseEntity {

    IAccount getAccount();

    void setAccount(IAccount account);

    String getCardType();

    void setCardType(String cardType);

    Date getExpirationDate();

    void setExpirationDate(Date expirationDate);

}
