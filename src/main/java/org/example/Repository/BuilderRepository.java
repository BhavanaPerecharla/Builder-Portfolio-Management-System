package org.example.Repository;

import org.example.Model.Builder;
import org.example.Util.DBConnection;
import org.example.Model.Address;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class BuilderRepository {
    private static final Logger logger = Logger.getLogger(BuilderRepository.class.getName());
    // INSERT Builder with Address
    public void insertBuilder(Builder builder, Address address) {
        String insertBuilderQuery = "INSERT INTO builder (name, email, password, contact, address_id) VALUES (?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement builderStmt = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            String addressId = AddressRepository.insertAddress(address);

            builderStmt = connection.prepareStatement(insertBuilderQuery);
            builderStmt.setString(1, builder.getBuilderName());
            builderStmt.setString(2, builder.getBuilderEmail());
            builderStmt.setString(3, builder.getBuilderPassword());
            builderStmt.setString(4, builder.getBuilderContact());
            builderStmt.setString(5, addressId);

            builderStmt.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting builder", e);
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Rollback failed", ex);
            }
        } finally {
            DBConnection.closeStatement(builderStmt);
            DBConnection.closeConnection(connection);
        }
    }


    // GET Builder by ID
    public static Builder getBuilderById(String builderId) {
        String query = "SELECT b.builder_id, b.builder_name, b.builder_email, b.builder_password, b.builder_contact, " +
                "a.address_id, a.address_Line1, a.city, a.states, a.zip_Code, a.country " +
                "FROM builder b JOIN address a ON b.address_id = a.address_id " +
                "WHERE b.builder_id = ?";


        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Builder builder = null;

        try {
            connection = DBConnection.getConnection();
            stmt = connection.prepareStatement(query);
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
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResultSet(rs);
            DBConnection.closeStatement(stmt);
            DBConnection.closeConnection(connection);
        }
        return builder;
    }
    public static Builder getBuilderByEmail(String email) {
        String query = "SELECT * FROM builder WHERE builder_Email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

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


    // GET All Builders
    public List<Builder> getAllBuilders() {
        List<Builder> builders = new ArrayList<>();
        String query = "SELECT b.builder_id, b.name, b.email, b.password, b.contact, a.address_id, a.addressLine, a.city, a.states, a.zipCode, a.country " +
                "FROM builder b JOIN address a ON b.address_id = a.address_id";

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.getConnection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                Address address = new Address(
                        rs.getString("addressLine"),
                        rs.getString("city"),
                        rs.getString("states"),
                        rs.getString("zipCode"),
                        rs.getString("country")
                );
                address.setAddressId(rs.getString("address_id"));

                Builder builder = new Builder(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("contact"),
                        rs.getString("address_id")
                );
                builder.setBuilderId(rs.getString("builder_id"));

                builders.add(builder);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResultSet(rs);
            DBConnection.closeStatement(stmt);
            DBConnection.closeConnection(connection);
        }

        return builders;
    }

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
            connection.setAutoCommit(false); // Start transaction

            // 1. Update builder details
            String updateBuilderSQL = "UPDATE builder SET builder_name = ?, builder_email = ?, builder_password = ?, builder_contact = ? WHERE builder_id = ?";
            updateBuilderStmt = connection.prepareStatement(updateBuilderSQL);
            updateBuilderStmt.setString(1, builder.getBuilderName());
            updateBuilderStmt.setString(2, builder.getBuilderEmail());
            updateBuilderStmt.setString(3, builder.getBuilderPassword());
            updateBuilderStmt.setString(4, builder.getBuilderContact());
            updateBuilderStmt.setString(5, builder.getBuilderId());
            int builderRows = updateBuilderStmt.executeUpdate();

            // 2. Update address details
            String updateAddressSQL = "UPDATE address SET address_line1 = ?, city = ?, States = ?, zip_Code = ?, country = ? WHERE address_id = ?";
            updateAddressStmt = connection.prepareStatement(updateAddressSQL);
            updateAddressStmt.setString(1, builder.getAddress().getAddressLine1());
            updateAddressStmt.setString(2, builder.getAddress().getCity());
            updateAddressStmt.setString(3, builder.getAddress().getStates());
            updateAddressStmt.setString(4, builder.getAddress().getZipCode());
            updateAddressStmt.setString(5, builder.getAddress().getCountry());
            updateAddressStmt.setString(6, builder.getAddress().getAddressId());
            int addressRows = updateAddressStmt.executeUpdate();

            if (builderRows > 0 && addressRows > 0) {
                connection.commit(); // ✅ Commit only if both updates succeed
                updated = true;
            } else {
                connection.rollback(); // ❌ Rollback if any update fails
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            DBConnection.closeStatement(updateBuilderStmt);
            DBConnection.closeStatement(updateAddressStmt);
            DBConnection.closeConnection(connection);
        }

        return updated;
    }


    // DELETE Builder and Address
    public void deleteBuilder(String builderId) {
        String selectAddressIdQuery = "SELECT address_id FROM builder WHERE builder_id = ?";
        String deleteBuilderQuery = "DELETE FROM builder WHERE builder_id = ?";
        String deleteAddressQuery = "DELETE FROM address WHERE address_id = ?";

        Connection connection = null;
        PreparedStatement selectStmt = null;
        PreparedStatement deleteBuilderStmt = null;
        PreparedStatement deleteAddressStmt = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            selectStmt = connection.prepareStatement(selectAddressIdQuery);
            selectStmt.setString(1, builderId);
            rs = selectStmt.executeQuery();

            String addressId = null;
            if (rs.next()) {
                addressId = rs.getString("address_id");
            }

            deleteBuilderStmt = connection.prepareStatement(deleteBuilderQuery);
            deleteBuilderStmt.setString(1, builderId);
            deleteBuilderStmt.executeUpdate();

            deleteAddressStmt = connection.prepareStatement(deleteAddressQuery);
            deleteAddressStmt.setString(1, addressId);
            deleteAddressStmt.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResultSet(rs);
            DBConnection.closeStatement(selectStmt);
            DBConnection.closeStatement(deleteBuilderStmt);
            DBConnection.closeStatement(deleteAddressStmt);
            DBConnection.closeConnection(connection);
        }
    }
    public static boolean updatePassword(String email, String hashedPassword) {
        String sql = "UPDATE builder SET builder_password = ? WHERE builder_email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
