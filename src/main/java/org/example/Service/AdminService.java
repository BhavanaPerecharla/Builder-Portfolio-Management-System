package org.example.Service;

import org.example.Model.Admin;
import org.example.Model.Address;
import org.example.Repository.AdminRepository;
import org.example.Repository.AddressRepository;

import org.example.Util.AddressEditor;
import org.example.Util.PasswordManager;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.Util.InputValidator.promptNonEmpty;
import static org.example.Util.InputValidator.promptValidContact;

public class AdminService {
    private static final Logger logger = Logger.getLogger(AdminService.class.getName());

    public static void viewProfile(String email) {
        try {
            Admin admin = AdminRepository.getAdminByEmail(email);
            if (admin == null) {
                System.out.println("❌ Admin not found.");
                return;
            }

            Address address = AddressRepository.getAddressById(admin.getAddressId());

            System.out.println("\n👤===== Admin Profile =====");
            System.out.println("🔹 Name: " + admin.getAdminName());
            System.out.println("🔹 Email: " + admin.getAdminEmail());
            System.out.println("🔹 Contact: " + admin.getAdminContact());

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
            logger.log(Level.SEVERE, "Error viewing admin profile", e);
        }
    }

    public static void editProfile(String email) {
        viewProfile(email); // Show current profile before editing
        Scanner sc = new Scanner(System.in);

        try {
            Admin admin = AdminRepository.getAdminByEmail(email);
            if (admin == null) {
                System.out.println("❌ Admin not found.");
                return;
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
                        boolean updated = AdminRepository.updateAdmin(admin, address);
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
            logger.log(Level.SEVERE, "Error editing admin profile", e);
        }
    }

    public static void changePassword(String email) {
        Scanner sc = new Scanner(System.in);

        try {
            Admin admin = AdminRepository.getAdminByEmail(email);
            if (admin == null) {
                System.out.println("❌ Admin not found.");
                return;
            }

            String hashedNewPassword = PasswordManager.handlePasswordChange(sc, admin.getAdminPassword());

            if (hashedNewPassword != null) {
                admin.setAdminPassword(hashedNewPassword);

                boolean updated = AdminRepository.updatePassword(email, hashedNewPassword);
                if (updated) {
                    System.out.println("✅ Password changed successfully.");
                } else {
                    System.out.println("❌ Failed to change password.");
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error changing admin password", e);
        }
    }
}
