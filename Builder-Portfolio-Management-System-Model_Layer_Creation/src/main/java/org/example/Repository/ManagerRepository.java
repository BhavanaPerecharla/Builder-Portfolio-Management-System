package org.example.Repository;


import org.example.Model.Address;
import org.example.Model.Manager;
import org.example.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository class for handling Manager-related database operations.
 * Follows the Repository pattern as part of the Data Access Layer in a 3-layered architecture.
 */
public class ManagerRepository {

    private static final Logger logger = Logger.getLogger(ManagerRepository.class.getName());
    private static ManagerRepository instance;

    // Singleton instance
    private ManagerRepository() {}


    // Private constructor to prevent direct instantiation
    public static ManagerRepository getInstance() {
        if (instance == null) {
            instance = new ManagerRepository();
        }
        return instance;
    }

    // GET ALL MANAGERS (WITH ADDRESS)
    /**
     * Retrieves all managers along with their addresses.
     * @return List of Manager objects.
     */
    public List<Manager> getAllManagers() {
        List<Manager> managers = new ArrayList<>();

        String query = "SELECT m.manager_id, m.manager_name, m.manager_email, m.manager_password, m.manager_contact, " +
                "m.pm_status, m.builder_id, a.address_id, a.address_Line1, a.city, a.states, a.zip_Code, a.country " +
                "FROM manager m JOIN address a ON m.address_id = a.address_id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Address address = new Address(
                        rs.getString("address_Line1"),
                        rs.getString("city"),
                        rs.getString("states"),
                        rs.getString("zip_Code"),
                        rs.getString("country")
                );
                address.setAddressId(rs.getString("address_id"));

                Manager manager = new Manager(
                        rs.getString("manager_Name"),
                        rs.getString("manager_Email"),
                        rs.getString("manager_Password"),
                        rs.getString("manager_Contact"),
                        rs.getString("pm_status"),
                        rs.getString("builder_id"),
                        rs.getString("address_id")
                );
                manager.setManagerId(rs.getString("manager_id"));

                managers.add(manager);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving managers", e);
        }

        return managers;
    }


    // GET MANAGER BY EMAIL
    /**
     * Retrieves a manager by their email.
     * @param email Manager email
     * @return Manager object or null
     */
    public static Manager getManagerByEmail(String email) {
        String sql = "SELECT * FROM manager WHERE manager_Email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return buildManagerFromResultSet(rs);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving manager by email", e);
        }
        return null;
    }


    /**
     * Retrieves a manager by their ID.
     * @param managerId Manager ID
     * @return Manager object or null
     */
    // GET MANAGER BY ID
    public static Manager getManagerById(String managerId) {
        String sql = "SELECT * FROM manager WHERE manager_Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, managerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return buildManagerFromResultSet(rs);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving manager by ID", e);
        }
        return null;
    }

    /**
     * Updates manager and corresponding address in a transaction.
     * @param manager Manager object with updated data
     * @param address Address object with updated data
     * @return true if successful, false otherwise
     */
    // UPDATE MANAGER (WITH ADDRESS)
    public static boolean updateManager(Manager manager, Address address) {
        String updateSQL = "UPDATE manager SET manager_Name = ?, manager_Email = ?, manager_Password = ?, manager_Contact = ?, pm_status = ?, builder_Id = ? WHERE manager_Id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(updateSQL)) {

            connection.setAutoCommit(false);

            stmt.setString(1, manager.getManagerName());
            stmt.setString(2, manager.getManagerEmail());
            stmt.setString(3, manager.getManagerPassword());
            stmt.setString(4, manager.getManagerContact());
            stmt.setString(5, manager.getPmStatus());
            stmt.setString(6, manager.getBuilderId());
            stmt.setString(7, manager.getManagerId());

            int rows = stmt.executeUpdate();
            boolean addressUpdated = AddressRepository.updateAddress(address);

            if (rows > 0 && addressUpdated) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating manager", e);
            return false;
        }
    }



    /**
     * Retrieves the manager ID associated with the given email.
     * @param email Manager email
     * @return manager_Id as String
     */
    public static String getManagerIdByEmail(String email) {
        String sql = "SELECT manager_Id FROM manager WHERE manager_Email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("manager_Id");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving manager ID by email", e);
        }
        return null;
    }



    /**
     * Updates the manager's password.
     * @param email Manager email
     * @param hashedPassword New hashed password
     * @return true if updated, false otherwise
     */
    // UPDATE PASSWORD
    public static boolean updatePassword(String email, String hashedPassword) {
        String sql = "UPDATE manager SET manager_Password = ? WHERE manager_Email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating password", e);
            return false;
        }
    }

    /**
     * Updates the manager's status (pm_status).
     * @param manager Manager object with new status
     * @return true if successful, false otherwise
     */
    // UPDATE MANAGER STATUS
    public static boolean updateManagerStatus(Manager manager) {
        String sql = "UPDATE manager SET pm_status = ? WHERE manager_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, manager.getPmStatus());
            stmt.setString(2, manager.getManagerId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating manager status", e);
            return false;
        }
    }

    /**
     * Utility method to construct a Manager object from a ResultSet.
     * @param rs ResultSet from query
     * @return Manager object
     * @throws SQLException if column parsing fails
     */
    // HELPER METHOD
    private static Manager buildManagerFromResultSet(ResultSet rs) throws SQLException {
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
}
