package org.example.Service;


import org.example.Util.DBConnection;
import org.example.Util.PasswordUtil;
import org.example.Constants.Role;

import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Service class responsible for handling user authentication-related logic,
 * including verifying user emails and authenticating credentials.
 */
public class LoginService {

    /**
     * Checks if a given email exists in the database for a specific role.
     *
     * @param email The email address to check.
     * @param role The user role (ADMIN, BUILDER, CLIENT, MANAGER).
     * @return true if the email exists for the given role, false otherwise.
     */
    public static boolean emailExists(String email, Role role) {
        // Dynamically build the SQL query based on role

        String sql = "SELECT 1 FROM " + role.getTableName() + " WHERE " + role.getEmailColumn() + " = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            // Execute the query and check if any record exists
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error checking email existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticates a user by verifying the given email and password against
     * the database. Returns the user's name if authentication succeeds.
     *
     * @param email The email address of the user attempting to log in.
     * @param password The plaintext password entered by the user.
     * @param role The user's role (used to determine table and columns).
     * @return The name of the user if authentication succeeds; null otherwise.
     */
    public static String authenticate(String email, String password, Role role) {

        // Dynamically build the SQL query based on role
        String sql = "SELECT " + role.getPasswordColumn() + ", " + role.getNameColumn() +
                " FROM " + role.getTableName() + " WHERE " + role.getEmailColumn() + " = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    // Verify provided password against the stored hashed password
                    String hashedPassword = rs.getString(role.getPasswordColumn());
                    String name = rs.getString(role.getNameColumn());

                    if (PasswordUtil.verifyPassword(password, hashedPassword)) {
                        return name;  // Authentication successful, return username
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error during authentication: " + e.getMessage());
        }

        return null;// Authentication failed
    }
}
