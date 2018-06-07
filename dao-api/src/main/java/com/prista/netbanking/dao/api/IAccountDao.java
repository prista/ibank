package com.prista.netbanking.dao.api;

import com.prista.netbanking.dao.api.filter.AccountFilter;
import com.prista.netbanking.dao.api.model.IAccount;

import java.util.List;

public interface IAccountDao extends BaseDao<IAccount, Integer> {

    long getCount(AccountFilter filter);

    List<IAccount> find(AccountFilter filter);

    IAccount getFullInfo(Integer id);

}
