package org.example.Service;


import org.example.Util.DBConnection;
import org.example.Util.PasswordUtil;

import java.sql.*;

public class LoginService {

    public static boolean isValidRole(String role) {
        return role.matches("admin|builder|client|manager");
    }

    public static boolean emailExists(String email, String role) {
        String tableName = role;
        String emailCol = role + "_email";
        String sql = "SELECT 1 FROM " + tableName + " WHERE " + emailCol + " = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if email exists

        } catch (SQLException e) {
            System.out.println("⚠️ Error checking email: " + e.getMessage());
            return false;
        }
    }

    public static String authenticate(String email, String password, String role) {
        String table = role;
        String emailCol = role + "_email";
        String passCol = role + "_password";
        String nameCol = role + "_name"; // Assuming each table has a column like admin_name, builder_name, etc.

        String sql = "SELECT " + passCol + ", " + nameCol + " FROM " + table + " WHERE " + emailCol + " = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString(passCol);
                String name = rs.getString(nameCol);

                if (PasswordUtil.verifyPassword(password, hashedPassword)) {
                    return name;
                }
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error authenticating user: " + e.getMessage());
        }

        return null; // login failed
    }

}
