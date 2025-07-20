package org.example.Model;

// Manager Model
public class Manager {
    private String managerId;
    private String managerName;
    private String managerEmail;
    private String managerPassword;
    private String managerContact;
    private String pmStatus;
    private String builderId;
    private String addressId;

    public Manager() {}

    public Manager(String managerName, String managerEmail, String managerPassword, String managerContact, String pmStatus, String builderId, String addressId) {
        this.managerName = managerName;
        this.managerEmail = managerEmail;
        this.managerPassword = managerPassword;
        this.managerContact = managerContact;
        this.pmStatus = pmStatus;
        this.builderId = builderId;
        this.addressId = addressId;
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

    public String getPmStatus() {
        return pmStatus;
    }

    public void setPmStatus(String pmStatus) {
        this.pmStatus = pmStatus;
    }

    public String getBuilderId() {
        return builderId;
    }

    public void setBuilderId(String builderId) {
        this.builderId = builderId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
