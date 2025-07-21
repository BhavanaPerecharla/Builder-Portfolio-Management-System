package org.example.Model;

import java.sql.Timestamp;

public class ProjectDocument {

    private String documentId;
    private String projectId;
    private String fileName;
    private String fileType;
    private byte[] fileData;
    private Timestamp uploadedOn;

    // --- Constructors ---
    public ProjectDocument() {
    }

    public ProjectDocument(String projectId, String fileName, String fileType, byte[] fileData) {
        this.projectId = projectId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileData = fileData;
    }

    public ProjectDocument(String projectId, String fileName, String fileType, byte[] fileData, Timestamp uploadedOn) {

        this.projectId = projectId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileData = fileData;
        this.uploadedOn = uploadedOn;
    }

    // --- Getters and Setters ---

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public Timestamp getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(Timestamp uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    @Override
    public String toString() {
        return "ProjectDocument{" +
                "documentId=" + documentId +
                ", projectId=" + projectId +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", uploadedOn=" + uploadedOn +
                '}';
    }
}
