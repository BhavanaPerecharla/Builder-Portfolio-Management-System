package org.example.Repository;

import org.example.Constants.BuilderSQLConstants;
import org.example.Model.Address;
import org.example.Model.Builder;
import org.example.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository Layer: BuilderRepository
 * Handles all database operations related to builders and their addresses.
 * This class follows the Singleton pattern to ensure a single instance is used throughout the application.
 */

public class BuilderRepository {

    private static final Logger logger = Logger.getLogger(BuilderRepository.class.getName());
    private static BuilderRepository instance;

    // Private constructor to prevent instantiation
    private BuilderRepository() {}

    //  Singleton instance retrieval method
    public static BuilderRepository getInstance() {
        if (instance == null) {
            instance = new BuilderRepository();
        }
        return instance;
    }

    // Get builder ID using email
    public static String getBuilderIdByEmail(String email) {
        String builderId = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(BuilderSQLConstants.GET_BUILDER_ID_BY_EMAIL)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                builderId = rs.getString("builder_id");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving builder ID by email: " + email, e);
        }
        return builderId;
    }


    // ✅ Get builder by ID (including address)
    public static Builder getBuilderById(String builderId) {
        Builder builder = null;
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.getConnection();
            stmt = connection.prepareStatement(BuilderSQLConstants.GET_BUILDER_BY_ID);
            stmt.setString(1, builderId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Address address = new Address(
                        rs.getString("address_Line1"),
                        rs.getString("city"),
                        rs.getString("states"),
                        rs.getString("zip_Code"),
                        rs.getString("country")
                );
                address.setAddressId(rs.getString("address_id"));

                builder = new Builder(
                        rs.getString("builder_name"),
                        rs.getString("builder_email"),
                        rs.getString("builder_password"),
                        rs.getString("builder_contact"),
                        rs.getString("address_id")
                );
                builder.setBuilderId(rs.getString("builder_id"));
                builder.setAddress(address);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving builder by ID: " + builderId, e);
            // Optionally rethrow or handle the exception as needed
        } finally {
            DBConnection.closeResultSet(rs);
            DBConnection.closeStatement(stmt);
            DBConnection.closeConnection(connection);
        }

        return builder;
    }

    // Get builder by email
    public static Builder getBuilderByEmail(String email) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(BuilderSQLConstants.GET_BUILDER_BY_EMAIL)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Builder builder = new Builder();
                builder.setBuilderId(rs.getString("builder_id"));
                builder.setBuilderName(rs.getString("builder_name"));
                builder.setBuilderEmail(rs.getString("builder_email"));
                builder.setBuilderContact(rs.getString("builder_contact"));
                builder.setBuilderPassword(rs.getString("builder_password"));
                builder.setAddressId(rs.getString("address_id"));

                return builder;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving builder by email", e);
        }
        return null;
    }

    // Get all builders (with addresses)
    public List<Builder> getAllBuilders() {
        List<Builder> builders = new ArrayList<>();
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.getConnection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(BuilderSQLConstants.GET_ALL_BUILDERS);

            while (rs.next()) {
                Address address = new Address(
                        rs.getString("address_Line1"),
                        rs.getString("city"),
                        rs.getString("states"),
                        rs.getString("zip_Code"),
                        rs.getString("country")
                );
                address.setAddressId(rs.getString("address_id"));

                Builder builder = new Builder(
                        rs.getString("builder_Name"),
                        rs.getString("builder_Email"),
                        rs.getString("builder_password"),
                        rs.getString("builder_Contact"),
                        rs.getString("address_id")
                );
                builder.setBuilderId(rs.getString("builder_id"));
                builder.setAddress(address);

                builders.add(builder);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving all builders", e);
        } finally {
            DBConnection.closeResultSet(rs);
            DBConnection.closeStatement(stmt);
            DBConnection.closeConnection(connection);
        }

        return builders;
    }

    // Update builder and address
    public static boolean updateBuilder(Builder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("Builder cannot be null.");
        }

        if (builder.getAddress() == null) {
            throw new IllegalArgumentException("Builder address cannot be null.");
        }

        Connection connection = null;
        PreparedStatement updateBuilderStmt = null;
        PreparedStatement updateAddressStmt = null;
        boolean updated = false;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false); // Transaction begins

            updateBuilderStmt = connection.prepareStatement(BuilderSQLConstants.UPDATE_BUILDER);
            updateBuilderStmt.setString(1, builder.getBuilderName());
            updateBuilderStmt.setString(2, builder.getBuilderEmail());
            updateBuilderStmt.setString(3, builder.getBuilderPassword());
            updateBuilderStmt.setString(4, builder.getBuilderContact());
            updateBuilderStmt.setString(5, builder.getBuilderId());

            int builderRows = updateBuilderStmt.executeUpdate();

            updateAddressStmt = connection.prepareStatement(BuilderSQLConstants.UPDATE_ADDRESS);
            updateAddressStmt.setString(1, builder.getAddress().getAddressLine1());
            updateAddressStmt.setString(2, builder.getAddress().getCity());
            updateAddressStmt.setString(3, builder.getAddress().getStates());
            updateAddressStmt.setString(4, builder.getAddress().getZipCode());
            updateAddressStmt.setString(5, builder.getAddress().getCountry());
            updateAddressStmt.setString(6, builder.getAddress().getAddressId());

            int addressRows = updateAddressStmt.executeUpdate();

            if (builderRows > 0 && addressRows > 0) {
                connection.commit();
                updated = true;
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.log(Level.SEVERE, "Error rolling back transaction", rollbackEx);
            }
            logger.log(Level.SEVERE, "Error updating builder or address", e);
        } finally {
            DBConnection.closeStatement(updateBuilderStmt);
            DBConnection.closeStatement(updateAddressStmt);
            DBConnection.closeConnection(connection);
        }

        return updated;
    }

    // Update only the password
    public static boolean updatePassword(String email, String hashedPassword) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(BuilderSQLConstants.UPDATE_PASSWORD)) {

            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating password for email: " + email, e);
            return false;
        }
    }
}
