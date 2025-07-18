package org.example.Model;


import java.math.BigDecimal;
import java.util.Date;

public class ProjectExpense {
    private String projectId;           // FK to Project
    private int paymentId;              // Part of composite PK (projectId + paymentId)
    private Date paymentDate;
    private BigDecimal amount;
    private String paymentDescription;

    public ProjectExpense() {
    }

    public ProjectExpense(String projectId, int paymentId, Date paymentDate, BigDecimal amount, String paymentDescription) {
        this.projectId = projectId;
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentDescription = paymentDescription;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }
}
