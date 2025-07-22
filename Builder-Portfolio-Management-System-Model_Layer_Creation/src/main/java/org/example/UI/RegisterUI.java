package org.example.UI;

import java.util.Scanner;

import org.example.Service.RegisterService;

import org.example.Constants.Role;


/**
 * UI for user registration options.
 * Allows users to register as different roles: Admin, Builder, Client, or Manager.
 */
public class RegisterUI {

    /**
     * Displays registration options to the user and routes to appropriate registration process.
     */
    public static void showRegisterOptions(Scanner scanner) {
        while (true) {
            // Display menu
            System.out.println("\n📝===== Registration =====");
            System.out.println("[1] Register as Admin");
            System.out.println("[2] Register as Builder");
            System.out.println("[3] Register as Client");
            System.out.println("[4] Register as Manager");
            System.out.println("[0] Back to Main Menu");
            System.out.print("👉 Enter your choice: ");

            String choice = scanner.nextLine().trim();
            Role role = null;

            // Map user choice to Role enum
            switch (choice) {
                case "1": role = Role.ADMIN; break;
                case "2": role = Role.BUILDER; break;
                case "3": role = Role.CLIENT; break;
                case "4": role = Role.MANAGER; break;
                case "0": return;
                default: System.out.println("❌ Invalid choice. Try again."); continue;
            }

            // Call service to handle role-specific registration
            RegisterService.registerUser(scanner, role);
        }
    }
}
