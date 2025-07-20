package org.example.UI;

import java.util.Scanner;

import org.example.Service.LoginService;
import org.example.UI.RegisterUI;
import org.example.Util.DBConnection;
import org.example.UI.LoginUI;

public class LoginUI {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n🌟===== Welcome to Builder Portfolio Management System =====🌟");
            System.out.println("[1] Login into your account");
            System.out.println("[2] Register as new User");
            System.out.println("[0] Exit ");
            System.out.print("👉 Enter your choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    performLogin(sc);
                    break;
                case "2":
                    RegisterUI.showRegisterOptions(sc); // Assuming your existing registration UI
                    break;
                case "0":
                    System.out.println("👋 Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }

    public static void performLogin(Scanner sc) {
        System.out.println("\n🔐===== Login =====");

        String role;
        while (true) {
            System.out.print("Enter Role (Admin/Builder/Client/Manager): ");
            role = sc.nextLine().trim().toLowerCase();

            if (LoginService.isValidRole(role)) {
                break;
            } else {
                System.out.println("❌ Invalid role. Please enter one of: Admin, Builder, Client, Manager.");
            }
        }

        String email = "";
        int emailAttempts = 3;
        while (emailAttempts-- > 0) {
            System.out.print("Enter Email: ");
            email = sc.nextLine().trim();

            if (LoginService.emailExists(email, role)) {
                break;
            } else {
                System.out.println("❌ Email not found for role: " + role.toUpperCase());
                if (emailAttempts > 0) {
                    System.out.println("🔁 Please try again. Attempts left: " + emailAttempts);
                } else {
                    System.out.println("❌ Too many failed attempts. Returning to main menu.");
                    return;
                }
            }
        }

        int passwordAttempts = 3;
        while (passwordAttempts-- > 0) {
            System.out.print("Enter Password: ");
            String password = sc.nextLine().trim();

            String name = LoginService.authenticate(email, password, role);
            if (name != null) {
                System.out.println("✅ Login Successful! Welcome, " + name + " (" + role.toUpperCase() + ")");

                switch (role.toLowerCase()) {
                    case "admin":
                        AdminDashboard.showDashboard(email);
                        break;
                    case "builder":
                        BuilderDashboard.showDashboard(email);
                        break;
                    case "client":
                        ClientDashboard.showDashboard(email);
                        break;
                    case "manager":
                        ManagerDashboard.showDashboard(email);
                        break;
                    default:
                        System.out.println("⚠️ Unknown role: " + role);
                }
                return;
            } else {
                System.out.println("❌ Incorrect password.");
                if (passwordAttempts > 0) {
                    System.out.println("🔁 Try again. Attempts left: " + passwordAttempts);
                } else {
                    System.out.println("🚫 Too many failed attempts. Returning to main menu.");
                }
            }
        }

    }
}

