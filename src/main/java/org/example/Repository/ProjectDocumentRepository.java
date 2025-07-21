package org.example.Repository;

import java.sql.Connection;
import org.example.Model.ProjectDocument;
import org.example.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository Layer: ProjectDocumentRepository
 * Handles all database operations related to project documents.
 * This class provides methods to upload, retrieve, and delete project documents.
 */
public class ProjectDocumentRepository {
    private static final Logger logger = Logger.getLogger(ProjectDocumentRepository.class.getName());

    private static final String INSERT_SQL =
            "INSERT INTO project_document (project_id, file_name, file_type, file_data) VALUES (?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM project_document WHERE document_id = ?";

    private static final String SELECT_BY_PROJECT_ID_SQL =
            "SELECT * FROM project_document WHERE project_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM project_document WHERE document_id = ?";



    // Check if a project exists by its ID.
    public boolean projectExists(String projectId) {
        String sql = "SELECT 1 FROM project WHERE project_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, projectId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();  // true if any record exists
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Upload a document linked to a project.
     */
    public void uploadDocument(ProjectDocument document) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            stmt.setString(1, document.getProjectId());
            stmt.setString(2, document.getFileName());
            stmt.setString(3, document.getFileType());
            stmt.setBytes(4, document.getFileData());

            stmt.executeUpdate();

            System.out.println("Document uploaded successfully.");

        } catch (SQLException e) {
               logger.log(Level.SEVERE, "Error uploading document", e);
        }
    }


    /**
     * Retrieve a single document by document ID.
     */
    public ProjectDocument getDocumentById(String documentId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setString(1, documentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ProjectDocument doc = new ProjectDocument();
                    doc.setDocumentId(rs.getString("document_id"));
                    doc.setProjectId(rs.getString("project_id"));
                    doc.setFileName(rs.getString("file_name"));
                    doc.setFileType(rs.getString("file_type"));
                    doc.setFileData(rs.getBytes("file_data"));
                    doc.setUploadedOn(rs.getTimestamp("uploaded_on"));
                    return doc;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Retrieve all documents for a project.
     */
    public static List<ProjectDocument> getDocumentsByProjectId(String projectId) {
        List<ProjectDocument> documents = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_PROJECT_ID_SQL)) {

            stmt.setString(1, projectId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProjectDocument doc = new ProjectDocument();
                    doc.setDocumentId(rs.getString("document_id"));
                    doc.setProjectId(rs.getString("project_id"));
                    doc.setFileName(rs.getString("file_name"));
                    doc.setFileType(rs.getString("file_type"));
                    doc.setFileData(rs.getBytes("file_data"));
                    doc.setUploadedOn(rs.getTimestamp("uploaded_on"));
                    documents.add(doc);
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving documents for project ID: " + projectId, e);
        }
        return documents;
    }


    /**
     * Delete a document by document ID.
     */
    public boolean deleteDocument(String documentId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setString(1, documentId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
