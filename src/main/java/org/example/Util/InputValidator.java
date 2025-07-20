package org.example.Util;

import org.example.Model.Manager;
import org.example.Repository.BuilderRepository;
import org.example.Repository.ClientRepository;
import org.example.Repository.ManagerRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InputValidator {

    private static final Pattern CONTACT_PATTERN = Pattern.compile("^\\d{10}$");


    public static String promptNonEmpty(Scanner sc, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt + ": ");
            input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                break;
            } else {
                System.out.println("❌ Input cannot be empty. Please try again.");
            }
        }
        return input;
    }

    /**
     * Prompts user for a valid 10-digit contact number.
     *
     * @param sc Scanner instance
     * @return Valid contact number
     */
    public static String promptValidContact(Scanner sc,String prompt) {
        String contact;
        while (true) {
            System.out.print("📞 Enter 10-digit contact number: ");
            contact = sc.nextLine().trim();
            if (CONTACT_PATTERN.matcher(contact).matches()) {
                break;
            } else {
                System.out.println("❌ Invalid contact number. It should be 10 digits.");
            }
        }
        return contact;
    }
    public static Date promptValidDate(Scanner sc, String prompt) {
        while (true) {
            try {
                String input = promptNonEmpty(sc, prompt);
                return Date.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }
    public static java.sql.Date promptOptionalDate(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + " (Leave blank if not applicable): ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                return java.sql.Date.valueOf(input); // Format: YYYY-MM-DD
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }


    public static BigDecimal promptValidAmount(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = sc.nextLine().trim();
            try {
                BigDecimal amount = new BigDecimal(input);
                if (amount.compareTo(BigDecimal.ZERO) >= 0) {
                    return amount;
                } else {
                    System.out.println("❌ Amount must be non-negative.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number format. Please enter a valid number.");
            }
        }
    }
    /**
     * Prompts the user to enter a valid project status.
     * The status can be either 'Upcoming', 'In Progress', or 'Completed'.
     *
     * @param sc Scanner instance
     * @return Valid project status
     */
    public static String promptValidProjectStatus(Scanner sc) {
        while (true) {
            System.out.print("Enter Project Status (Upcoming / In Progress / Completed): ");
            String status = sc.nextLine().trim();

            if ("Upcoming".equalsIgnoreCase(status) ||
                    "In Progress".equalsIgnoreCase(status) ||
                    "Completed".equalsIgnoreCase(status)) {
                return status;  // Return valid status
            } else {
                System.out.println("❌ Invalid status. Choose: Upcoming, In Progress, or Completed.");
            }
        }
    }
}

