package org.example.Model;

import java.sql.Timestamp;
/**
 * Represents a document uploaded for a specific project.
 * Supports file metadata and content storage.
 */

public class ProjectDocument {

    private String documentId;
    private String projectId;
    private String fileName;
    private String fileType;
    private byte[] fileData;
    private Timestamp uploadedOn;

    // Default constructor
    public ProjectDocument() {
    }

   // Parameterized constructor to initialize all fields
    public ProjectDocument(String projectId, String fileName, String fileType, byte[] fileData, Timestamp uploadedOn) {

        this.projectId = projectId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileData = fileData;
        this.uploadedOn = uploadedOn;
    }

    //  Getters and Setters methods for each field

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

    // Override equals method to compare ProjectDocument objects based on documentId
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
