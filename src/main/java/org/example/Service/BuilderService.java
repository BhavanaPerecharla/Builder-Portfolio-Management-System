package org.example.Service;
import org.example.Repository.AddressRepository; // Adjust the package as needed

import org.example.Model.Address;
import org.example.Model.Builder;
import org.example.Repository.BuilderRepository;
import org.example.Util.PasswordUtil;
import org.example.Repository.AddressRepository;

import java.util.Scanner;

import java.util.logging.Level;
import java.util.logging.Logger;

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
                System.out.println("[4] Change Password");
                System.out.println("[0] Back to Dashboard");
                System.out.print("👉 Enter your choice: ");
                String choice = sc.nextLine().trim();

                switch (choice) {
                    case "1":
                        String name = promptNonEmpty(sc, "Enter new name");
                        builder.setBuilderName(name);
                        break;

                    case "2":
                        while (true) {
                            String contact = promptNonEmpty(sc, "Enter Contact Number (10 digits)");
                            if (!contact.matches("\\d{10}")) {
                                System.out.println("❌ Invalid phone number. Enter exactly 10 digits.");
                            } else {
                                builder.setBuilderContact(contact);
                                break;
                            }
                        }
                        break;

                    case "3":
                        Address address = AddressRepository.getAddressById(builder.getAddressId());

                        if (address == null) {
                            address = new Address();
                            address.setAddressId(builder.getAddressId());

                        }
                        builder.setAddress(address);

                        while (true) {
                            System.out.println("\n🏠===== Edit Address =====");
                            System.out.println("[1] Edit Address Line 1");
                            System.out.println("[2] Edit City");
                            System.out.println("[3] Edit State");
                            System.out.println("[4] Edit Zip Code");
                            System.out.println("[5] Edit Country");
                            System.out.println("[0] Done Editing Address");
                            System.out.print("👉 Choose field to edit: ");
                            String addrChoice = sc.nextLine().trim();

                            switch (addrChoice) {
                                case "1":
                                    address.setAddressLine1(promptNonEmpty(sc, "Enter Address Line 1"));
                                    break;
                                case "2":
                                    address.setCity(promptNonEmpty(sc, "Enter City"));
                                    break;
                                case "3":
                                    address.setStates(promptNonEmpty(sc, "Enter State"));
                                    break;
                                case "4":
                                    address.setZipCode(promptNonEmpty(sc, "Enter Zip Code"));
                                    break;
                                case "5":
                                    address.setCountry(promptNonEmpty(sc, "Enter Country"));
                                    break;
                                case "0":
                                    AddressRepository.updateAddress(address);

                                    System.out.println("✅ Address updated successfully.");
                                    break;
                                default:
                                    System.out.println("❌ Invalid choice.");
                            }

                            if (addrChoice.equals("0")) break;
                        }
                        break;

                    case "4":
                        int attempts = 3;
                        while (attempts-- > 0) {
                            System.out.print("🔐 Enter current password: ");
                            String currentPassword = sc.nextLine().trim();

                            if (PasswordUtil.verifyPassword(currentPassword, builder.getBuilderPassword())) {
                                break;
                            } else {
                                System.out.println("❌ Incorrect current password.");
                                if (attempts > 0) {
                                    System.out.println("🔁 Try again. Attempts left: " + attempts);
                                } else {
                                    System.out.println("🚫 Too many failed attempts. Returning to dashboard.");
                                    return;
                                }
                            }
                        }
                        System.out.println("Enter a new password that meets the strength requirements:");
                        System.out.println("\n📜 Password Guidelines:");
                        System.out.println("• Minimum 10 characters");
                        System.out.println("• At least 1 uppercase letter");
                        System.out.println("• At least 1 lowercase letter");
                        System.out.println("• At least 1 digit");
                        System.out.println("• At least 1 special character (@$!%*?&)");

                        String newPassword;
                        while (true) {
                            System.out.print("🔑 Enter new password: ");
                            newPassword = sc.nextLine().trim();

                            if (!PasswordUtil.isValidPassword(newPassword)) {
                                System.out.println("❌ Password does not meet strength requirements.");
                                continue;
                            }

                            System.out.print("🔁 Confirm new password: ");
                            String confirmPassword = sc.nextLine().trim();

                            if (!newPassword.equals(confirmPassword)) {
                                System.out.println("❌ Passwords do not match. Please try again.");
                            } else {
                                break;
                            }
                        }

                        String hashedPassword = PasswordUtil.hashPassword(newPassword);
                        builder.setBuilderPassword(hashedPassword);

                        boolean updated = BuilderRepository.updatePassword(email, hashedPassword);
                        if (updated) {
                            System.out.println("✅ Password updated successfully!");
                        } else {
                            System.out.println("❌ Failed to update password.");
                        }
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

    private static String promptNonEmpty(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("⚠️ This field cannot be empty.");
            }
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

            System.out.print("🔐 Enter Current Password: ");
            String currentPassword = sc.nextLine();

            if (!PasswordUtil.verifyPassword(currentPassword, builder.getBuilderPassword())) {
                System.out.println("❌ Incorrect current password.");
                return;
            }

            String newPassword;
            while (true) {
                System.out.print("🔑 Enter new password: ");
                newPassword = sc.nextLine().trim();

                if (!PasswordUtil.isValidPassword(newPassword)) {
                    System.out.println("❌ Password does not meet strength requirements.");
                    continue;
                }

                System.out.print("🔁 Confirm new password: ");
                String confirmPassword = sc.nextLine().trim();

                if (!newPassword.equals(confirmPassword)) {
                    System.out.println("❌ Passwords do not match. Please try again.");
                } else {
                    break;
                }
            }

            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            builder.setBuilderPassword(hashedPassword);
            boolean updated = BuilderRepository.updatePassword(email, hashedPassword);
            if (updated) {
                System.out.println("✅ Password changed successfully.");
            } else {
                System.out.println("❌ Failed to change password.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error changing password", e);
        }
    }
}
