package org.example.Model;

import org.example.Model.Address;
public class Manager {
    private String managerId;
    private String managerName;
    private String managerEmail;
    private String managerPassword;
    private String managerContact;
    private String managerStatus; // free or working
    private String builderId;     // Foreign key reference to Builder
    private Address address;      // Composition with Address

    public Manager() {
    }

    public Manager(String managerId, String managerName, String managerEmail, String managerPassword,
                   String managerContact, String managerStatus, String builderId, Address address) {
        this.managerId = managerId;
        this.managerName = managerName;
        this.managerEmail = managerEmail;
        this.managerPassword = managerPassword;
        this.managerContact = managerContact;
        this.managerStatus = managerStatus;
        this.builderId = builderId;
        this.address = address;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getManagerPassword() {
        return managerPassword;
    }

    public void setManagerPassword(String managerPassword) {
        this.managerPassword = managerPassword;
    }

    public String getManagerContact() {
        return managerContact;
    }

    public void setManagerContact(String managerContact) {
        this.managerContact = managerContact;
    }

    public String getManagerStatus() {
        return managerStatus;
    }

    public void setManagerStatus(String managerStatus) {
        this.managerStatus = managerStatus;
    }

    public String getBuilderId() {
        return builderId;
    }

    public void setBuilderId(String builderId) {
        this.builderId = builderId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

