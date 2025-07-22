package org.example.Model;

/**
 * Represents a Builder in the system.
 * Holds basic information along with address association.
 */
public class Builder {
    private String builderId;
    private String builderName;
    private String builderEmail;
    private String builderPassword;
    private String builderContact;
    private String addressId;
    private Address address;

    // No-args constructor
    public Builder() {}

    // Constructor with Address ID
    public Builder(String builderName, String builderEmail, String builderPassword, String builderContact, String addressId) {
        this.builderName = builderName;
        this.builderEmail = builderEmail;
        this.builderPassword = builderPassword;
        this.builderContact = builderContact;
        this.addressId = addressId;
    }

    // Constructor with Address object
    public Builder(String builderId, String builderName, String builderEmail, String builderPassword, String builderContact, Address address) {
        this.builderId = builderId;
        this.builderName = builderName;
        this.builderEmail = builderEmail;
        this.builderPassword = builderPassword;
        this.builderContact = builderContact;
        this.address = address;
        syncAddressIdFromAddress();
    }

    // Sync method (if address is set externally)
    public void syncAddressIdFromAddress() {
        if (address != null) {
            this.addressId = address.getAddressId();
        }
    }

    // Getters and Setters
    public String getBuilderId() {
        return builderId;
    }

    public void setBuilderId(String builderId) {
        this.builderId = builderId;
    }

    public String getBuilderName() {
        return builderName;
    }

    public void setBuilderName(String builderName) {
        this.builderName = builderName;
    }

    public String getBuilderEmail() {
        return builderEmail;
    }

    public void setBuilderEmail(String builderEmail) {
        this.builderEmail = builderEmail;
    }

    public String getBuilderPassword() {
        return builderPassword;
    }

    public void setBuilderPassword(String builderPassword) {
        this.builderPassword = builderPassword;
    }

    public String getBuilderContact() {
        return builderContact;
    }

    public void setBuilderContact(String builderContact) {
        this.builderContact = builderContact;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        if (address != null) {
            this.addressId = address.getAddressId(); // Sync the ID
        }
    }
}