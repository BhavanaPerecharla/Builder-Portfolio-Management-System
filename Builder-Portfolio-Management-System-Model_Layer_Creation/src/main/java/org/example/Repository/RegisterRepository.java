package org.example.Repository;

import org.example.Constants.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 Repository class to handle registration-related database operations.
 Includes methods for checking existing emails/builders, inserting addresses and users.
 */

public class RegisterRepository {

    /**
     Checks if the given email already exists in the corresponding role's table.
     @param conn Active database connection
    @param role Role of the user (ADMIN, CLIENT, BUILDER, MANAGER)
    @param email Email to be checked for existence
    @return true if email exists, false otherwise
    @throws Exception if database access fails
     */

    public static boolean emailExists(Connection conn, Role role, String email) throws Exception {
        String query = "SELECT 1 FROM " + role.getTableName() + " WHERE " + role.getEmailColumn() + " = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }


    /**

     Inserts a new address into the address table and returns the generated address_id.

     @param conn         Active database connection
     @param addressLine1 Address line 1
     @param city         City name
     @param state        State name
     @param zip          Zip code
     @param country      Country name
     @return Generated address_id as a String; null if insertion fails
     @throws Exception if database error occurs

     */

    public static String insertAddress(Connection conn, String addressLine1, String city, String state, String zip, String country) throws Exception {
        String sql = "INSERT INTO address (address_line1, city, states, zip_code, country) VALUES (?, ?, ?, ?, ?) RETURNING address_id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, addressLine1);
            ps.setString(2, city);
            ps.setString(3, state);
            ps.setString(4, zip);
            ps.setString(5, country);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString(1);
            return null;
        }
    }


    /**

     Checks if a builder exists in the database using their builderId.

     @param conn      Active database connection
     @param builderId ID of the builder
     @return true if builder exists; false otherwise
     @throws Exception if database error occurs

     */

    public static boolean isBuilderExists(Connection conn, String builderId) throws Exception {
        String sql = "SELECT 1 FROM builder WHERE builder_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, builderId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }


    /**

     Inserts a new user into the appropriate table based on the role.
     Each role has different fields and requirements.

     @param conn        Active database connection
     @param role        Role of the user
     @param name        User's name
     @param email       User's email
     @param password    User's password
     @param addressId   ID of the user's address
     @param contact     User's contact number
     @param clientType  (For CLIENT) Type of client
     @param pmStatus    (For MANAGER) Project manager status
     @param builderId   (For MANAGER) Builder they are associated with
     @throws Exception if database error occurs

     */
    public static void insertUser(Connection conn, Role role, String name, String email, String password, String addressId,
                                  String contact, String clientType, String pmStatus, String builderId) throws Exception {

        // Construct SQL insert statement based on the user's role
        String sql = switch (role) {
            case ADMIN -> "INSERT INTO admin (admin_name, admin_email, admin_password, address_id, admin_contact) VALUES (?, ?, ?, ?, ?)";
            case BUILDER -> "INSERT INTO builder (builder_name, builder_email, builder_password, address_id, builder_contact) VALUES (?, ?, ?, ?, ?)";
            case CLIENT -> "INSERT INTO client (client_name, client_email, client_password, address_id, client_contact, client_type) VALUES (?, ?, ?, ?, ?, ?)";
            case MANAGER -> "INSERT INTO manager (manager_name, manager_email, manager_password, address_id, manager_contact, pm_status, builder_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        };

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, addressId);
            ps.setString(5, contact);

            // Set additional fields depending on role
            if (role == Role.CLIENT) {
                ps.setString(6, clientType);
            } else if (role == Role.MANAGER) {
                ps.setString(6, pmStatus);
                ps.setString(7, builderId);
            }

            ps.executeUpdate();
        }
    }
}
