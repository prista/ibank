package com.prista.netbanking.dao.api.model;

import com.prista.netbanking.dao.api.model.base.IBaseEntity;

public interface IBank extends IBaseEntity {

    String getName();

    void setName(String name);
}
