package com.netbanking.dao.orm.model;

import com.prista.netbanking.dao.api.model.IBank;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Bank extends BaseEntity implements IBank {

    @Column
    private String name;

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
        return "Bank{" +
                "name='" + name + '\'' +
                '}';
    }
}
