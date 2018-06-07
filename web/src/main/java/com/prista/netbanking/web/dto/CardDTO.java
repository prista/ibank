package com.prista.netbanking.web.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class CardDTO {

    private String cardType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
