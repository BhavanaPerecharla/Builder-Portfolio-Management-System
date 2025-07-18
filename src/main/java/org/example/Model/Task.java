package org.example.Model;

import java.util.Date;

public class Task {
    private int taskId;
    private String projectId;          // FK to Project
    private String taskName;
    private Date taskStartDate;
    private Date taskEstCompleteDate;
    private int progress;              // in percentage (0–100)

    public Task() {
    }

    public Task(int taskId, String projectId, String taskName, Date taskStartDate, Date taskEstCompleteDate, int progress) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.taskName = taskName;
        this.taskStartDate = taskStartDate;
        this.taskEstCompleteDate = taskEstCompleteDate;
        this.progress = progress;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(Date taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public Date getTaskEstCompleteDate() {
        return taskEstCompleteDate;
    }

    public void setTaskEstCompleteDate(Date taskEstCompleteDate) {
        this.taskEstCompleteDate = taskEstCompleteDate;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
