package com.prista.netbanking.dao.api;

import com.prista.netbanking.dao.api.filter.BankFilter;
import com.prista.netbanking.dao.api.model.IBank;

import java.util.List;

public interface IBankDao extends BaseDao<IBank, Integer> {
    long getCount(BankFilter filter);

    List<IBank> find(BankFilter filter);
}
