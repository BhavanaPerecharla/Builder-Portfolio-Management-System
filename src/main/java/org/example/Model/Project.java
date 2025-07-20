package org.example.Model;

import java.math.BigDecimal;
import java.util.Date;

public class Project {
    private String projectId;
    private String projectName;
    private String projectDescription;
    private Date projectStartDate;
    private Date projectEstCompleteDate;
    private Date projectActualCompleteDate;
    private String projectStatus; // Upcoming, In Progress, Completed
    private String managerId;     // FK to Manager
    private String clientId;      // FK to Client
    private String builderId;     // FK to Builder
    private BigDecimal estimatedCost;

    public Project() {
    }

    public Project(String projectId, String projectName, String projectDescription,
                   Date projectStartDate, Date projectEstCompleteDate, Date projectActualCompleteDate,
                   String projectStatus, String managerId, String clientId, String builderId, BigDecimal estimatedCost) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectStartDate = projectStartDate;
        this.projectEstCompleteDate = projectEstCompleteDate;
        this.projectActualCompleteDate = projectActualCompleteDate;
        this.projectStatus = projectStatus;
        this.managerId = managerId;
        this.clientId = clientId;
        this.builderId = builderId;
        this.estimatedCost = estimatedCost;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public Date getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(Date projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public Date getProjectEstCompleteDate() {
        return projectEstCompleteDate;
    }

    public void setProjectEstCompleteDate(Date projectEstCompleteDate) {
        this.projectEstCompleteDate = projectEstCompleteDate;
    }

    public Date getProjectActualCompleteDate() {
        return projectActualCompleteDate;
    }

    public void setProjectActualCompleteDate(Date projectActualCompleteDate) {
        this.projectActualCompleteDate = projectActualCompleteDate;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getBuilderId() {
        return builderId;
    }

    public void setBuilderId(String builderId) {
        this.builderId = builderId;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }
}
