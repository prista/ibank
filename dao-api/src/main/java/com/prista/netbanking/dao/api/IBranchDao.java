package com.prista.netbanking.dao.api;

import com.prista.netbanking.dao.api.filter.BranchFilter;
import com.prista.netbanking.dao.api.model.IBranch;

import java.util.List;

public interface IBranchDao extends BaseDao<IBranch, Integer> {

    long getCount(BranchFilter filter);

    List<IBranch> find(BranchFilter filter);

    IBranch getFullInfo(Integer id);
}
