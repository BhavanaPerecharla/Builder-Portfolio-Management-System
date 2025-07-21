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
    private static ManagerRepository instance;

    private ManagerRepository() {}

    public static ManagerRepository getInstance() {
        if (instance == null) {
            instance = new ManagerRepository();
        }
        return instance;
    }

    // GET ALL MANAGERS (WITH ADDRESS)
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
                        rs.getString("manager_password"),
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

    // INSERT MANAGER (WITH ADDRESS)
    public void insertManager(Manager manager, Address address) {
        String insertManagerSQL = "INSERT INTO manager (manager_Name, manager_Email, manager_Password, manager_Contact, pm_status, builder_Id, address_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            String addressId = AddressRepository.insertAddress(address);

            try (PreparedStatement stmt = connection.prepareStatement(insertManagerSQL)) {
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
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting manager", e);
        }
    }

    // GET MANAGER BY EMAIL
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

    // DELETE MANAGER (AND ADDRESS)
    public void deleteManager(String managerId) {
        String selectAddressSQL = "SELECT address_id FROM manager WHERE manager_Id = ?";
        String deleteManagerSQL = "DELETE FROM manager WHERE manager_Id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectAddressSQL);
             PreparedStatement deleteStmt = connection.prepareStatement(deleteManagerSQL)) {

            connection.setAutoCommit(false);

            String addressId = null;

            selectStmt.setString(1, managerId);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                addressId = rs.getString("address_id");
            }

            deleteStmt.setString(1, managerId);
            deleteStmt.executeUpdate();

            if (addressId != null) {
                AddressRepository.deleteAddress(addressId);
            }

            connection.commit();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting manager", e);
        }
    }

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
