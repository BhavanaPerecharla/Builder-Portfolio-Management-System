package org.example.Repository;
import org.example.Util.DBConnection;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.example.Model.Address;
import java.sql.*;



public class AddressRepository {
    private static final Logger logger = Logger.getLogger(AddressRepository.class.getName());

    // Insert query with RETURNING to get generated address_id (VARCHAR)
    private static final String INSERT_QUERY = "INSERT INTO address (address_Line1, city, state, zip_Code, country) " +
            "VALUES (?, ?, ?, ?, ?) RETURNING address_id";

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM address WHERE address_Id = ?";

    // Inserts a new address and returns the generated address_id (VARCHAR)
    public String insertAddress(Address address) {
        String generatedId = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_QUERY)) {

            ps.setString(1, address.getAddressLine1());
            ps.setString(2, address.getCity());
            ps.setString(3, address.getState());
            ps.setString(4, address.getZipCode());
            ps.setString(5, address.getCountry());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    generatedId = rs.getString("address_id");
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting address into database", e);
        }

        return generatedId;
    }

    // Fetch address by ID (VARCHAR)
    public Address getAddressById(String addressId) {
        Address address = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_QUERY)) {

            ps.setString(1, addressId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    address = new Address(
                            rs.getString("address_Id"),
                            rs.getString("address_Line1"),
                            rs.getString("city"),
                            rs.getString("state"),
                            rs.getString("zip_Code"),
                            rs.getString("country")
                    );
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving address from database", e);
        }

        return address;
    }
}



