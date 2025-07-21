package org.example.UI;

import java.util.Scanner;
import org.example.Service.BuilderService;

public class BuilderDashboard {

    public static void showDashboard(String email) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n🏗️===== Builder Dashboard =====");
            System.out.println("[1] View Profile");
            System.out.println("[2] Edit Profile");
            System.out.println("[3] Change Password");
            System.out.println("[4] Manage My Projects");   // Single menu for all project-related actions
            System.out.println("[0] Logout");
            System.out.print("👉 Enter your choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    BuilderService.viewProfile(email);
                    break;

                case "2":
                    BuilderService.editProfile(email);
                    break;

                case "3":
                    BuilderService.changePassword(email);
                    break;

                case "4":
                    ProjectUI.manageProjects(email);  
                    break;

                case "0":
                    System.out.println("👋 Logging out...");
                    return;

                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }
}
