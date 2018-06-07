package com.prista.netbanking.dao.jdbc.model;

import com.prista.netbanking.dao.api.model.IUserProfile;

public class UserProfile extends BaseEntity implements IUserProfile {

    private String username;

    private String password;

    private String role;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(final String username) {
        this.username = username;
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

    @Override
    public String toString() {
        return "UserProfile [username=" + username + ", password=" + password + ", " + "getId()=" + getId()
                + ", getCreated()=" + getCreated() + ", getUpdated()=" + getUpdated() + "]";
    }
}
