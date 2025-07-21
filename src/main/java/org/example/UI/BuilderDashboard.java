package org.example.UI;


import org.example.Service.BuilderService;
import org.example.Util.InputUtil;
/**
 * Handles the dashboard interface for the Builder role.
 */
public class BuilderDashboard {
    /**
     * Displays the dashboard menu and handles user choices for the builder.
     *
     * @param email The email of the logged-in builder (used to retrieve/update their profile).
     */
    public static void showDashboard(String email) {
        while (true) {
            showMenu();
            String choice = InputUtil.prompt("👉 Enter your choice: ");

            switch (choice) {
                case "1" -> BuilderService.viewProfile(email);
                case "2" -> BuilderService.editProfile(email);
                case "3" -> BuilderService.changePassword(email);
                case "4" -> ProjectUI.manageProjects(email);
                case "0" -> {
                    System.out.println("👋 Logging out...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the builder dashboard menu options.
     */
    private static void showMenu() {
        System.out.println("\n🏗️===== Builder Dashboard =====");
        System.out.println("[1] View Profile");
        System.out.println("[2] Edit Profile");
        System.out.println("[3] Change Password");
        System.out.println("[4] Manage My Projects");
        System.out.println("[0] Logout");
    }
}
