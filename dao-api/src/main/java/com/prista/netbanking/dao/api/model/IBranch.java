package com.prista.netbanking.dao.api.model;

import com.prista.netbanking.dao.api.model.base.IBaseEntity;

import java.util.Set;

public interface IBranch extends IBaseEntity {

    IBank getBank();

    void setBank(IBank bank);

    String getName();

    void setName(String name);

    String getStreetAddress();

    void setStreetAddress(String streetAddress);

    String getCity();

    void setCity(String city);

    Integer getPostCode();

    void setPostCode(Integer postCode);

    Set<IUserProfile> getUserProfiles();

    void setUserProfiles(Set<IUserProfile> userProfiles);

}
