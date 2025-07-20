package org.example.Util;



import org.example.Model.Address;
import org.example.Repository.AddressRepository;

import java.util.Scanner;

import static org.example.Util.InputValidator.promptNonEmpty;

public class AddressEditor {

    public static void editAddress(Scanner sc, Address address) {
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
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }
}
