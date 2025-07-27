package org.example.UI;
import org.example.Service.AdminService;
import org.example.Util.InputUtil;


/**
 * UI class responsible for displaying the Admin Dashboard and handling admin interactions.
 */

public class AdminDashboard {


    /**
     * Displays the admin dashboard and handles user input for various admin actions.
     *
     * @param email The email of the currently logged-in admin.
     */

    public static void showDashboard(String email) {
        while (true) {
            showMenu(); // Display the available options to the admin
            String choice = InputUtil.prompt("👉 Enter your choice: ");

            // Handle the admin's menu choice
            switch (choice) {
                case "1":
                    AdminService.viewProfile(email);   // View admin profile
                    break;
                case "2":
                     String updatedEmail = AdminService.editProfileAndReturnUpdatedEmail(email);
                     if (updatedEmail != null) {
                         email = updatedEmail; // update the email used in session
                      }
                      break;
                case "3":
                    AdminService.changePassword(email);  // Change admin password
                    break;
                case "4":
                    AdminService.viewAllBuilders();   // Display all builders
                    break;
                case "5":
                    AdminService.viewAllClients();    // Display all clients
                    break;
                case "6":
                    AdminService.viewAllManagers();   // Display all managers
                    break;
                case "0":
                    System.out.println("👋 Logging out..."); // Exit the dashboard
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please try again."); // Handle invalid input
            }
        }
    }

    /**
     * Displays the admin dashboard menu options.
     */

    private static void showMenu() {
        System.out.println("\n🛡️===== Admin Dashboard =====");
        System.out.println("[1] View Profile");
        System.out.println("[2] Edit Profile");
        System.out.println("[3] Change Password");
        System.out.println("[4] View All Builders");
        System.out.println("[5] View All Clients");
        System.out.println("[6] View All Managers");
        System.out.println("[0] Logout");
    }
}
