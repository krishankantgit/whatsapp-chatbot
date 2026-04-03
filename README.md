# WhatsApp Chatbot Backend — Spring Boot

A REST API backend simulating a WhatsApp chatbot webhook using Spring Boot.

## Project Structure

```
whatsapp-chatbot/
├── src/
│   ├── main/
│   │   ├── java/com/chatbot/
│   │   │   ├── WhatsAppChatbotApplication.java   ← Entry point
│   │   │   ├── controller/
│   │   │   │   └── WebhookController.java        ← REST endpoints
│   │   │   ├── model/
│   │   │   │   ├── MessageRequest.java           ← Incoming JSON shape
│   │   │   │   └── MessageResponse.java          ← Outgoing JSON shape
│   │   │   └── service/
│   │   │       └── ChatbotService.java           ← Reply logic + logging
│   │   └── resources/
│   │       └── application.properties
│   └── test/java/com/chatbot/
│       └── ChatbotServiceTest.java               ← Unit tests
└── pom.xml
```

## Prerequisites

- Java 17+
- Maven 3.8+

## Run Locally

```bash
# Clone or download the project
cd whatsapp-chatbot

# Build & run
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`

## API Endpoints

### POST /webhook  — Receive a message

**Request body:**
```json
{
  "from": "+91-9876543210",
  "message": "Hi",
  "timestamp": "2024-01-15 10:30:00"
}
```

**Response:**
```json
{
  "to": "+91-9876543210",
  "reply": "Hello! How can I help you today?",
  "status": "sent",
  "timestamp": "2024-01-15 10:30:01"
}
```

### GET /webhook — Health / verification check

```bash
curl http://localhost:8080/webhook
```

### GET /webhook/health

```bash
curl http://localhost:8080/webhook/health
```

## Predefined Replies

| Input (case-insensitive) | Bot Reply |
|---|---|
| hi / hello | Hello! How can I help you today? |
| bye / goodbye | Goodbye! Have a great day! |
| help | Sure! You can say Hi, Bye, or ask about our services. |
| thanks / thank you | You're welcome! Is there anything else I can help you with? |
| how are you | I'm a bot, but I'm doing great! How can I assist you? |
| *(anything else)* | Sorry, I didn't understand that. Type 'help' to see what I can do! |

## Test with cURL

```bash
# Say Hi
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"from":"+91-9876543210","message":"Hi","timestamp":"2024-01-15 10:00:00"}'

# Say Bye
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"from":"+91-9876543210","message":"Bye","timestamp":"2024-01-15 10:01:00"}'

# Unknown message
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"from":"+91-9876543210","message":"what is java?","timestamp":"2024-01-15 10:02:00"}'
```

## Deploy on Render (Bonus)

1. Push this project to a GitHub repository
2. Go to [render.com](https://render.com) and create a new **Web Service**
3. Connect your GitHub repo
4. Set these settings:
   - **Runtime**: Java
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/whatsapp-chatbot-1.0.0.jar`
5. Click **Deploy**
6. Your webhook URL will be: `https://your-app.onrender.com/webhook`

## Run Tests

```bash
mvn test
```

## Logging

All incoming and outgoing messages are logged to the console:

```
=== INCOMING MESSAGE ===
From    : +91-9876543210
Message : Hi
At      : 2024-01-15 10:30:00
========================
=== OUTGOING REPLY ===
To      : +91-9876543210
Reply   : Hello! How can I help you today?
At      : 2024-01-15 10:30:01
======================
```
