package org.example.Model;

/**
 * Represents an Admin user in the system.
 * Contains personal details and associated address.
 */

public class Admin {
    private String adminId;
    private String adminName;
    private String adminEmail;
    private String adminPassword;
    private String adminContact;
    private String addressId;
    // No-args constructor
    public Admin() {}

    // Parameterized constructor
    public Admin(String adminName, String adminEmail, String adminPassword, String adminContact, String addressId) {
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
        this.adminContact = adminContact;
        this.addressId = addressId;
    }

    // Getters and Setters
    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminContact() {
        return adminContact;
    }

    public void setAdminContact(String adminContact) {
        this.adminContact = adminContact;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
