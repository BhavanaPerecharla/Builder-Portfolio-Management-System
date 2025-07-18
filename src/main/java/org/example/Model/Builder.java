package org.example.Model;

public class Builder {
    private String builderId;
    private String builderName;
    private String builderEmail;
    private String builderPassword;
    private String builderContact;
    private Address address; // Composition with Address

    public Builder() {
    }

    public Builder(String builderId, String builderName, String builderEmail, String builderPassword, String builderContact, Address address) {
        this.builderId = builderId;
        this.builderName = builderName;
        this.builderEmail = builderEmail;
        this.builderPassword = builderPassword;
        this.builderContact = builderContact;
        this.address = address;
    }

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
