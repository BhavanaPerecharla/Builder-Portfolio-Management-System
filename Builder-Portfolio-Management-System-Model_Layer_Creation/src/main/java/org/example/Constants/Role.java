package org.example.Constants;



/**
 * Enum to define valid user roles with associated table names and key columns.
 */
public enum Role {
    ADMIN("admin"),
    BUILDER("builder"),
    CLIENT("client"),
    MANAGER("manager");

    private final String tableName;

    Role(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getEmailColumn() {
        return tableName + "_email";
    }

    public String getPasswordColumn() {
        return tableName + "_password";
    }

    public String getNameColumn() {
        return tableName + "_name";
    }

    /**
     * Parses user input into a Role enum, case-insensitive.
     * Returns null if invalid.
     */
    public static Role fromString(String input) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(input)) {
                return role;
            }
        }
        return null;
    }
}
