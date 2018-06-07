package com.netbanking.dao.orm.model;

import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.dao.api.model.IBranch;
import com.prista.netbanking.dao.api.model.IUserProfile;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Branch extends BaseEntity implements IBranch {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Bank.class)
    private IBank bank;

    @Column
    private String name;

    @Column
    private String streetAddress;

    @Column
    private String city;

    @Column
    private Integer postCode;

    @JoinTable(name = "user_profile_2_branch", joinColumns = {@JoinColumn(name = "branch_id")}, inverseJoinColumns = {
            @JoinColumn(name = "user_profile_id")})
    @ManyToMany(targetEntity = UserProfile.class, fetch = FetchType.LAZY)
    @OrderBy("username ASC")
    private Set<IUserProfile> userProfiles = new HashSet<>();

    @Override
    public IBank getBank() {
        return bank;
    }

    @Override
    public void setBank(final IBank bank) {
        this.bank = bank;
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
    public String getStreetAddress() {
        return streetAddress;
    }

    @Override
    public void setStreetAddress(final String streetAddress) {
        this.streetAddress = streetAddress;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(final String city) {
        this.city = city;
    }

    @Override
    public Integer getPostCode() {
        return postCode;
    }

    @Override
    public void setPostCode(final Integer postCode) {
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

