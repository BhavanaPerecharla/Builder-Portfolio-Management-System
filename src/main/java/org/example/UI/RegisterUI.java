package org.example.UI;

import java.util.Scanner;
import java.util.logging.Logger;
import org.example.Service.RegisterService;

public class RegisterUI {
    private static final Logger logger = Logger.getLogger(RegisterUI.class.getName());

    public static void showRegisterOptions(Scanner scanner) {
        while (true) {
            System.out.println("\n📝===== Registration =====");
            System.out.println("[1] Register as Admin");
            System.out.println("[2] Register as Builder");
            System.out.println("[3] Register as Client");
            System.out.println("[4] Register as Manager");
            System.out.println("[0] Back to Main Menu");
            System.out.print("👉 Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1": RegisterService.registerUser(scanner, "admin"); break;
                case "2": RegisterService.registerUser(scanner, "builder"); break;
                case "3": RegisterService.registerUser(scanner, "client"); break;
                case "4": RegisterService.registerUser(scanner, "manager"); break;
                case "0": return;
                default: System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }
}
