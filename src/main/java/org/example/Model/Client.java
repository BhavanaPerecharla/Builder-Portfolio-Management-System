package org.example.Model;

/**
 * Represents a Client entity in the system.
 * Includes basic client information and address association.
 */

public class Client {
    private String clientId;
    private String clientName;
    private String clientEmail;
    private String clientPassword;
    private String clientContact;
    private String clientType;
    private String addressId;
    private Address address;

    // Constructors
    public Client() {}

    // Parameterized constructor
    public Client(String clientId, String clientName, String clientEmail, String clientPassword,
                  String clientContact, String clientType,String addressId, Address address) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPassword = clientPassword;
        this.clientContact = clientContact;
        this.clientType = clientType;
        this.addressId=addressId;
        this.address = address;
    }

    // Getters and Setters
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientPassword() {
        return clientPassword;
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }

    public String getClientContact() {
        return clientContact;
    }

    public void setClientContact(String clientContact) {
        this.clientContact = clientContact;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
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

    /**
     * Sets the Address object and auto-syncs the addressId for consistency.
     */

    public void setAddress(Address address) {
        this.address = address;
        if (address != null) {
            this.addressId = address.getAddressId();
        }
    }
}
