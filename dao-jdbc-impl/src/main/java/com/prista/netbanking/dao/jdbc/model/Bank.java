package com.prista.netbanking.dao.jdbc.model;

import com.prista.netbanking.dao.api.model.IBank;

public class Bank extends BaseEntity implements IBank {

    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "name='" + name + '\'' +
                '}';
    }
}
