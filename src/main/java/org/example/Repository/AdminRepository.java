package org.example.Repository;

import org.example.Model.Admin;
import org.example.Model.Address;
import org.example.Util.DBConnection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository class responsible for interacting with the admin table in the database.
 */
public class AdminRepository {

    private static final Logger logger = Logger.getLogger(AdminRepository.class.getName());

    /**
     * Retrieves an Admin object based on email.
     *
     * @param email Admin email to look up.
     * @return Admin object if found, otherwise null.
     */
    public static Admin getAdminByEmail(String email) {
        String sql = "SELECT * FROM admin WHERE LOWER(admin_email) = LOWER(?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getString("admin_id"));
                admin.setAdminName(rs.getString("admin_name"));
                admin.setAdminEmail(rs.getString("admin_email"));
                admin.setAdminPassword(rs.getString("admin_password"));
                admin.setAdminContact(rs.getString("admin_contact"));
                admin.setAddressId(rs.getString("address_id"));
                return admin;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching admin by email: " + email, e);
        }

        return null;
    }

    /**
     * Updates admin and address details transactionally.
     *
     * @param admin   Updated Admin object.
     * @param address Updated Address object.
     * @return true if update is successful, false otherwise.
     */
    public static boolean updateAdmin(Admin admin, Address address) {
        String updateAdminSQL = "UPDATE admin SET admin_name = ?, admin_email = ?, admin_password = ?, admin_contact = ? WHERE admin_id = ?";
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


    /**
     * Updates password for the admin identified by email.
     *
     * @param email          Admin's email.
     * @param hashedPassword New hashed password.
     * @return true if update was successful, false otherwise.
     */
    public static boolean updatePassword(String email, String hashedPassword) {
        String sql = "UPDATE admin SET admin_password = ? WHERE admin_email = ?";

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
