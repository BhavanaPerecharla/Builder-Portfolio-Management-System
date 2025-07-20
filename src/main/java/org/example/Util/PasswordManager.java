package org.example.Util;

import java.util.Scanner;

public class PasswordManager {

    private static final int MAX_ATTEMPTS = 3;

    /**
     * Handles full password change process:
     * 1️⃣ Verifies current password with multiple attempts.
     * 2️⃣ Collects and validates new password.
     * 3️⃣ Returns hashed new password or null if failed.
     *
     * @param sc Scanner instance
     * @param currentHashedPassword User's current hashed password
     * @return New hashed password or null if failed
     */
    public static String handlePasswordChange(Scanner sc, String currentHashedPassword) {
        if (!verifyCurrentPasswordWithAttempts(sc, currentHashedPassword, MAX_ATTEMPTS)) {
            return null;  // Too many failed attempts
        }

        String newPassword = promptNewValidPassword(sc);
        return PasswordUtil.hashPassword(newPassword);
    }

    /**
     * Prompts user for current password with limited attempts.
     *
     * @param sc Scanner instance
     * @param currentHashedPassword Hashed password to verify against
     * @param maxAttempts Maximum allowed attempts
     * @return true if correct password entered, false otherwise
     */
    private static boolean verifyCurrentPasswordWithAttempts(Scanner sc, String currentHashedPassword, int maxAttempts) {
        int attempts = maxAttempts;
        while (attempts-- > 0) {
            System.out.print("🔐 Enter Current Password: ");
            String currentPassword = sc.nextLine();

            if (PasswordUtil.verifyPassword(currentPassword, currentHashedPassword)) {
                return true;  // Correct password
            } else {
                System.out.println("❌ Incorrect password.");
                if (attempts > 0) {
                    System.out.println("🔁 Attempts remaining: " + attempts);
                } else {
                    System.out.println("🚫 Too many failed attempts. Password change aborted.");
                }
            }
        }
        return false;  // Failed after max attempts
    }

    /**
     * Prompts user for a new valid password, confirming it before accepting.
     *
     * @param sc Scanner instance
     * @return Valid new password (plain text)
     */
    private static String promptNewValidPassword(Scanner sc) {
        String newPassword;

        System.out.println("\n📜 Password Guidelines:");
        System.out.println("• Minimum 10 characters");
        System.out.println("• At least 1 uppercase letter");
        System.out.println("• At least 1 lowercase letter");
        System.out.println("• At least 1 digit");
        System.out.println("• At least 1 special character (@$!%*?&)");

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

        return newPassword;
    }
}
