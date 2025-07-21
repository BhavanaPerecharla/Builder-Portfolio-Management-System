package org.example.Service;
import static org.example.Util.InputValidator.promptNonEmpty;
import static org.example.Util.InputValidator.promptValidContact;
import org.example.Model.Client;
import org.example.Model.Address;
import org.example.Repository.ClientRepository;
import org.example.Repository.AddressRepository;
import org.example.Util.AddressEditor;
import org.example.Util.PasswordManager;
import org.example.Util.PasswordUtil;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientService {
    private static final Logger logger = Logger.getLogger(ClientService.class.getName());

    public static void viewProfile(String email) {
        try {
            Client client = ClientRepository.getClientByEmail(email);
            if (client == null) {
                System.out.println("❌ Client not found.");
                return;
            }

            Address address = AddressRepository.getAddressById(client.getAddressId());

            System.out.println("\n👤===== Client Profile =====");
            System.out.println("🔹 Name: " + client.getClientName());
            System.out.println("🔹 Email: " + client.getClientEmail());
            System.out.println("🔹 Contact: " + client.getClientContact());
            System.out.println("🔹 Type: " + client.getClientType());

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
            logger.log(Level.SEVERE, "Error viewing client profile", e);
        }
    }

    public static void editProfile(String email) {
        Scanner sc = new Scanner(System.in);

        try {
            Client client = ClientRepository.getClientByEmail(email);
            if (client == null) {
                System.out.println("❌ Client not found.");
                return;
            }

            Address address = AddressRepository.getAddressById(client.getAddressId());
            if (address == null) {
                address = new Address();
                address.setAddressId(client.getAddressId());
            }

            while (true) {
                System.out.println("\n🛠️===== Edit Profile =====");
                System.out.println("[1] Edit Name");
                System.out.println("[2] Edit Contact");
                System.out.println("[3] Edit Client Type");
                System.out.println("[4] Edit Address");
                System.out.println("[0] Back to Dashboard");
                System.out.print("👉 Enter your choice: ");
                String choice = sc.nextLine().trim();

                switch (choice) {
                    case "1":
                        client.setClientName(promptNonEmpty(sc, "Enter new name"));
                        break;
                    case "2":
                        client.setClientContact(promptValidContact(sc, "Enter new contact number"));
                        break;
                    case "3":
                        client.setClientType(promptNonEmpty(sc, "Enter client type"));
                        break;
                    case "4":
                        AddressEditor.editAddress(sc, address);
                        break;

                    case "0":
                        ClientRepository.updateClient(client, address);
                        System.out.println("✅ Profile updated successfully!");
                        return;
                    default:
                        System.out.println("❌ Invalid choice.");
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error editing client profile", e);
        }
    }

    // Add method to get client ID from email:
    public static String getClientIdByEmail(String clientEmail) {
        return ClientRepository.getClientIdByEmail(clientEmail);
    }



    public static void changePassword(String email) {
        Scanner sc = new Scanner(System.in);

        try {
            Client client = ClientRepository.getClientByEmail(email);
            if (client == null) {
                System.out.println("❌ Client not found.");
                return;
            }

            String hashedNewPassword = PasswordManager.handlePasswordChange(sc, client.getClientPassword());

            if (hashedNewPassword != null) {
                client.setClientPassword(hashedNewPassword);

                boolean updated = ClientRepository.updatePassword(email, hashedNewPassword);
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
