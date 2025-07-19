package org.example.Service;

import org.example.Repository.RegisterRepository;
import org.example.Util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.example.Util.DBConnection;

public class RegisterService {
    private static final Logger logger = Logger.getLogger(RegisterService.class.getName());

    public static void registerUser(Scanner scanner, String role) {
        try (Connection conn = DBConnection.getConnection()) {

            String name = promptNonEmpty(scanner, "Enter Name");

            String email;
            while (true) {
                email = promptNonEmpty(scanner, "Enter Email");
                if (RegisterRepository.emailExists(conn, role, email)) {
                    System.out.println("❌ Email already exists. Please use a different email.");
                } else break;
            }

            String password;
            while (true) {
                password = promptNonEmpty(scanner, "Enter password (min 10 chars, 1 uppercase, 1 lowercase, 1 digit, 1 special char)");
                if (!PasswordUtil.isValidPassword(password)) {
                    System.out.println("❌ Password does not meet the requirements. Try again.");
                    continue;
                }

                String confirmPassword = promptNonEmpty(scanner, "Re-enter password");
                if (!password.equals(confirmPassword)) {
                    System.out.println("❌ Passwords do not match. Try again.");
                } else break;
            }

            String hashedPassword = PasswordUtil.hashPassword(password);

            String contact;
            while (true) {
                contact = promptNonEmpty(scanner, "Enter Contact Number (10 digits)");
                if (!contact.matches("\\d{10}")) {
                    System.out.println("❌ Invalid phone number. Enter exactly 10 digits.");
                } else break;
            }

            // 📍 Address
            String addressLine1 = promptNonEmpty(scanner, "Enter Address Line 1");
            String city = promptNonEmpty(scanner, "Enter City");
            String state = promptNonEmpty(scanner, "Enter State");
            String zip = promptNonEmpty(scanner, "Enter Zip Code");
            String country = promptNonEmpty(scanner, "Enter Country");

            // 🔄 Insert Address
            String addressId = RegisterRepository.insertAddress(conn, addressLine1, city, state, zip, country);
            if (addressId == null) {
                System.out.println("❌ Address insertion failed.");
                return;
            }

            // 💼 Additional Fields
            String clientType = null;
            String pmStatus = null;
            String builderId = null;

            if (role.equals("client")) {
                clientType = promptNonEmpty(scanner, "Enter Client Type (individual/corporate name)");

            } else if (role.equals("manager")) {
                while (true) {
                    System.out.print("Enter Manager Status (Bench/Working): ");
                    pmStatus = scanner.nextLine().trim();
                    if (pmStatus.isEmpty()) {
                        System.out.println("❌ Manager Status cannot be empty.");
                        continue;
                    }

                    String statusLower = pmStatus.toLowerCase();
                    if (statusLower.equals("bench") || statusLower.equals("working")) {
                        pmStatus = statusLower.toUpperCase();
                        break;
                    } else {
                        System.out.println("❌ Invalid status. Please enter 'Bench' or 'Working' (case-insensitive).");
                    }
                }

                while (true) {
                    builderId = promptNonEmpty(scanner, "Enter Associated Builder ID");
                    if (RegisterRepository.isBuilderExists(conn, builderId)) break;
                    else System.out.println("❌ Builder ID does not exist. Please enter a valid Builder ID.");
                }
            }

            while (true) {
                System.out.print("🟢 Do you want to submit your registration? (Y/N): ");
                String confirmation = scanner.nextLine().trim().toLowerCase();
                if (confirmation.equals("y")) {
                    System.out.println("📤 Submitting your details...");
                    break;
                } else if (confirmation.equals("n")) {
                    System.out.println("❌ Registration cancelled.");
                    return;
                } else {
                    System.out.println("⚠️ Please enter 'Y' for yes or 'N' for no.");
                }
            }

            RegisterRepository.insertUser(conn, role, name, email, hashedPassword, addressId, contact, clientType, pmStatus, builderId);
            System.out.println("✅ Registration successful! " + name + " has been registered as a " + role.toUpperCase() + ".");



        } catch (Exception e) {
            System.out.println("⚠️ Error: " + e.getMessage());
            logger.log(Level.SEVERE, "An error occurred during registration", e);
        }
    }

    private static String promptNonEmpty(Scanner scanner, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt + ": ");
            input = scanner.nextLine().trim();
            if (input.isEmpty()) System.out.println("❌ This field cannot be empty.");
            else return input;
        }
    }
}
