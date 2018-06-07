package com.prista.netbanking.web.dto.search;

public class AccountSearchDTO {

    private String name;

    private Boolean unlockedOnly;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getUnlockedOnly() {
        return unlockedOnly;
    }

    public void setUnlockedOnly(Boolean unlockedOnly) {
        this.unlockedOnly = unlockedOnly;
    }
}
