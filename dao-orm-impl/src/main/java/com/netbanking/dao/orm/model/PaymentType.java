package com.netbanking.dao.orm.model;

import com.prista.netbanking.dao.api.model.IPaymentType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class PaymentType extends BaseEntity implements IPaymentType {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = PaymentType.class)
    private IPaymentType parent;

    @Column
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
        return "PaymentType{" +
                "parent=" + parent +
                ", name='" + name + '\'' +
                '}';
    }
}
