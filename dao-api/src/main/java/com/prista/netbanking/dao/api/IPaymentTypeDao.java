package com.prista.netbanking.dao.api;

import com.prista.netbanking.dao.api.filter.PaymentTypeFilter;
import com.prista.netbanking.dao.api.model.IPaymentType;

import java.util.List;

public interface IPaymentTypeDao extends BaseDao<IPaymentType, Integer> {

    long getCount(PaymentTypeFilter filter);

    List<IPaymentType> find(PaymentTypeFilter filter);

    IPaymentType getFullInfo(Integer id);

}
