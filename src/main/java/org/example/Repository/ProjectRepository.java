package org.example.Repository;


import org.example.Model.Project;
import org.example.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ProjectRepository {
    private static final Logger logger = Logger.getLogger(ProjectRepository.class.getName());
    public static List<Project> getProjectsByBuilderEmail(String builderEmail) {
        List<Project> projects = new ArrayList<>();

        String query = "SELECT p.* FROM project p " +
                "JOIN builder b ON p.builder_id = b.builder_id " +
                "WHERE b.builder_email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, builderEmail);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    projects.add(mapProject(rs));
                }
            }

        }catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching projects by builder email: " + builderEmail, e);
        }


        return projects;
    }

    public static boolean insertProject(Project project) {
        String query = "INSERT INTO project (project_id, project_name, project_description, project_start_date, " +
                "project_est_complete_date, project_actual_complete_date, project_status, manager_id, client_id, builder_id, estimated_cost) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, project.getProjectId());
            stmt.setString(2, project.getProjectName());
            stmt.setString(3, project.getProjectDescription());
            stmt.setDate(4, new java.sql.Date(project.getProjectStartDate().getTime()));
            stmt.setDate(5, (project.getProjectEstCompleteDate() != null) ? new java.sql.Date(project.getProjectEstCompleteDate().getTime()) : null);
            stmt.setDate(6, (project.getProjectActualCompleteDate() != null) ? new java.sql.Date(project.getProjectActualCompleteDate().getTime()) : null);
            stmt.setString(7, project.getProjectStatus());
            stmt.setString(8, project.getManagerId());
            stmt.setString(9, project.getClientId());
            stmt.setString(10, project.getBuilderId());
            stmt.setBigDecimal(11, project.getEstimatedCost());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting project ID: " + project.getProjectId(), e);
            return false;
        }
    }
    public static List<Project> getProjectsByClientId(String clientId) {
        List<Project> projects = new ArrayList<>();

        String sql = "SELECT * FROM project WHERE Client_Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Project project = new Project();
                project.setProjectId(rs.getString("project_Id"));
                project.setProjectName(rs.getString("project_Name"));
                project.setProjectDescription(rs.getString("project_Description"));
                project.setProjectStartDate(rs.getDate("project_Start_Date"));
                project.setProjectEstCompleteDate(rs.getDate("project_Est_Complete_Date"));
                project.setProjectActualCompleteDate(rs.getDate("project_Actual_Complete_Date"));
                project.setProjectStatus(rs.getString("project_Status"));
                project.setManagerId(rs.getString("Manager_Id"));
                project.setClientId(rs.getString("Client_Id"));
                project.setBuilderId(rs.getString("Builder_Id"));
                project.setEstimatedCost(rs.getBigDecimal("estimated_Cost"));

                projects.add(project);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching projects for client ID: " + clientId, e);
        }

        return projects;
    }

    public static boolean updateProjectStatus(String projectId, String newStatus) {
        String query = "UPDATE project SET project_status = ? WHERE project_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newStatus);
            stmt.setString(2, projectId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating status for project ID: " + projectId, e);
            return false;
        }
    }

    public static boolean deleteProject(String projectId) {
        String query = "DELETE FROM project WHERE project_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, projectId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting project with ID: " + projectId, e);
            return false;
        }
    }

    public static int getNextProjectNumberForBuilder(String builderId) {
        String query = "SELECT COUNT(*) AS project_count FROM project WHERE builder_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, builderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("project_count") + 1;
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching project count for builder ID: " + builderId, e);
        }

        return 1;  // Default to PRJ001 if no projects found
    }
    public static Project getProjectById(String projectId) {
        String query = "SELECT * FROM project WHERE project_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, projectId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Project project = new Project();
                project.setProjectId(rs.getString("project_id"));
                project.setProjectName(rs.getString("project_name"));
                project.setProjectDescription(rs.getString("project_description"));
                project.setProjectStartDate(rs.getDate("project_start_date"));
                project.setProjectEstCompleteDate(rs.getDate("project_est_complete_date"));
                project.setProjectActualCompleteDate(rs.getDate("project_actual_complete_date"));
                project.setProjectStatus(rs.getString("project_status"));
                project.setManagerId(rs.getString("manager_id"));
                project.setClientId(rs.getString("client_id"));
                project.setBuilderId(rs.getString("builder_id"));
                project.setEstimatedCost(rs.getBigDecimal("estimated_cost"));
                return project;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching project by ID: " + projectId, e);
        }
        return null;
    }
    public static List<Project> getProjectsByClientEmail(String clientEmail) {
        List<Project> projects = new ArrayList<>();

        String sql = "SELECT * FROM project WHERE client_id IN (SELECT client_id FROM client WHERE client_email = ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, clientEmail);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Project project = new Project();
                    project.setProjectId(rs.getString("project_id"));
                    project.setProjectName(rs.getString("project_name"));
                    project.setProjectDescription(rs.getString("project_description"));
                    project.setProjectStartDate(rs.getDate("project_start_date"));
                    project.setProjectEstCompleteDate(rs.getDate("project_est_complete_date"));
                    project.setProjectActualCompleteDate(rs.getDate("project_actual_complete_date"));
                    project.setProjectStatus(rs.getString("project_status"));
                    project.setManagerId(rs.getString("manager_id"));
                    project.setClientId(rs.getString("client_id"));
                    project.setBuilderId(rs.getString("builder_id"));
                    project.setEstimatedCost(rs.getBigDecimal("estimated_cost"));

                    projects.add(project);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }


    public static boolean updateProject(Project project) {
        String sql = "UPDATE project SET project_name = ?, project_description = ?, project_start_date = ?, " +
                "project_est_complete_date = ?, project_actual_complete_date = ?, project_status = ?, " +
                "manager_id = ?, client_id = ?, builder_id = ?, estimated_cost = ? WHERE project_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, project.getProjectName());
            stmt.setString(2, project.getProjectDescription());
            stmt.setDate(3, new Date(project.getProjectStartDate().getTime()));
            stmt.setDate(4, project.getProjectEstCompleteDate() != null ? new Date(project.getProjectEstCompleteDate().getTime()) : null);
            stmt.setDate(5, project.getProjectActualCompleteDate() != null ? new Date(project.getProjectActualCompleteDate().getTime()) : null);
            stmt.setString(6, project.getProjectStatus());
            stmt.setString(7, project.getManagerId());
            stmt.setString(8, project.getClientId());
            stmt.setString(9, project.getBuilderId());
            stmt.setBigDecimal(10, project.getEstimatedCost());
            stmt.setString(11, project.getProjectId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // If update is successful

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating project with ID: " + project.getProjectId(), e);
        }

        return false;  // If update fails
    }
    // In ProjectRepository.java

    public static List<Project> getProjectsByManagerId(String managerId) {
        List<Project> projects = new ArrayList<>();

        String sql = "SELECT * FROM project WHERE manager_Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, managerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Project project = new Project();

                project.setProjectId(rs.getString("project_Id"));
                project.setProjectName(rs.getString("project_Name"));
                project.setProjectDescription(rs.getString("project_Description"));
                project.setProjectStartDate(rs.getDate("project_Start_Date"));
                project.setProjectEstCompleteDate(rs.getDate("project_Est_Complete_Date"));
                project.setProjectActualCompleteDate(rs.getDate("project_Actual_Complete_Date"));
                project.setProjectStatus(rs.getString("project_Status"));
                project.setManagerId(rs.getString("Manager_Id"));
                project.setClientId(rs.getString("Client_Id"));
                project.setBuilderId(rs.getString("Builder_Id"));
                project.setEstimatedCost(rs.getBigDecimal("estimated_Cost"));

                projects.add(project);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching projects by manager ID", e);
        }

        return projects;
    }

    public static boolean projectExists(String projectId) {
        String sql = "SELECT 1 FROM project WHERE project_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, projectId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();  // true if project exists
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private static Project mapProject(ResultSet rs) throws SQLException {
        Project project = new Project();

        project.setProjectId(rs.getString("project_id"));
        project.setProjectName(rs.getString("project_name"));
        project.setProjectDescription(rs.getString("project_description"));
        project.setProjectStartDate(rs.getDate("project_start_date"));
        project.setProjectEstCompleteDate(rs.getDate("project_est_complete_date"));
        project.setProjectActualCompleteDate(rs.getDate("project_actual_complete_date"));
        project.setProjectStatus(rs.getString("project_status"));
        project.setManagerId(rs.getString("manager_id"));
        project.setClientId(rs.getString("client_id"));
        project.setBuilderId(rs.getString("builder_id"));
        project.setEstimatedCost(rs.getBigDecimal("estimated_cost"));

        return project;
    }


}
