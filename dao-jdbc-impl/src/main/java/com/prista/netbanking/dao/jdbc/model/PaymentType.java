package com.prista.netbanking.dao.jdbc.model;

import com.prista.netbanking.dao.api.model.IPaymentType;

public class PaymentType extends BaseEntity implements IPaymentType {

    private IPaymentType parent;

    private String name;

    @Override
    public IPaymentType getParent() {
        return parent;
    }

    @Override
    public void setParent(final IPaymentType parent) {
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PaymentType{" + "parent=" + parent + ", name='" + name + '\'' + '}';
    }
}
