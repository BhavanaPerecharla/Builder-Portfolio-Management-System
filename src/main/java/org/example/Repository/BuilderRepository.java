package org.example.Repository;

import org.example.Model.Address;
import org.example.Model.Builder;
import org.example.Util.DBConnection;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuilderRepository {
    private static final Logger logger = Logger.getLogger(BuilderRepository.class.getName());

    private final AddressRepository addressRepository = new AddressRepository();

    // Insert builder without setting builder_id manually
    public void insertBuilder(Builder builder) {
        String sql = "INSERT INTO builder (builder_Name, builder_Email, builder_Password, builder_Contact, builder_AddressId) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Insert address and retrieve generated address_id
            String addressId = addressRepository.insertAddress(builder.getAddress());

            // Insert builder using addressId
            statement.setString(1, builder.getBuilderName());
            statement.setString(2, builder.getBuilderEmail());
            statement.setString(3, builder.getBuilderPassword());
            statement.setString(4, builder.getBuilderContact());
            statement.setString(5, addressId);

            statement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting builder into database", e);
        }
    }

    // Get builder by ID (including its address)
    public Builder getBuilderById(String builderId) {
        String sql = "SELECT * FROM builder WHERE builder_Id = ?";
        Builder builder = null;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, builderId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                builder = new Builder();
                builder.setBuilderId(resultSet.getString("builder_Id"));
                builder.setBuilderName(resultSet.getString("builder_Name"));
                builder.setBuilderEmail(resultSet.getString("builder_Email"));
                builder.setBuilderPassword(resultSet.getString("builder_Password"));
                builder.setBuilderContact(resultSet.getString("builder_Contact"));

                String addressId = resultSet.getString("builder_AddressId");
                Address address = addressRepository.getAddressById(addressId);
                builder.setAddress(address);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving builder from database", e);
        }

        return builder;
    }
}
