package com.prista.netbanking.dao.api.filter;

public class AccountFilter extends AbstractFilter {

    private String name;

    private Boolean locked;

    private boolean fetchUserProfile;

    private boolean fetchBank;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public boolean getFetchUserProfile() {
        return fetchUserProfile;
    }

    public void setFetchUserProfile(boolean fetchUserProfile) {
        this.fetchUserProfile = fetchUserProfile;
    }

    public boolean getFetchBank() {
        return fetchBank;
    }

    public void setFetchBank(boolean fetchBank) {
        this.fetchBank = fetchBank;
    }

}
