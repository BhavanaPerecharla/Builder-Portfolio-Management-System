package org.example.Repository;


import org.example.Model.Client;
import org.example.Model.Address;
import org.example.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRepository {
    private static final Logger logger = Logger.getLogger(ClientRepository.class.getName());
    private static ClientRepository instance;

    private ClientRepository() {
        // private constructor to prevent instantiation
    }

    public static ClientRepository getInstance() {
        if (instance == null) {
            instance = new ClientRepository();
        }
        return instance;
    }

    // Get Client By Email
    public static Client getClientByEmail(String email) {
        String sql = "SELECT * FROM client WHERE client_Email = ?";
        Client client = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                client = mapResultSetToClient(rs);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching client by email", e);
        }

        return client;
    }

    // Get Client By ID
    public static Client getClientById(String clientId) {
        String sql = "SELECT * FROM client WHERE client_Id = ?";
        Client client = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, clientId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                client = mapResultSetToClient(rs);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching client by ID", e);
        }

        return client;
    }


    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT c.client_id, c.client_name, c.client_email, c.client_contact, c.client_type, " +
                "a.address_id, a.address_Line1, a.city, a.states, a.zip_Code, a.country " +
                "FROM client c JOIN address a ON c.address_id = a.address_id";

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

                Client client = new Client();
                client.setClientId(rs.getString("client_Id"));
                client.setClientName(rs.getString("client_Name"));
                client.setClientEmail(rs.getString("client_Email"));
                client.setClientContact(rs.getString("client_Contact"));
                client.setClientType(rs.getString("client_type"));
                client.setAddress(address);

                clients.add(client);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving client details", e);
        }

        return clients;
    }



    public static boolean updateClient(Client client, Address address) {
        Connection conn = null;
        PreparedStatement updateClientStmt = null;
        boolean success = false;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Update client
            String sql = "UPDATE client SET client_Name = ?, client_Email = ?, client_Password = ?, client_Contact = ?, client_type = ? WHERE client_Id = ?";
            updateClientStmt = conn.prepareStatement(sql);

            updateClientStmt.setString(1, client.getClientName());
            updateClientStmt.setString(2, client.getClientEmail());
            updateClientStmt.setString(3, client.getClientPassword());
            updateClientStmt.setString(4, client.getClientContact());
            updateClientStmt.setString(5, client.getClientType());
            updateClientStmt.setString(6, client.getClientId());

            int clientRows = updateClientStmt.executeUpdate();

            boolean addressUpdated = AddressRepository.updateAddress(address);  // ✅ Address updated via AddressRepository

            if (clientRows > 0 && addressUpdated) {
                conn.commit();
                success = true;
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                logger.log(Level.SEVERE, "Error rolling back transaction", rollbackEx);
            }
            logger.log(Level.SEVERE, "Error updating client", e);
        } finally {
            DBConnection.closeStatement(updateClientStmt);
            DBConnection.closeConnection(conn);
        }

        return success;
    }

    public static String getClientIdByEmail(String email) {
        String sql = "SELECT client_Id FROM client WHERE client_Email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("client_Id");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching client ID by email", e);
        }

        return null;
    }

    // Update Password Only
    public static boolean updatePassword(String email, String hashedPassword) {
        String sql = "UPDATE client SET client_Password = ? WHERE client_Email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating client password", e);
            return false;
        }
    }


    // Helper method
    private static Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setClientId(rs.getString("client_Id"));
        client.setClientName(rs.getString("client_Name"));
        client.setClientEmail(rs.getString("client_Email"));
        client.setClientPassword(rs.getString("client_Password"));
        client.setClientContact(rs.getString("client_Contact"));
        client.setClientType(rs.getString("client_type"));
        client.setAddressId(rs.getString("address_id"));
        return client;
    }
}
