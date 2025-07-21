package org.example.UI;


import java.util.Scanner;
import org.example.Service.AdminService;

public class AdminDashboard {

    public static void showDashboard(String email) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n🛡️===== Admin Dashboard =====");
            System.out.println("[1] View Profile");
            System.out.println("[2] Edit Profile");
            System.out.println("[3] Change Password");
            System.out.println("[4] View All Builders");
            System.out.println("[5] View All Clients");
            System.out.println("[6] View All Managers");
            System.out.println("[0] Logout");
            System.out.print("👉 Enter your choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    AdminService.viewProfile(email);
                    break;

                case "2":
                    AdminService.editProfile(email);
                    break;

                case "3":
                    AdminService.changePassword(email);
                    break;

                case "4":
                    AdminService.viewAllBuilders();
                    break;

                case "5":
                    AdminService.viewAllClients();
                    break;

                case "6":
                    AdminService.viewAllManagers();
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
