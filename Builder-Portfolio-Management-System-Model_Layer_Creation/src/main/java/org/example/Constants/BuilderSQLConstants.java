package org.example.Constants;


/**
 * Contains all SQL queries related to the 'builder' and 'address' tables.
 * This centralization improves maintainability and readability.
 */
public class BuilderSQLConstants {

    // === Builder Queries ===

    public static final String GET_BUILDER_ID_BY_EMAIL =
            "SELECT builder_id FROM builder WHERE builder_Email = ?";

    public static final String GET_BUILDER_BY_ID =
            "SELECT b.builder_id, b.builder_name, b.builder_email, b.builder_password, b.builder_contact, " +
                    "a.address_id, a.address_Line1, a.city, a.states, a.zip_Code, a.country " +
                    "FROM builder b JOIN address a ON b.address_id = a.address_id " +
                    "WHERE b.builder_id = ?";

    public static final String GET_BUILDER_BY_EMAIL =
            "SELECT * FROM builder WHERE builder_Email = ?";

    public static final String GET_ALL_BUILDERS =
            "SELECT b.builder_id, b.builder_name, b.builder_email, b.builder_password, b.builder_contact, " +
                    "a.address_id, a.address_Line1, a.city, a.states, a.zip_Code, a.country " +
                    "FROM builder b JOIN address a ON b.address_id = a.address_id";

    public static final String UPDATE_BUILDER =
            "UPDATE builder SET builder_name = ?, builder_email = ?, builder_password = ?, builder_contact = ? " +
                    "WHERE builder_id = ?";

    public static final String UPDATE_ADDRESS =
            "UPDATE address SET address_line1 = ?, city = ?, States = ?, zip_Code = ?, country = ? " +
                    "WHERE address_id = ?";

    public static final String UPDATE_PASSWORD =
            "UPDATE builder SET builder_password = ? WHERE builder_email = ?";
}

