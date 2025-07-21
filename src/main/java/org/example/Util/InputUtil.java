package org.example.Util;
import java.util.Scanner;

/**
 * Utility class for centralized input handling.
 */
public class InputUtil {
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     Prompts the user for input with the given message.
     @param message Prompt message to display.
     @return Trimmed user input.
     */
    public static String prompt(String message) {
        System.out.print(message);
        return SCANNER.nextLine().trim();
    }
}
