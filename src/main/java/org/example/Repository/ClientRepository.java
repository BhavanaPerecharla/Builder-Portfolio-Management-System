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

    // Insert Client (Address should already exist)
    public void insertClient(Client client, Address address) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String addressId = AddressRepository.insertAddress(address);  // ✅ Insert address via AddressRepository

            String sql = "INSERT INTO client (client_Name, client_Email, client_Password, client_Contact, client_type, address_id) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, client.getClientName());
            stmt.setString(2, client.getClientEmail());
            stmt.setString(3, client.getClientPassword());
            stmt.setString(4, client.getClientContact());
            stmt.setString(5, client.getClientType());
            stmt.setString(6, addressId);

            stmt.executeUpdate();
            conn.commit();

            logger.info("Client inserted successfully.");

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            logger.log(Level.SEVERE, "Error inserting client", e);
        } finally {
            DBConnection.closeStatement(stmt);
            DBConnection.closeConnection(conn);
        }
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

    // Get All Clients
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching all clients", e);
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
                rollbackEx.printStackTrace();
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

    // Delete Client and Address (Transactional)
    public static void deleteClient(String clientId) {
        String fetchAddressQuery = "SELECT address_id FROM client WHERE client_Id = ?";
        String deleteClientQuery = "DELETE FROM client WHERE client_Id = ?";

        Connection conn = null;
        PreparedStatement fetchStmt = null;
        PreparedStatement deleteClientStmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            fetchStmt = conn.prepareStatement(fetchAddressQuery);
            fetchStmt.setString(1, clientId);
            rs = fetchStmt.executeQuery();

            String addressId = null;
            if (rs.next()) {
                addressId = rs.getString("address_id");
            }

            deleteClientStmt = conn.prepareStatement(deleteClientQuery);
            deleteClientStmt.setString(1, clientId);
            deleteClientStmt.executeUpdate();

            AddressRepository addressRepo = new AddressRepository();
            addressRepo.deleteAddress(addressId);

            conn.commit();

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            logger.log(Level.SEVERE, "Error deleting client", e);

        } finally {
            DBConnection.closeResultSet(rs);
            DBConnection.closeStatement(fetchStmt);
            DBConnection.closeStatement(deleteClientStmt);
            DBConnection.closeConnection(conn);
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
