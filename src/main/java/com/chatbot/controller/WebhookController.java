package com.chatbot.controller;

import com.chatbot.model.MessageRequest;
import com.chatbot.model.MessageResponse;
import com.chatbot.service.ChatbotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
@CrossOrigin(origins = "*")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @Autowired
    private ChatbotService chatbotService;

    /**
     * POST /webhook — receives a WhatsApp-style message and returns a bot reply
     */
    @PostMapping
    public ResponseEntity<MessageResponse> receiveMessage(@RequestBody MessageRequest request) {
        logger.info("POST /webhook called");

        if (request.getMessage() == null || request.getMessage().isBlank()) {
            logger.warn("Received request with empty message body");
            return ResponseEntity.badRequest().build();
        }

        MessageResponse response = chatbotService.processMessage(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /webhook — health check / webhook verification (simulates Meta webhook verification)
     */
    @GetMapping
    public ResponseEntity<Map<String, String>> verifyWebhook(
            @RequestParam(value = "hub.mode", required = false) String mode,
            @RequestParam(value = "hub.verify_token", required = false) String token,
            @RequestParam(value = "hub.challenge", required = false) String challenge) {

        logger.info("GET /webhook verification request — mode: {}, token: {}", mode, token);

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

    /**
     * GET /webhook/health — simple health endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> resp = new HashMap<>();
        resp.put("status", "UP");
        resp.put("service", "WhatsApp Chatbot Backend");
        return ResponseEntity.ok(resp);
    }
}
