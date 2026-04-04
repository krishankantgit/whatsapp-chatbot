package com.chatbot.util;

import org.springframework.stereotype.Component;

@Component
public class InputSanitizer {

    private static final int MAX_MESSAGE_LENGTH = 500;
    private static final int MAX_PHONE_LENGTH = 20;

    // Remove HTML/JS tags to prevent XSS
    public String sanitizeMessage(String input) {
        if (input == null) return null;

        String sanitized = input
                .replaceAll("<[^>]*>", "")           // Remove HTML tags
                .replaceAll("javascript:", "")         // Remove JS injections
                .replaceAll("on\\w+\\s*=", "")        // Remove event handlers
                .replaceAll("[<>\"'%;()&+]", "")      // Remove special chars
                .trim();

        // Limit length
        if (sanitized.length() > MAX_MESSAGE_LENGTH) {
            sanitized = sanitized.substring(0, MAX_MESSAGE_LENGTH);
        }

        return sanitized;
    }

    public String sanitizePhone(String input) {
        if (input == null) return null;

        String sanitized = input
                .replaceAll("[^+\\d\\-\\s]", "") // Only allow +, digits, -, space
                .trim();

        if (sanitized.length() > MAX_PHONE_LENGTH) {
            sanitized = sanitized.substring(0, MAX_PHONE_LENGTH);
        }

        return sanitized;
    }

    // Validate phone number format
    public boolean isValidPhone(String phone) {
        if (phone == null || phone.isBlank()) return false;
        return phone.matches("^[+]?[\\d\\s\\-]{7,20}$");
    }

    // Validate message is not empty or blank
    public boolean isValidMessage(String message) {
        return message != null && !message.isBlank() && message.length() <= MAX_MESSAGE_LENGTH;
    }
}