package com.prista.netbanking.dao.api.model;

import com.prista.netbanking.dao.api.model.base.IBaseEntity;
import com.prista.netbanking.dao.api.model.enums.AccountType;
import com.prista.netbanking.dao.api.model.enums.CurrencyType;

public interface IAccount extends IBaseEntity {

    String getName();

    void setName(String name);

    IUserProfile getUserProfile();

    void setUserProfile(IUserProfile userProfile);

    ICard getCard();

    void setCard(ICard card);

    AccountType getAccountType();

    void setAccountType(AccountType accountType);

    Double getBalance();

    void setBalance(Double balance);

    CurrencyType getCurrency();

    void setCurrency(CurrencyType currency);

    Boolean getLocked();

    IBank getBank();

    void setBank(IBank bank);

    void setLocked(Boolean locked);

    Boolean getDeleted();

    void setDeleted(Boolean deleted);

}
