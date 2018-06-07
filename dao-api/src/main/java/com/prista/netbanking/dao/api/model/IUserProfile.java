package com.prista.netbanking.dao.api.model;

import com.prista.netbanking.dao.api.model.base.IBaseEntity;

public interface IUserProfile extends IBaseEntity {

    String getUsername();

    void setUsername(String username);

    String getRole();

    void setRole(String role);

    String getPassword();

    void setPassword(String password);

}
