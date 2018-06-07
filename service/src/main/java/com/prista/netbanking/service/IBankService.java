package com.prista.netbanking.service;

import com.prista.netbanking.dao.api.filter.BankFilter;
import com.prista.netbanking.dao.api.model.IBank;

import javax.transaction.Transactional;
import java.util.List;

public interface IBankService {

    IBank get(Integer id);

    List<IBank> getAll();

    @Transactional
    void save(IBank entity);

    @Transactional
    void delete(Integer id);

    @Transactional
    void deleteAll();

    IBank createEntity();

    List<IBank> find(BankFilter filter);

    long getCount(BankFilter filter);

}
