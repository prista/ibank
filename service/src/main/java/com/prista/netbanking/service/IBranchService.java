package com.prista.netbanking.service;

import com.prista.netbanking.dao.api.filter.BranchFilter;
import com.prista.netbanking.dao.api.model.IBranch;

import javax.transaction.Transactional;
import java.util.List;

public interface IBranchService {

    IBranch get(Integer id);

    IBranch getFullInfo(Integer id);

    List<IBranch> getAll();

    @Transactional
    void save(IBranch entity);

    @Transactional
    void delete(Integer id);

    @Transactional
    void deleteAll();

    IBranch createEntity();

    List<IBranch> find(BranchFilter filter);

    long getCount(BranchFilter filter);
}
