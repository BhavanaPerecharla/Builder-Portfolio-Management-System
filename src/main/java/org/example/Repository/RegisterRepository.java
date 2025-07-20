package org.example.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterRepository {

    public static boolean emailExists(Connection conn, String role, String email) throws Exception {
        String query = "SELECT 1 FROM " + role + " WHERE " + role + "_email = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

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

    public static boolean isBuilderExists(Connection conn, String builderId) throws Exception {
        String sql = "SELECT 1 FROM builder WHERE builder_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, builderId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public static void insertUser(Connection conn, String role, String name, String email, String password, String addressId,
                                  String contact, String clientType, String pmStatus, String builderId) throws Exception {

        String sql = switch (role) {
            case "admin" -> "INSERT INTO admin (admin_name, admin_email, admin_password, address_id, admin_contact) VALUES (?, ?, ?, ?, ?)";
            case "builder" -> "INSERT INTO builder (builder_name, builder_email, builder_password, address_id, builder_contact) VALUES (?, ?, ?, ?, ?)";
            case "client" -> "INSERT INTO client (client_name, client_email, client_password, address_id, client_contact, client_type) VALUES (?, ?, ?, ?, ?, ?)";
            case "manager" -> "INSERT INTO manager (manager_name, manager_email, manager_password, address_id, manager_contact, pm_status, builder_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            default -> throw new IllegalArgumentException("Invalid role");
        };

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, addressId);
            ps.setString(5, contact);

            if (role.equals("client")) {
                ps.setString(6, clientType);
            } else if (role.equals("manager")) {
                ps.setString(6, pmStatus);
                ps.setString(7, builderId);
            }

            ps.executeUpdate();
        }
    }
}
