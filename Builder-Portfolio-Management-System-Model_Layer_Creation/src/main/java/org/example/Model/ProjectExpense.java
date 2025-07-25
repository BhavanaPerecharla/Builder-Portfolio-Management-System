package org.example.Model;
/**
 * Represents an individual project expense/payment entry.
 */

public class ProjectExpense {
    private String projectId;
    private String paymentId;
    private java.sql.Date paymentDate;
    private double amount;
    private String paymentDescription;

    //Default constructor
    public ProjectExpense() {}

    //Parameterized constructor to initialize all fields
    public ProjectExpense(String projectId, String paymentId, java.sql.Date paymentDate, double amount, String paymentDescription) {
        this.projectId = projectId;
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentDescription = paymentDescription;
    }

    // Getters and Setters for each field
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public java.sql.Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(java.sql.Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }
}
