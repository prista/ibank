package com.prista.netbanking.service;

import com.prista.netbanking.dao.api.filter.AccountFilter;
import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.ICard;

import javax.transaction.Transactional;
import java.util.List;

public interface IAccountService {

    IAccount get(Integer id);

    IAccount getCard(Integer id);

    IAccount getFullInfo(Integer id);

    @Transactional
    void save(IAccount entity);

    @Transactional
    void save(IAccount entity, ICard card);

    @Transactional
    void update(ICard card);

    @Transactional
    void delete(Integer id);

    @Transactional
    void deleteAll();

    IAccount createEntity();

    ICard createCardEntity();

    List<IAccount> getAll();

    List<IAccount> find(AccountFilter filter);

    long getCount(AccountFilter filter);

}
