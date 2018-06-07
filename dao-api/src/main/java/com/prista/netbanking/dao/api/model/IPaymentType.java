package com.prista.netbanking.dao.api.model;

import com.prista.netbanking.dao.api.model.base.IBaseEntity;

public interface IPaymentType extends IBaseEntity {

    IPaymentType getParent();

    void setParent(IPaymentType parent);

    String getName();

    void setName(String name);
}
