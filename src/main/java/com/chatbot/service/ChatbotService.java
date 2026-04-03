package com.chatbot.service;

import com.chatbot.model.MessageRequest;
import com.chatbot.model.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotService.class);

    private static final Map<String, String> REPLY_MAP = new HashMap<>();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        // Predefined replies
        REPLY_MAP.put("hi", "Hello! How can I help you today?");
        REPLY_MAP.put("hello", "Hello! How can I help you today?");
        REPLY_MAP.put("bye", "Goodbye! Have a great day!");
        REPLY_MAP.put("goodbye", "Goodbye! Have a great day!");
        REPLY_MAP.put("help", "Sure! You can say Hi, Bye, or ask about our services.");
        REPLY_MAP.put("thanks", "You're welcome! Is there anything else I can help you with?");
        REPLY_MAP.put("thank you", "You're welcome! Is there anything else I can help you with?");
        REPLY_MAP.put("how are you", "I'm a bot, but I'm doing great! How can I assist you?");
    }

    public MessageResponse processMessage(MessageRequest request) {
        // Log the incoming message
        logger.info("=== INCOMING MESSAGE ===");
        logger.info("From    : {}", request.getFrom());
        logger.info("Message : {}", request.getMessage());
        logger.info("At      : {}", request.getTimestamp() != null ? request.getTimestamp() : getCurrentTimestamp());
        logger.info("========================");

        String incomingMessage = request.getMessage() != null ? request.getMessage().trim().toLowerCase() : "";
        String replyText = generateReply(incomingMessage);

        String responseTimestamp = getCurrentTimestamp();

        logger.info("=== OUTGOING REPLY ===");
        logger.info("To      : {}", request.getFrom());
        logger.info("Reply   : {}", replyText);
        logger.info("At      : {}", responseTimestamp);
        logger.info("======================");

        return new MessageResponse(
                request.getFrom(),
                replyText,
                "sent",
                responseTimestamp
        );
    }

    private String generateReply(String message) {
        // Check for exact match first
        if (REPLY_MAP.containsKey(message)) {
            return REPLY_MAP.get(message);
        }

        // Check for partial keyword matches
        for (Map.Entry<String, String> entry : REPLY_MAP.entrySet()) {
            if (message.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // Default reply for unknown messages
        return "Sorry, I didn't understand that. Type 'help' to see what I can do!";
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(FORMATTER);
    }
}
