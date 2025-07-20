package org.example.Repository;

import org.example.Model.Address;
import org.example.Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class AddressRepository {

    private static final Logger logger = Logger.getLogger(AddressRepository.class.getName());

    public static String insertAddress(Address address) throws SQLException {
        String sql = "INSERT INTO address (addressLine, city, states, zipCode, country) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, address.getAddressLine1());
            stmt.setString(2, address.getCity());
            stmt.setString(3, address.getStates());
            stmt.setString(4, address.getZipCode());
            stmt.setString(5, address.getCountry());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getString(1);  // return generated address_id
            } else {
                throw new SQLException("Address insertion failed.");
            }
        }
    }
    // READ - By ID
    public static Address getAddressById(String addressId) {
        String sql = "SELECT * FROM address WHERE address_Id = ?";
        Address address = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, addressId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                address = new Address();
                address.setAddressId(rs.getString("address_Id"));
                address.setAddressLine1(rs.getString("address_Line1"));
                address.setCity(rs.getString("city"));
                address.setStates(rs.getString("States"));
                address.setZipCode(rs.getString("zip_Code"));
                address.setCountry(rs.getString("country"));
            }

            DBConnection.closeResultSet(rs);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching address by ID", e);
        }

        return address;
    }

    // READ - All
    public List<Address> getAllAddresses() {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM address";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Address address = new Address();
                address.setAddressId(rs.getString("address_Id"));
                address.setAddressLine1(rs.getString("address_Line1"));
                address.setCity(rs.getString("city"));
                address.setStates(rs.getString("States"));
                address.setZipCode(rs.getString("zip_Code"));
                address.setCountry(rs.getString("country"));
                addresses.add(address);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching all addresses", e);
        }

        return addresses;
    }

    public static boolean updateAddress(Address address) {
        if (address == null) {
            return false;  // or throw IllegalArgumentException
        }

        String sql = "UPDATE address SET address_Line1 = ?, city = ?, states = ?, zip_Code = ?, country = ? WHERE address_Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, address.getAddressLine1());
            stmt.setString(2, address.getCity());
            stmt.setString(3, address.getStates());
            stmt.setString(4, address.getZipCode());
            stmt.setString(5, address.getCountry());
            stmt.setString(6, address.getAddressId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating address (ID: " + address.getAddressId() + ")", e);
            return false;
        }
    }

    // DELETE
    public static void deleteAddress(String addressId) {
        String sql = "DELETE FROM address WHERE address_Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, addressId);
            stmt.executeUpdate();
            logger.info("Address deleted successfully.");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting address", e);
        }
    }


}
