package com.prista.netbanking.web.dto.address;

public class AddressDTO {

    private String region;
    private String city;
    private String country;

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "AddressDTO [region=" + region + ", city=" + city + ", country=" + country + "]";
    }

}
