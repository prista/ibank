package com.prista.netbanking.service;

import com.prista.netbanking.dao.api.filter.TransactionFilter;
import com.prista.netbanking.dao.api.model.ITransaction;

import javax.transaction.Transactional;
import java.util.List;

public interface ITransactionService {

    ITransaction get(Integer id);

    ITransaction getFullInfo(Integer id);

    @Transactional
    void save(ITransaction entity);

    @Transactional
    void delete(Integer id);

    @Transactional
    void deleteAll();

    ITransaction createEntity();

    List<ITransaction> getAll();

    List<ITransaction> find(TransactionFilter filter);

    long getCount(TransactionFilter filter);

    ITransaction getNewestTransaction();

}
