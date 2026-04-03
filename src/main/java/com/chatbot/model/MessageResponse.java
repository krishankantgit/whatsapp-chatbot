package com.chatbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageResponse {

    @JsonProperty("to")
    private String to;

    @JsonProperty("reply")
    private String reply;

    @JsonProperty("status")
    private String status;

    @JsonProperty("timestamp")
    private String timestamp;

    public MessageResponse() {}

    public MessageResponse(String to, String reply, String status, String timestamp) {
        this.to = to;
        this.reply = reply;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
