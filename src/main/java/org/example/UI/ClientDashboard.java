package org.example.UI;

import org.example.Service.ClientService;
import org.example.Service.ProjectService;

import java.util.Scanner;

public class ClientDashboard {

    public static void showDashboard(String clientEmail) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n🛠️===== Client Dashboard =====");
            System.out.println("[1] View Profile");
            System.out.println("[2] Edit Profile");
            System.out.println("[3] Change Password");
            System.out.println("[4] View My Projects");
            System.out.println("[0] Logout");
            System.out.print("👉 Enter your choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    ClientService.viewProfile(clientEmail);
                    break;
                case "2":
                    ClientService.editProfile(clientEmail);
                    break;
                case "3":
                    ClientService.changePassword(clientEmail);
                    break;
                case "4":
                    ProjectService.viewProjectsByClientEmail(clientEmail);  // NEW METHOD CALL
                    break;
                case "0":
                    System.out.println("👋 Logging out...");
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }
}
