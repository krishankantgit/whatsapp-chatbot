package com.chatbot.controller;

import com.chatbot.model.MessageRequest;
import com.chatbot.model.MessageResponse;
import com.chatbot.service.ChatbotService;
import com.chatbot.service.RateLimiterService;
import com.chatbot.util.InputSanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @Autowired private ChatbotService chatbotService;
    @Autowired private RateLimiterService rateLimiterService;
    @Autowired private InputSanitizer inputSanitizer;

    @PostMapping
    public ResponseEntity<?> receiveMessage(
            @RequestBody MessageRequest request,
            HttpServletRequest httpRequest) {

        String clientIp = getClientIp(httpRequest);
        logger.info("Request from IP: {}", clientIp);

        // 1. Rate Limit Check
        if (!rateLimiterService.isAllowed(clientIp)) {
            logger.warn("Rate limit hit for IP: {}", clientIp);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Too many requests. Please wait a minute.");
            error.put("status", "429");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(error);
        }

        // 2. Null Check
        if (request == null) {
            return badRequest("Request body is missing.");
        }

        // 3. Sanitize Inputs
        String sanitizedMessage = inputSanitizer.sanitizeMessage(request.getMessage());
        String sanitizedPhone   = inputSanitizer.sanitizePhone(request.getFrom());

        // 4. Validate Inputs
        if (!inputSanitizer.isValidMessage(sanitizedMessage)) {
            return badRequest("Message is empty or too long (max 500 characters).");
        }

        if (!inputSanitizer.isValidPhone(sanitizedPhone)) {
            return badRequest("Invalid phone number format.");
        }

        // 5. Set sanitized values back
        request.setMessage(sanitizedMessage);
        request.setFrom(sanitizedPhone);

        // 6. Process and respond
        MessageResponse response = chatbotService.processMessage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> verifyWebhook(
            @RequestParam(value = "hub.mode",         required = false) String mode,
            @RequestParam(value = "hub.verify_token", required = false) String token,
            @RequestParam(value = "hub.challenge",    required = false) String challenge) {

        Map<String, String> response = new HashMap<>();
        if ("subscribe".equals(mode) && "my_verify_token".equals(token)) {
            response.put("challenge", challenge);
            response.put("status", "verified");
            return ResponseEntity.ok(response);
        }
        response.put("status", "ok");
        response.put("message", "WhatsApp Chatbot is running!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> resp = new HashMap<>();
        resp.put("status", "UP");
        resp.put("service", "WhatsApp Chatbot Backend");
        return ResponseEntity.ok(resp);
    }

    // Helper: extract real IP (works behind proxy/Render)
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) {
            return ip.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private ResponseEntity<Map<String, String>> badRequest(String message) {
        logger.warn("Bad request: {}", message);
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        error.put("status", "400");
        return ResponseEntity.badRequest().body(error);
    }
}