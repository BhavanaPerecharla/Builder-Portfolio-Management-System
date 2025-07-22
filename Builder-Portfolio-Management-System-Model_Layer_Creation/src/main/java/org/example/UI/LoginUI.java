package org.example.UI;

import java.util.Scanner;

import org.example.Service.LoginService;


import org.example.Constants.Role;

/**
 * LoginUI - Command Line Interface for login and user registration routing.
 * Acts as the entry point for user authentication.
 */
public class LoginUI {

    /**
     * Main method to launch the application.
     * Displays the main menu for login, registration, or exit.
     */

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n🌟===== Welcome to Builder Portfolio Management System =====🌟");
            System.out.println("[1] Login to your account");  // Handle user login
            System.out.println("[2] Register as New User");  // Redirect to registration UI
            System.out.println("[0] Exit");
            System.out.print("👉 Enter your choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    performLogin(sc);
                    break;
                case "2":
                    RegisterUI.showRegisterOptions(sc);
                    break;
                case "0":
                    System.out.println("👋 Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }

    /**
     * Handles the login process:
     * 1. Role selection and validation.
     * 2. Email verification (with retry limit).
     * 3. Password authentication (with retry limit).
     * 4. Redirects to appropriate dashboard after successful login.
     *
     * @param sc Scanner object for user input
     */

    public static void performLogin(Scanner sc) {
        System.out.println("\n🔐===== Login =====");


        // Step 1: Role selection and validation
        Role role = null;
        while (role == null) {
            System.out.print("Enter Role (Admin/Builder/Client/Manager): ");
            String roleInput = sc.nextLine().trim();
            role = Role.fromString(roleInput);

            if (role == null) {
                System.out.println("❌ Invalid role. Please enter Admin, Builder, Client, or Manager.");
            }
        }

        // Step 2: Email verification
        String email = "";
        int emailAttempts = 3;

        while (emailAttempts > 0) {
            System.out.print("Enter Email: ");
            email = sc.nextLine().trim();

            if (LoginService.emailExists(email, role)) {
                break;
            } else {
                emailAttempts--;
                System.out.println("❌ Email not found for role: " + role.name());
                if (emailAttempts > 0) {
                    System.out.println("🔁 Attempts remaining: " + emailAttempts);
                } else {
                    System.out.println("🚫 Too many failed attempts. Returning to main menu.");
                    return;
                }
            }
        }


        // Step 3: Password authentication
        int passwordAttempts = 3;

        while (passwordAttempts > 0) {
            System.out.print("Enter Password: ");
            String password = sc.nextLine().trim();

            String name = LoginService.authenticate(email, password, role);

            if (name != null) {
                System.out.println("✅ Login Successful! Welcome, " + name + " (" + role.name() + ")");
                launchDashboard(role, email);
                return;
            } else {
                passwordAttempts--;
                System.out.println("❌ Incorrect password.");
                if (passwordAttempts > 0) {
                    System.out.println("🔁 Attempts remaining: " + passwordAttempts);
                } else {
                    System.out.println("🚫 Too many failed attempts. Returning to main menu.");
                }
            }
        }
    }


    /**
     * Redirects the authenticated user to their respective dashboard.
     *
     * @param role  User's role (ADMIN, BUILDER, CLIENT, MANAGER)
     * @param email User's email
     */
    private static void launchDashboard(Role role, String email) {
        switch (role) {
            case ADMIN:
                AdminDashboard.showDashboard(email);
                break;
            case BUILDER:
                BuilderDashboard.showDashboard(email);
                break;
            case CLIENT:
                ClientDashboard.showDashboard(email);
                break;
            case MANAGER:
                ManagerDashboard.showDashboard(email);
                break;
            default:
                System.out.println("⚠️ Unknown role: " + role.name());
        }
    }
}
