package org.example.UI;


import org.example.Service.ClientService;
import org.example.Service.ProjectService;

import java.util.Scanner;
/**
 * ClientDashboard provides an interactive console interface for a client user.
 * It allows the client to view/edit their profile, view projects, payments, and documents.
 */

public class ClientDashboard {

    /**
     * Displays the client dashboard menu and handles user choices.
     *
     * @param clientEmail the email address of the logged-in client
     */

    public static void showDashboard(String clientEmail) {
        Scanner sc = new Scanner(System.in);

        // Infinite loop to keep showing the menu until the client chooses to logout
        while (true) {
            displayMenu();
            System.out.print("👉 Enter your choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> ClientService.viewProfile(clientEmail);  // View profile details
                case "2" -> ClientService.editProfile(clientEmail);  // Edit profile
                case "3" -> ClientService.changePassword(clientEmail);  // Change password
                case "4" -> ProjectService.viewProjectsByClientEmail(clientEmail);// List client's projects
                case "5" -> ProjectService.viewPaymentsForClientProjects(clientEmail);// Show payment details
                case "6" -> ProjectService.viewDocumentsForClientProjects(clientEmail);  // Delegated to Service layer
                case "0" -> {
                    System.out.println("👋 Logging out...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }

    /**
     * Displays the main client dashboard menu options to the console.
     */
    private static void displayMenu() {
        System.out.println("\n🛠️===== Client Dashboard =====");
        System.out.println("[1] View Profile");
        System.out.println("[2] Edit Profile");
        System.out.println("[3] Change Password");
        System.out.println("[4] View My Projects");
        System.out.println("[5] View Payments for My Projects");
        System.out.println("[6] View Project Documents");
        System.out.println("[0] Logout");
    }
}

