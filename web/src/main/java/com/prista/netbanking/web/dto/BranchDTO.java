package com.prista.netbanking.web.dto;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BranchDTO {
    private Integer id;

    @NotNull
    private Integer bankId;

    private String bankName;

    @Size(min = 1, max = 20) // should be the same as in DB constraints
    private String name;

    private String streetAddress;

    private String city;

    private Integer postCode;

    private Date created;

    private Date updated;

    private Set<Integer> userProfilesIds;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(final Integer bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(final String bankName) {
        this.bankName = bankName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(final String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public Integer getPostCode() {
        return postCode;
    }

    public void setPostCode(final Integer postCode) {
        this.postCode = postCode;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(final Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(final Date updated) {
        this.updated = updated;
    }

    public Set<Integer> getUserProfilesIds() {
        return userProfilesIds;
    }

    public void setUserProfilesIds(final Set<Integer> userProfilesIds) {
        this.userProfilesIds = userProfilesIds;
    }
}
