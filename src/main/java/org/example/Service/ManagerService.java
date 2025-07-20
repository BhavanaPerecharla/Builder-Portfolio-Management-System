package org.example.Service;


import static org.example.Util.InputValidator.promptNonEmpty;
import static org.example.Util.InputValidator.promptValidContact;

import org.example.Model.Address;
import org.example.Model.Manager;
import org.example.Repository.BuilderRepository;
import org.example.Repository.ManagerRepository;
import org.example.Repository.AddressRepository;
import org.example.Util.AddressEditor;
import org.example.Util.PasswordManager;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManagerService {
    private static final Logger logger = Logger.getLogger(ManagerService.class.getName());

    public static void viewProfile(String email) {
        try {
            Manager manager = ManagerRepository.getManagerByEmail(email);
            if (manager == null) {
                System.out.println("❌ Manager not found.");
                return;
            }

            Address address = AddressRepository.getAddressById(manager.getAddressId());

            System.out.println("\n👤===== Manager Profile =====");
            System.out.println("🔹 Name: " + manager.getManagerName());
            System.out.println("🔹 Email: " + manager.getManagerEmail());
            System.out.println("🔹 Contact: " + manager.getManagerContact());
            System.out.println("🔹 PM Status: " + manager.getPmStatus());
            System.out.println("🔹 Assigned Builder ID: " + manager.getBuilderId());

            if (address != null) {
                System.out.println("🏠 Address:");
                System.out.println("   - Line1: " + address.getAddressLine1());
                System.out.println("   - City: " + address.getCity());
                System.out.println("   - State: " + address.getStates());
                System.out.println("   - Zip: " + address.getZipCode());
                System.out.println("   - Country: " + address.getCountry());
            } else {
                System.out.println("⚠️ Address not found.");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error viewing manager profile", e);
        }
    }

    public static void editProfile(String email) {
        Scanner sc = new Scanner(System.in);

        try {
            Manager manager = ManagerRepository.getManagerByEmail(email);
            if (manager == null) {
                System.out.println("❌ Manager not found.");
                return;
            }

            Address address = AddressRepository.getAddressById(manager.getAddressId());
            if (address == null) {
                address = new Address();
                address.setAddressId(manager.getAddressId());
            }

            while (true) {
                System.out.println("\n🛠️===== Edit Profile =====");
                System.out.println("[1] Edit Name");
                System.out.println("[2] Edit Contact");
                System.out.println("[3] Edit PM Status");
                System.out.println("[4] Edit Assigned Builder ID");
                System.out.println("[5] Edit Address");
                System.out.println("[0] Back to Dashboard");
                System.out.print("👉 Enter your choice: ");
                String choice = sc.nextLine().trim();

                switch (choice) {
                    case "1":
                        manager.setManagerName(promptNonEmpty(sc, "Enter new name"));
                        if (ManagerRepository.updateManager(manager, address)) {
                            System.out.println("✅ Profile updated successfully!");
                        } else {
                            System.out.println("❌ Failed to update profile.");
                        }
                        break;
                    case "2":
                        manager.setManagerContact(promptValidContact(sc, "Enter new contact number"));
                        if (ManagerRepository.updateManager(manager, address)) {
                            System.out.println("✅ Profile updated successfully!");
                        } else {
                            System.out.println("❌ Failed to update profile.");
                        }
                        break;
                    case "3":
                        manager.setPmStatus(promptNonEmpty(sc, "Enter PM status (bench / working)"));
                        if (ManagerRepository.updateManager(manager, address)) {
                            System.out.println("✅ Profile updated successfully!");
                        } else {
                            System.out.println("❌ Failed to update profile.");
                        }
                        break;
                    case "4":
                        while (true) {
                            String builderId = promptNonEmpty(sc, "Enter assigned Builder ID");
                            if (BuilderRepository.getBuilderById(builderId) != null) {
                                manager.setBuilderId(builderId);
                                System.out.println("✅ Builder ID assigned.");
                                break;
                            } else {
                                System.out.println("❌ Invalid Builder ID. No such builder exists. Please try again.");
                            }
                        }
                        break;

                    case "5":
                        AddressEditor.editAddress(sc, address);
                        if (ManagerRepository.updateManager(manager, address)) {
                            System.out.println("✅ Profile updated successfully!");
                        } else {
                            System.out.println("❌ Failed to update profile.");
                        }
                        break;
                    case "0":

                        boolean updated = ManagerRepository.updateManager(manager, address);
                        if (updated) {
                            System.out.println("✅ Profile updated successfully!");
                        } else {
                            System.out.println("❌ Failed to update profile.");
                        }
                        return;
                    default:
                        System.out.println("❌ Invalid choice.");
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error editing manager profile", e);
        }
    }

    public static void changePassword(String email) {
        Scanner sc = new Scanner(System.in);

        try {
            Manager manager = ManagerRepository.getManagerByEmail(email);
            if (manager == null) {
                System.out.println("❌ Manager not found.");
                return;
            }

            String hashedNewPassword = PasswordManager.handlePasswordChange(sc, manager.getManagerPassword());

            if (hashedNewPassword != null) {
                manager.setManagerPassword(hashedNewPassword);

                boolean updated = ManagerRepository.updatePassword(email, hashedNewPassword);
                if (updated) {
                    System.out.println("✅ Password changed successfully.");
                } else {
                    System.out.println("❌ Failed to change password.");
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error changing password", e);
        }
    }
}
