package org.example.Util;

public class ValidationUtil {
    public static boolean isValidPassword(String password) {
        if (password.length() > 10) return false;
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).+$");
    }
}
