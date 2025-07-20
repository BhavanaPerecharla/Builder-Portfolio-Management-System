package org.example.Repository;


import org.example.Model.Admin;
import org.example.Model.Address;
import org.example.Util.DBConnection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminRepository {
    private static final Logger logger = Logger.getLogger(AdminRepository.class.getName());

    // Insert Admin with Address
    public static void insertAdmin(Admin admin, Address address) {
        String insertAdminSQL = "INSERT INTO admin (admin_Name, admin_Email, admin_Password, admin_Contact, address_id) VALUES (?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement adminStmt = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            String addressId = AddressRepository.insertAddress(address);

            adminStmt = connection.prepareStatement(insertAdminSQL);
            adminStmt.setString(1, admin.getAdminName());
            adminStmt.setString(2, admin.getAdminEmail());
            adminStmt.setString(3, admin.getAdminPassword());
            adminStmt.setString(4, admin.getAdminContact());
            adminStmt.setString(5, addressId);

            adminStmt.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting admin", e);
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Rollback failed", ex);
            }
        } finally {
            DBConnection.closeStatement(adminStmt);
            DBConnection.closeConnection(connection);
        }
    }

    // Get Admin by Email
    public static Admin getAdminByEmail(String email) {
        String sql = "SELECT * FROM admin WHERE admin_Email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getString("admin_Id"));
                admin.setAdminName(rs.getString("admin_Name"));
                admin.setAdminEmail(rs.getString("admin_Email"));
                admin.setAdminPassword(rs.getString("admin_Password"));
                admin.setAdminContact(rs.getString("admin_Contact"));
                admin.setAddressId(rs.getString("address_id"));

                return admin;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching admin by email", e);
        }

        return null;
    }

    // Update Admin and Address
    public static boolean updateAdmin(Admin admin, Address address) {
        String updateAdminSQL = "UPDATE admin SET admin_Name = ?, admin_Email = ?, admin_Password = ?, admin_Contact = ? WHERE admin_Id = ?";
        Connection connection = null;
        PreparedStatement adminStmt = null;
        boolean updated = false;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            adminStmt = connection.prepareStatement(updateAdminSQL);
            adminStmt.setString(1, admin.getAdminName());
            adminStmt.setString(2, admin.getAdminEmail());
            adminStmt.setString(3, admin.getAdminPassword());
            adminStmt.setString(4, admin.getAdminContact());
            adminStmt.setString(5, admin.getAdminId());

            int adminRows = adminStmt.executeUpdate();

            boolean addressUpdated = AddressRepository.updateAddress(address);

            if (adminRows > 0 && addressUpdated) {
                connection.commit();
                updated = true;
            } else {
                connection.rollback();
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating admin", e);
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Rollback failed", ex);
            }
        } finally {
            DBConnection.closeStatement(adminStmt);
            DBConnection.closeConnection(connection);
        }

        return updated;
    }

    // Delete Admin (and Address if needed)
    public static void deleteAdmin(String adminId) {
        String selectAddressIdSQL = "SELECT address_id FROM admin WHERE admin_Id = ?";
        String deleteAdminSQL = "DELETE FROM admin WHERE admin_Id = ?";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        PreparedStatement deleteAdminStmt = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            selectStmt = connection.prepareStatement(selectAddressIdSQL);
            selectStmt.setString(1, adminId);
            rs = selectStmt.executeQuery();

            String addressId = null;
            if (rs.next()) {
                addressId = rs.getString("address_id");
            }

            deleteAdminStmt = connection.prepareStatement(deleteAdminSQL);
            deleteAdminStmt.setString(1, adminId);
            deleteAdminStmt.executeUpdate();

            if (addressId != null) {
                AddressRepository.deleteAddress(addressId);
            }

            connection.commit();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting admin", e);
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Rollback failed during delete", ex);
            }
        } finally {
            DBConnection.closeResultSet(rs);
            DBConnection.closeStatement(selectStmt);
            DBConnection.closeStatement(deleteAdminStmt);
            DBConnection.closeConnection(connection);
        }
    }

    // Update Password Only
    public static boolean updatePassword(String email, String hashedPassword) {
        String sql = "UPDATE admin SET admin_Password = ? WHERE admin_Email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating password for admin email: " + email, e);
            return false;
        }
    }
}
