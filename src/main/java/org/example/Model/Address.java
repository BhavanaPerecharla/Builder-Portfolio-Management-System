package org.example.Model;


// Address Model
public class Address {
    private String addressId;
    private String addressLine1;
    private String city;
    private String states;
    private String zipCode;
    private String country;

    public Address() {}

    public Address(String addressLine1, String city, String states, String zipCode, String country) {
        this.addressLine1 = addressLine1;
        this.city = city;
        this.states = states;
        this.zipCode = zipCode;
        this.country = country;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
