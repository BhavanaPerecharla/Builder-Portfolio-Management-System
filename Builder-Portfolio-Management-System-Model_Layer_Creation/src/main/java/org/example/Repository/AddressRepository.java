package org.example.Repository;

import org.example.Model.Address;
import org.example.Util.DBConnection;

import java.sql.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository class to handle database operations related to Address.
 * It provides methods to fetch and update address details.
 */
public class AddressRepository {

    private static final Logger logger = Logger.getLogger(AddressRepository.class.getName());

    /**
     * Retrieves an Address object from the database by its ID.
     *
     * @param addressId the unique ID of the address to retrieve
     * @return an Address object if found, otherwise null
     */

    public static Address getAddressById(String addressId) {
        String sql = "SELECT * FROM address WHERE address_Id = ?";
        Address address = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, addressId);
            ResultSet rs = stmt.executeQuery();

            // If a result is found, create and populate the Address object
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

        return address;  // Return the retrieved Address object or null
    }


    /**
     * Updates an existing address in the database.
     *
     * @param address the Address object containing updated values
     * @return true if the update was successful, false otherwise
     */

    public static boolean updateAddress(Address address) {
        String sql = "UPDATE address SET address_Line1 = ?, city = ?, states = ?, zipCode = ?, country = ? WHERE address_Id = ?";

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
            logger.log(Level.SEVERE, "Error updating address", e);
        }
        return false;
    }

    /**
     * Utility method to map a ResultSet row to an Address object.
     *
     * @param rs the ResultSet from a query
     * @return an Address object containing the mapped data
     * @throws SQLException if an error occurs during data extraction
     */
    private static Address mapResultSetToAddress(ResultSet rs) throws SQLException {
        Address address = new Address();
        address.setAddressId(rs.getString("address_Id"));
        address.setAddressLine1(rs.getString("address_Line1"));
        address.setCity(rs.getString("city"));
        address.setStates(rs.getString("states"));
        address.setZipCode(rs.getString("zipCode"));
        address.setCountry(rs.getString("country"));
        return address;
    }
}
