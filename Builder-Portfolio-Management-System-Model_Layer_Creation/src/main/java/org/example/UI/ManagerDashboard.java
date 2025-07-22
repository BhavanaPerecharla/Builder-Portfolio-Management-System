package org.example.UI;


import java.util.Scanner;
import org.example.Service.ManagerService;
import org.example.Service.ProjectService;

/**
 * UI Layer: ManagerDashboard
 * Provides a menu-driven interface for managers.
 */
public class ManagerDashboard {

    /**
     * Displays the dashboard menu and handles user input.
     *
     * @param email The email of the logged-in manager.
     */
    public static void showDashboard(String email) {
        Scanner sc = new Scanner(System.in);
        String choice;

        do {
            displayMenu(); // Modular method to print the menu
            System.out.print("👉 Enter your choice: ");
            choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    // View manager profile details
                    ManagerService.viewProfile(email);
                    break;
                case "2":
                    // Edit manager profile
                    ManagerService.editProfile(email);
                    break;
                case "3":
                    // Change account password
                    ManagerService.changePassword(email);
                    break;
                case "4":
                    // View projects assigned to this manager
                    ProjectService.viewProjectsByManagerEmail(email);
                    break;
                case "0":
                    // Logout option
                    System.out.println("👋 Logging out...");
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        } while (!choice.equals("0"));
    }

    /**
     * Displays the dashboard menu options to the manager.
     * Keeping this method separate promotes modularity and makes code cleaner.
     */
    private static void displayMenu() {
        System.out.println("\n👔===== Manager Dashboard =====");
        System.out.println("[1] View Profile");
        System.out.println("[2] Edit Profile");
        System.out.println("[3] Change Password");
        System.out.println("[4] View My Projects");
        System.out.println("[0] Logout");
    }
}
