package org.example.UI;

import java.util.Scanner;
import org.example.Service.ManagerService;
import org.example.Service.ProjectService;

public class ManagerDashboard {

    public static void showDashboard(String email) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n👔===== Manager Dashboard =====");
            System.out.println("[1] View Profile");
            System.out.println("[2] Edit Profile");
            System.out.println("[3] Change Password");
            System.out.println("[4] View My Projects");
            System.out.println("[0] Logout");
            System.out.print("👉 Enter your choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    ManagerService.viewProfile(email);
                    break;

                case "2":
                    ManagerService.editProfile(email);
                    break;

                case "3":
                    ManagerService.changePassword(email);
                    break;

                case "4":
                    ProjectService.viewProjectsByManagerEmail(email);
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
