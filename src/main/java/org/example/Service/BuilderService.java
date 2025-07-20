package org.example.Service;
import org.example.Repository.AddressRepository; // Adjust the package as needed
import org.example.Util.AddressEditor;
import org.example.Util.PasswordManager;
import org.example.Model.Address;
import org.example.Model.Builder;
import org.example.Repository.BuilderRepository;


import java.util.Scanner;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.Util.InputValidator.promptNonEmpty;
import static org.example.Util.InputValidator.promptValidContact;

public class BuilderService {
    private static final Logger logger = Logger.getLogger(BuilderService.class.getName());

    public static void viewProfile(String email) {
        try {
            Builder builder = BuilderRepository.getBuilderByEmail(email);
            if (builder == null) {
                System.out.println("❌ Builder not found.");
                return;
            }

            Address address = AddressRepository.getAddressById(builder.getAddressId());


            System.out.println("\n👤===== Builder Profile =====");
            System.out.println("🔹 Name: " + builder.getBuilderName());
            System.out.println("🔹 Email: " + builder.getBuilderEmail());
            System.out.println("🔹 Contact: " + builder.getBuilderContact());

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
            logger.log(Level.SEVERE, "Error viewing builder profile", e);
        }
    }

    public static void editProfile(String email) {
        Scanner sc = new Scanner(System.in);
        try {
            Builder builder = BuilderRepository.getBuilderByEmail(email);
            if (builder == null) {
                System.out.println("❌ Builder not found.");
                return;
            }
            if (builder.getAddress() == null) {
                Address address = AddressRepository.getAddressById(builder.getAddressId());
                if (address == null) {
                    address = new Address(); // 🆕 create new address object
                    address.setAddressId(builder.getAddressId()); // 🆕 set the addressId
                }
                builder.setAddress(address); // 🆕 attach to builder to avoid null
            }


            while (true) {
                System.out.println("\n🛠️===== Edit Profile =====");
                System.out.println("[1] Edit Name");
                System.out.println("[2] Edit Contact");
                System.out.println("[3] Edit Address");
                System.out.println("[0] Back to Dashboard");
                System.out.print("👉 Enter your choice: ");
                String choice = sc.nextLine().trim();

                switch (choice) {
                    case "1":
                        String name = promptNonEmpty(sc, "Enter new name");
                        builder.setBuilderName(name);
                        break;

                    case "2":
                        builder.setBuilderContact(promptValidContact(sc, "Enter new contact number"));
                        break;

                    case "3":
                        Address address = AddressRepository.getAddressById(builder.getAddressId());
                        if (address == null) {
                            address = new Address();
                            address.setAddressId(builder.getAddressId());
                        }
                        builder.setAddress(address);

                        AddressEditor.editAddress(sc, address);
                        break;



                    case "0":
                        System.out.println("🔙 Returning to dashboard...");
                        return;

                    default:
                        System.out.println("❌ Invalid choice. Please try again.");
                }

                boolean updated = BuilderRepository.updateBuilder(builder);
                if (updated) {
                    System.out.println("✅ Profile updated successfully!");
                } else {
                    System.out.println("❌ Failed to update profile.");
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating builder profile", e);
        }
    }


    public static void changePassword(String email) {
        Scanner sc = new Scanner(System.in);

        try {
            Builder builder = BuilderRepository.getBuilderByEmail(email);
            if (builder == null) {
                System.out.println("❌ Builder not found.");
                return;
            }

            String hashedNewPassword = PasswordManager.handlePasswordChange(sc, builder.getBuilderPassword());

            if (hashedNewPassword != null) {
                builder.setBuilderPassword(hashedNewPassword);

                boolean updated = BuilderRepository.updatePassword(email, hashedNewPassword);
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
