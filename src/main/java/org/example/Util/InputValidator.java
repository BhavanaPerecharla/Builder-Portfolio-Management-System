package org.example.Util;

import java.util.Scanner;
import java.util.regex.Pattern;

public class InputValidator {

    private static final Pattern CONTACT_PATTERN = Pattern.compile("^\\d{10}$");

    /**
     * Prompts user until a non-empty string is entered.
     *
     * @param sc     Scanner instance
     * @param prompt Prompt message
     * @return Valid non-empty input
     */
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
    public static String promptValidContact(Scanner sc) {
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
}
