package com.prista.netbanking.service;


import com.prista.netbanking.dao.api.filter.PaymentTypeFilter;
import com.prista.netbanking.dao.api.model.IPaymentType;

import javax.transaction.Transactional;
import java.util.List;

public interface IPaymentTypeService {

    IPaymentType get(Integer id);

    IPaymentType getFullInfo(Integer id);

    @Transactional
    void save(IPaymentType entity);

    @Transactional
    void delete(Integer id);

    @Transactional
    void deleteAll();

    IPaymentType createEntity();

    List<IPaymentType> getAll();

    List<IPaymentType> find(PaymentTypeFilter filter);

    long getCount(PaymentTypeFilter filter);

}
