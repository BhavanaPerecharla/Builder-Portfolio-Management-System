package org.example.Service;

import org.example.Model.*;
import org.example.Repository.*;
import org.example.Util.AddressEditor;
import org.example.Util.PasswordManager;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.Util.InputValidator.promptNonEmpty;
import static org.example.Util.InputValidator.promptValidContact;


/**
 * Service class to manage all admin functionalities such as viewing/editing profile,
 * managing users (builders, managers, clients), and password operations.
 */

public class AdminService {
    private static final Logger logger = Logger.getLogger(AdminService.class.getName());
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Displays admin profile details based on email.
     */
    public static void viewProfile(String email) {
        try {
            Admin admin = AdminRepository.getAdminByEmail(email);
            if (admin == null) {
                System.out.println("❌ Admin not found.");
                return;
            }

            Address address = AddressRepository.getAddressById(admin.getAddressId());

            System.out.println("\n👤===== Admin Profile =====");
            System.out.printf("🔹 Name   : %s%n", admin.getAdminName());
            System.out.printf("🔹 Email  : %s%n", admin.getAdminEmail());
            System.out.printf("🔹 Contact: %s%n", admin.getAdminContact());

            if (address != null) {
                System.out.println("🏠 Address:");
                printAddress(address);
            } else {
                System.out.println("⚠️ Address not found.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error viewing admin profile", e);
        }
    }


    /**
     * Displays all managers in the system.
     */
    public static void viewAllManagers() {
        List<Manager> managers = ManagerRepository.getInstance().getAllManagers();
        if (managers.isEmpty()) {
            System.out.println("⚠️ No managers found.");
            return;
        }

        System.out.println("\n👷 All Managers Details:");
        for (Manager m : managers) {
            System.out.println("-------------------------------------");
            System.out.printf("🆔 ID         : %s%n", m.getManagerId());
            System.out.printf("👤 Name       : %s%n", m.getManagerName());
            System.out.printf("📧 Email      : %s%n", m.getManagerEmail());
            System.out.printf("📞 Contact    : %s%n", m.getManagerContact());
            System.out.printf("📋 PM Status  : %s%n", m.getPmStatus());
            System.out.printf("🏗️ Builder ID : %s%n", m.getBuilderId());

            Address address = AddressRepository.getAddressById(m.getAddressId());
            if (address != null) {
                System.out.println("🏠 Address:");
                printAddress(address);
            } else {
                System.out.println("🏠 Address    : Not Available");
            }
        }
    }

    /**
     * Displays all builders in the system.
     */
    public static void viewAllBuilders() {
        List<Builder> builders = BuilderRepository.getInstance().getAllBuilders();
        if (builders.isEmpty()) {
            System.out.println("⚠️ No builders found.");
            return;
        }

        System.out.println("\n🏗️===== List of Builders =====");
        for (Builder b : builders) {
            System.out.println("------------------------------------------");
            System.out.printf("🆔 Builder ID : %s%n", b.getBuilderId());
            System.out.printf("👷 Name       : %s%n", b.getBuilderName());
            System.out.printf("📧 Email      : %s%n", b.getBuilderEmail());
            System.out.printf("📞 Contact    : %s%n", b.getBuilderContact());

            Address address = b.getAddress();
            if (address != null) {
                System.out.println("🏠 Address:");
                printAddress(address);
            } else {
                System.out.println("🏠 Address    : Not Available");
            }
        }
    }

    // Lists all client records with associated details
    public static void viewAllClients() {
        List<Client> clients = ClientRepository.getInstance().getAllClients();
        if (clients.isEmpty()) {
            System.out.println("⚠️ No clients found.");
            return;
        }

        System.out.println("\n📋===== All Clients =====");
        for (Client c : clients) {
            System.out.println("--------------------------------------------");
            System.out.printf("🆔 Client ID : %s%n", c.getClientId());
            System.out.printf("👤 Name      : %s%n", c.getClientName());
            System.out.printf("📧 Email     : %s%n", c.getClientEmail());
            System.out.printf("📞 Contact   : %s%n", c.getClientContact());
            System.out.printf("🏷️ Type      : %s%n", c.getClientType());

            Address address = c.getAddress();
            if (address != null) {
                System.out.println("🏠 Address:");
                printAddress(address);
            } else {
                System.out.println("🏠 Address    : Not Available");
            }
        }
    }

    // Allows the admin to edit their profile details-including name, contact, email, and address
    public static String editProfileAndReturnUpdatedEmail(String email) {
    try {
        Admin admin = AdminRepository.getAdminByEmail(email);
        if (admin == null) {
            System.out.println("❌ Admin not found.");
            return null;
        }

        Address address = AddressRepository.getAddressById(admin.getAddressId());
        if (address == null) {
            address = new Address();
            address.setAddressId(admin.getAddressId());
        }

        while (true) {
            System.out.println("\n🛠️===== Edit Profile =====");
            System.out.println("[1] Edit Name");
            System.out.println("[2] Edit Contact");
            System.out.println("[3] Edit Email");
            System.out.println("[4] Edit Address");
            System.out.println("[0] Back to Dashboard");
            System.out.print("👉 Enter your choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    admin.setAdminName(promptNonEmpty(sc, "Enter new name"));
                    System.out.println("✅ Name updated successfully.");
                    break;
                case "2":
                    admin.setAdminContact(promptValidContact(sc, "Enter new contact number"));
                    System.out.println("✅ Contact updated successfully.");
                    break;
                case "3":
                    admin.setAdminEmail(promptNonEmpty(sc, "Enter new email"));
                    System.out.println("✅ Email updated successfully.");
                    break;
                case "4":
                    AddressEditor.editAddress(sc, address);
                    break;
                case "0":
                    if (AdminRepository.updateAdmin(admin, address)) {
                        System.out.println("✅ Profile updated successfully!");
                    } else {
                        System.out.println("❌ Failed to update profile.");
                    }
                    return admin.getAdminEmail(); // return updated email
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    } catch (Exception e) {
        logger.log(Level.SEVERE, "Error editing admin profile", e);
        return null;
    }
}

                     

    // Allows the admin to change their password securely
    public static void changePassword(String email) {
        try {

            Admin admin = AdminRepository.getAdminByEmail(email);
            if (admin == null) {
                System.out.println("❌ Admin not found.");
                return;
            }
            // PasswordManager handles confirmation, validation, and hashing
            String hashedPassword = PasswordManager.handlePasswordChange(sc, admin.getAdminPassword());
            if (hashedPassword != null) {
                admin.setAdminPassword(hashedPassword);
                boolean success = AdminRepository.updatePassword(email, hashedPassword);
                System.out.println(success ? "✅ Password changed successfully." : "❌ Failed to change password.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error changing admin password", e);
        }
    }

    // Helper method to display address details in a formatted way
    private static void printAddress(Address address) {
        System.out.printf("   - Line1   : %s%n", address.getAddressLine1());
        System.out.printf("   - City    : %s%n", address.getCity());
        System.out.printf("   - State   : %s%n", address.getStates());
        System.out.printf("   - Zip     : %s%n", address.getZipCode());
        System.out.printf("   - Country : %s%n", address.getCountry());
    }
}
