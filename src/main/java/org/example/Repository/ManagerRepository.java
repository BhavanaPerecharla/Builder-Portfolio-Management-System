package org.example.Repository;

import org.example.Model.Address;
import org.example.Model.Manager;
import org.example.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManagerRepository {
    private static final Logger logger = Logger.getLogger(ManagerRepository.class.getName());

    // INSERT Manager (with Address Handling)
    public void insertManager(Manager manager, Address address) {
        String insertManagerSQL = "INSERT INTO manager (manager_Name, manager_Email, manager_Password, manager_Contact, pm_status, builder_Id, address_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            String addressId = AddressRepository.insertAddress(address);

            stmt = connection.prepareStatement(insertManagerSQL);
            stmt.setString(1, manager.getManagerName());
            stmt.setString(2, manager.getManagerEmail());
            stmt.setString(3, manager.getManagerPassword());
            stmt.setString(4, manager.getManagerContact());
            stmt.setString(5, manager.getPmStatus());
            stmt.setString(6, manager.getBuilderId());
            stmt.setString(7, addressId);

            stmt.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting manager", e);
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Rollback failed", ex);
            }
        } finally {
            DBConnection.closeStatement(stmt);
            DBConnection.closeConnection(connection);
        }
    }

    // GET Manager by Email
    public static Manager getManagerByEmail(String email) {
        String sql = "SELECT * FROM manager WHERE manager_Email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Manager manager = new Manager();
                manager.setManagerId(rs.getString("manager_Id"));
                manager.setManagerName(rs.getString("manager_Name"));
                manager.setManagerEmail(rs.getString("manager_Email"));
                manager.setManagerPassword(rs.getString("manager_Password"));
                manager.setManagerContact(rs.getString("manager_Contact"));
                manager.setPmStatus(rs.getString("pm_status"));
                manager.setBuilderId(rs.getString("builder_Id"));
                manager.setAddressId(rs.getString("address_id"));

                return manager;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving manager by email", e);
        }
        return null;
    }

    // GET Manager by ID (optional)
    public static Manager getManagerById(String managerId) {
        String sql = "SELECT * FROM manager WHERE manager_Id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, managerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Manager manager = new Manager();
                manager.setManagerId(rs.getString("manager_Id"));
                manager.setManagerName(rs.getString("manager_Name"));
                manager.setManagerEmail(rs.getString("manager_Email"));
                manager.setManagerPassword(rs.getString("manager_Password"));
                manager.setManagerContact(rs.getString("manager_Contact"));
                manager.setPmStatus(rs.getString("pm_status"));
                manager.setBuilderId(rs.getString("builder_Id"));
                manager.setAddressId(rs.getString("address_id"));

                return manager;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving manager by ID", e);
        }
        return null;
    }

    // UPDATE Manager (with Address Handling)
    public static boolean updateManager(Manager manager, Address address) {
        Connection connection = null;
        PreparedStatement updateManagerStmt = null;
        boolean updated = false;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            // Update manager table
            String updateManagerSQL = "UPDATE manager SET manager_Name = ?, manager_Email = ?, manager_Password = ?, manager_Contact = ?, pm_status = ?, builder_Id = ? WHERE manager_Id = ?";
            updateManagerStmt = connection.prepareStatement(updateManagerSQL);

            updateManagerStmt.setString(1, manager.getManagerName());
            updateManagerStmt.setString(2, manager.getManagerEmail());
            updateManagerStmt.setString(3, manager.getManagerPassword());
            updateManagerStmt.setString(4, manager.getManagerContact());
            updateManagerStmt.setString(5, manager.getPmStatus());
            updateManagerStmt.setString(6, manager.getBuilderId());
            updateManagerStmt.setString(7, manager.getManagerId());

            int managerRows = updateManagerStmt.executeUpdate();

            // Update address via AddressRepository
            boolean addressUpdated = AddressRepository.updateAddress(address);

            if (managerRows > 0 && addressUpdated) {
                connection.commit();
                updated = true;
            } else {
                connection.rollback();
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating manager", e);
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Rollback failed", ex);
            }
        } finally {
            DBConnection.closeStatement(updateManagerStmt);
            DBConnection.closeConnection(connection);
        }

        return updated;
    }

    // DELETE Manager (and Address)
    public void deleteManager(String managerId) {
        String selectAddressIdSQL = "SELECT address_id FROM manager WHERE manager_Id = ?";
        String deleteManagerSQL = "DELETE FROM manager WHERE manager_Id = ?";

        Connection connection = null;
        PreparedStatement selectStmt = null;
        PreparedStatement deleteManagerStmt = null;
        String addressId = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            // Fetch address_id
            selectStmt = connection.prepareStatement(selectAddressIdSQL);
            selectStmt.setString(1, managerId);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                addressId = rs.getString("address_id");
            }

            // Delete manager
            deleteManagerStmt = connection.prepareStatement(deleteManagerSQL);
            deleteManagerStmt.setString(1, managerId);
            deleteManagerStmt.executeUpdate();

            // Delete address via AddressRepository
            if (addressId != null) {
                AddressRepository.deleteAddress(addressId);
            }

            connection.commit();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting manager", e);
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Rollback failed", ex);
            }
        } finally {
            DBConnection.closeStatement(selectStmt);
            DBConnection.closeStatement(deleteManagerStmt);
            DBConnection.closeConnection(connection);
        }
    }

    // UPDATE Password
    public static boolean updatePassword(String email, String hashedPassword) {
        String sql = "UPDATE manager SET manager_Password = ? WHERE manager_Email = ?";

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

    public static boolean updateManagerStatus(Manager manager) {
        String sql = "UPDATE manager SET pm_status = ? WHERE manager_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, manager.getPmStatus());
            stmt.setString(2, manager.getManagerId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.out.println("❌ Error updating manager status: " + e.getMessage());
            return false;
        }
    }


    // GET All Managers
    public List<Manager> getAllManagers() {
        List<Manager> managers = new ArrayList<>();
        String sql = "SELECT * FROM manager";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Manager manager = new Manager();
                manager.setManagerId(rs.getString("manager_Id"));
                manager.setManagerName(rs.getString("manager_Name"));
                manager.setManagerEmail(rs.getString("manager_Email"));
                manager.setManagerPassword(rs.getString("manager_Password"));
                manager.setManagerContact(rs.getString("manager_Contact"));
                manager.setPmStatus(rs.getString("pm_status"));
                manager.setBuilderId(rs.getString("builder_Id"));
                manager.setAddressId(rs.getString("address_id"));

                managers.add(manager);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving managers list", e);
        }

        return managers;
    }
}
