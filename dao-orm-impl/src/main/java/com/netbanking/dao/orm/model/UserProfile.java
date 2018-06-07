package com.netbanking.dao.orm.model;

import com.prista.netbanking.dao.api.model.IUserProfile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class UserProfile extends BaseEntity implements IUserProfile {

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String role;

    @Transient
    private String upperCaseName;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
        setUpperCaseName(username.toUpperCase());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(final String role) {
        this.role = role;
    }

    public String getUpperCaseName() {
        return upperCaseName;
    }

    public void setUpperCaseName(String upperCaseName) {
        this.upperCaseName = upperCaseName;
    }

    @Override
    public String toString() {
        return "UserProfile [username=" + username + ", password=" + password + ", "
                + "getId()=" + getId() + ", getCreated()="
                + getCreated() + ", getUpdated()=" + getUpdated() + "]";
    }

}
