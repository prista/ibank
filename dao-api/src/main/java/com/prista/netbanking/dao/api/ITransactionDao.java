package com.prista.netbanking.dao.api;

import com.prista.netbanking.dao.api.filter.TransactionFilter;
import com.prista.netbanking.dao.api.model.ITransaction;

import java.util.List;

public interface ITransactionDao extends BaseDao<ITransaction, Integer> {

    long getCount(TransactionFilter filter);

    List<ITransaction> find(TransactionFilter filter);

    ITransaction getFullInfo(Integer id);

    ITransaction getNewestTransaction();

    void updateBalance(Integer id);
}
