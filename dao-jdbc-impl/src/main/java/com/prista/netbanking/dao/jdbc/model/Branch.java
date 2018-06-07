package com.prista.netbanking.dao.jdbc.model;

import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.dao.api.model.IBranch;
import com.prista.netbanking.dao.api.model.IUserProfile;

import java.util.HashSet;
import java.util.Set;

public class Branch extends BaseEntity implements IBranch {

    private IBank bank;

    private String name;

    private String streetAddress;

    private String city;

    private Integer postCode;

    private Set<IUserProfile> userProfiles = new HashSet<>();

    @Override
    public IBank getBank() {
        return bank;
    }

    @Override
    public void setBank(IBank bank) {
        this.bank = bank;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getStreetAddress() {
        return streetAddress;
    }

    @Override
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public Integer getPostCode() {
        return postCode;
    }

    @Override
    public void setPostCode(Integer postCode) {
        this.postCode = postCode;
    }

    @Override
    public Set<IUserProfile> getUserProfiles() {
        return userProfiles;
    }

    @Override
    public void setUserProfiles(Set<IUserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "bank=" + bank +
                ", name='" + name + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", postCode=" + postCode +
                '}';
    }
}
