package org.example.Service;

import org.example.Constants.Role;
import org.example.Repository.RegisterRepository;
import org.example.Util.DBConnection;
import org.example.Util.PasswordUtil;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.Connection;
import java.util.Scanner;

/**
 * Service class responsible for handling the registration logic for different user roles.
 */

public class RegisterService {

    // Logger for tracking runtime information and errors
    private static final Logger logger = Logger.getLogger(RegisterService.class.getName());

    /**
     * Main method for registering a user with the given role.
     * Handles input prompts, validation, and database insertion.
     */

    public static void registerUser(Scanner scanner, Role role) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Prompt user for basic details
            String name = promptNonEmpty(scanner, "Enter Name");
            String email = promptUniqueEmail(scanner, conn, role);
            String password = promptPassword(scanner);
            String contact = promptContact(scanner);

            // Handle address information and insert into DB
            String addressId = promptAndInsertAddress(scanner, conn);

            // Additional Fields
            String clientType = null;
            String pmStatus = null;
            String builderId = null;

            // Role-specific prompts
            switch (role) {
                case CLIENT:
                    clientType = promptNonEmpty(scanner, "Enter Client Type (individual/corporate)");
                    break;
                case MANAGER:
                    pmStatus = promptManagerStatus(scanner);
                    builderId = promptBuilderId(scanner, conn);
                    break;
                default:
                    break;
            }

            // Confirmation before committing data
            System.out.print("🟢 Submit registration? (Y/N): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("y")) {
                System.out.println("❌ Registration cancelled.");
                return;
            }

            // Insert user data into the appropriate table
            RegisterRepository.insertUser(conn, role, name, email, password, addressId, contact, clientType, pmStatus, builderId);

            // Commit transaction if everything succeeded
            conn.commit();
            System.out.println("✅ Registration successful! Welcome, " + name + " (" + role.name() + ")");

        } catch (Exception e) {
            logger.severe("⚠️ Error during registration: " + e.getMessage());
        }
    }

    /**
     * Prompts for email and ensures it's not already registered.
     */
    private static String promptUniqueEmail(Scanner scanner, Connection conn, Role role) {
        String email;
        while (true) {
            email = promptNonEmpty(scanner, "Enter Email");
            try {
                if (RegisterRepository.emailExists(conn, role, email)) {
                    System.out.println("❌ Email already exists. Try a different one.");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error checking email existence: " + e.getMessage());
                logger.log(Level.SEVERE, "Error checking email existence", e);

            }
        }
        return email;
    }


    /**
     * Prompts for a secure password and confirms it.
     */
    private static String promptPassword(Scanner scanner) {
        while (true) {
            String password = promptNonEmpty(scanner, "Enter password (min 10 chars, 1 uppercase, 1 lowercase, 1 digit, 1 special)");
            if (!PasswordUtil.isValidPassword(password)) {
                System.out.println("❌ Password doesn't meet the criteria.");
                continue;
            }

            String confirm = promptNonEmpty(scanner, "Re-enter password");
            if (!password.equals(confirm)) {
                System.out.println("❌ Passwords don't match.");
                continue;
            }

            return PasswordUtil.hashPassword(password);
        }
    }

    /**
     * Prompts for and validates a 10-digit contact number.
     */
    private static String promptContact(Scanner scanner) {
        while (true) {
            String contact = promptNonEmpty(scanner, "Enter Contact Number (10 digits)");
            if (contact.matches("\\d{10}")) return contact;
            System.out.println("❌ Invalid phone number. Enter exactly 10 digits.");
        }
    }

    /**
     * Prompts user for address fields and inserts into database.
     */
    private static String promptAndInsertAddress(Scanner scanner, Connection conn) {
        try {
            String line1 = promptNonEmpty(scanner, "Address Line 1");
            String city = promptNonEmpty(scanner, "City");
            String state = promptNonEmpty(scanner, "State");
            String zip = promptNonEmpty(scanner, "Zip Code");
            String country = promptNonEmpty(scanner, "Country");

            String addressId = RegisterRepository.insertAddress(conn, line1, city, state, zip, country);

            if (addressId == null)
                throw new Exception("Address insertion failed.");

            return addressId;

        } catch (Exception e) {
            System.out.println("❌ Failed to insert address: " + e.getMessage());
            logger.log(Level.SEVERE, "Error inserting address", e);
            return null;  // You might handle this case differently depending on your flow
        }
    }

    /**
     * Prompts for Manager status and validates input.
     */

    private static String promptManagerStatus(Scanner scanner) {
        while (true) {
            System.out.print("Enter Manager Status (Bench/Working): ");
            String status = scanner.nextLine().trim().toLowerCase();
            if (status.equals("bench") || status.equals("working")) {
                return status.toUpperCase();
            }
            System.out.println("❌ Invalid status. Enter 'Bench' or 'Working'.");
        }
    }

    /**
     * Prompts for a builder ID and verifies if it exists in the database.
     */
    private static String promptBuilderId(Scanner scanner, Connection conn) {
        while (true) {
            String builderId = promptNonEmpty(scanner, "Enter Associated Builder ID");
            try {
                if (RegisterRepository.isBuilderExists(conn, builderId)) {
                    return builderId;
                } else {
                    System.out.println("❌ Builder ID not found.");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error while checking builder ID: " + e.getMessage());
                logger.log(Level.SEVERE, "Error checking builder ID", e);
            }
        }
    }


    /**
     * Prompts for a non-empty input string with the given message.
     */

    private static String promptNonEmpty(Scanner scanner, String prompt) {
        String input;
        do {
            System.out.print(prompt + ": ");
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("❌ This field cannot be empty.");
            }
        } while (input.isEmpty());
        return input;
    }
}
