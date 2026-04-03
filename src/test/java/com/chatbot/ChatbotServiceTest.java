package com.chatbot;

import com.chatbot.model.MessageRequest;
import com.chatbot.model.MessageResponse;
import com.chatbot.service.ChatbotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatbotServiceTest {

    private ChatbotService chatbotService;

    @BeforeEach
    void setUp() {
        chatbotService = new ChatbotService();
    }

    @Test
    void testHiReply() {
        MessageRequest req = new MessageRequest("+91-9876543210", "Hi", null);
        MessageResponse res = chatbotService.processMessage(req);
        assertEquals("Hello! How can I help you today?", res.getReply());
        assertEquals("sent", res.getStatus());
    }

    @Test
    void testHiCaseInsensitive() {
        MessageRequest req = new MessageRequest("+91-9876543210", "HI", null);
        MessageResponse res = chatbotService.processMessage(req);
        assertEquals("Hello! How can I help you today?", res.getReply());
    }

    @Test
    void testByeReply() {
        MessageRequest req = new MessageRequest("+91-9876543210", "Bye", null);
        MessageResponse res = chatbotService.processMessage(req);
        assertEquals("Goodbye! Have a great day!", res.getReply());
    }

    @Test
    void testUnknownMessageReply() {
        MessageRequest req = new MessageRequest("+91-9876543210", "random text xyz", null);
        MessageResponse res = chatbotService.processMessage(req);
        assertTrue(res.getReply().contains("didn't understand"));
    }

    @Test
    void testResponseHasSenderInfo() {
        MessageRequest req = new MessageRequest("+91-1234567890", "hello", null);
        MessageResponse res = chatbotService.processMessage(req);
        assertEquals("+91-1234567890", res.getTo());
    }
}
