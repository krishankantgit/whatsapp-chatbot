# WhatsApp Chatbot Backend — Spring Boot

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)
![Deployed](https://img.shields.io/badge/Deployed-Render-purple)

A fully functional WhatsApp chatbot backend simulation built with **Java** and **Spring Boot**. This project exposes a REST API webhook that accepts WhatsApp-style JSON messages and responds with predefined intelligent replies. It also includes a responsive WhatsApp-style frontend UI.

---

## Live Demo

| | Link |
|---|---|
| **Live Frontend** | https://whatsapp-chatbot-ys3u.onrender.com |
| **Webhook API** | https://whatsapp-chatbot-ys3u.onrender.com/webhook |
| **Health Check** | https://whatsapp-chatbot-ys3u.onrender.com/webhook/health |
| **GitHub Repo** | https://github.com/krishankantgit/whatsapp-chatbot |

---

## Features

- REST API endpoint (`/webhook`) to receive POST requests
- Accepts JSON input simulating WhatsApp messages
- Predefined intelligent replies (Hi, Bye, Help, Thanks, etc.)
- Logs all incoming and outgoing messages with timestamps
- Responsive WhatsApp-style frontend UI (works on mobile and desktop)
- Security: CORS configuration, IP rate limiting, input sanitization, request validation
- Deployed on Render (free hosting)
- Dockerized for easy deployment

---

## Project Structure

```
whatsapp-chatbot/
├── src/
│   ├── main/
│   │   ├── java/com/chatbot/
│   │   │   ├── WhatsAppChatbotApplication.java    ← Entry point
│   │   │   ├── controller/
│   │   │   │   └── WebhookController.java         ← REST endpoints
│   │   │   ├── model/
│   │   │   │   ├── MessageRequest.java            ← Incoming JSON
│   │   │   │   └── MessageResponse.java           ← Outgoing JSON
│   │   │   ├── service/
│   │   │   │   ├── ChatbotService.java            ← Reply logic + logging
│   │   │   │   └── RateLimiterService.java        ← IP rate limiting
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java            ← CORS configuration
│   │   │   └── util/
│   │   │       └── InputSanitizer.java            ← Input sanitization
│   │   └── resources/
│   │       ├── static/
│   │       │   └── index.html                     ← WhatsApp Frontend UI
│   │       └── application.properties
│   └── test/
│       └── java/com/chatbot/
│           └── ChatbotServiceTest.java             ← Unit tests
├── Dockerfile
├── pom.xml
└── README.md
```

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot 3.2.0 | Backend framework |
| Maven | Build tool |
| SLF4J | Logging |
| Docker | Containerization |
| Render | Cloud deployment |
| HTML/CSS/JS | Frontend UI |

---

## API Endpoints

### POST `/webhook` — Send a message

**Request:**
```json
{
  "from": "+91-9876543210",
  "message": "Hi",
  "timestamp": "2024-01-15 10:00:00"
}
```

**Response:**
```json
{
  "to": "+91-9876543210",
  "reply": "Hello! How can I help you today?",
  "status": "sent",
  "timestamp": "2024-01-15 10:00:01"
}
```

---

### GET `/webhook` — Server status / webhook verification

**Response:**
```json
{
  "status": "ok",
  "message": "WhatsApp Chatbot is running!"
}
```

---

### GET `/webhook/health` — Health check

**Response:**
```json
{
  "status": "UP",
  "service": "WhatsApp Chatbot Backend"
}
```

---

## Predefined Replies

| User Message | Bot Reply |
|---|---|
| Hi / Hello | Hello! How can I help you today? |
| Bye / Goodbye | Goodbye! Have a great day! |
| help | Sure! You can say Hi, Bye, or ask about our services. |
| thanks / thank you | You're welcome! Is there anything else I can help you with? |
| how are you | I'm a bot, but I'm doing great! How can I assist you? |
| *(anything else)* | Sorry, I didn't understand that. Type 'help' to see what I can do! |

---

## Security Features

| Feature | Description |
|---|---|
| CORS Configuration | Only allows requests from whitelisted origins |
| IP Rate Limiting | Max 10 requests per minute per IP address |
| Input Sanitization | Removes HTML tags, JS injections, special characters |
| Request Validation | Rejects empty, null, or oversized messages |
| IP Logging | All suspicious requests are logged with IP address |

---

## Run Locally

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps

```bash
# Clone the repository
git clone https://github.com/krishankantgit/whatsapp-chatbot.git

# Go into the project folder
cd whatsapp-chatbot

# Run the application
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`

Frontend UI: `http://localhost:8080`

---

## Test with cURL

```bash
# Say Hi
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"from":"+91-9876543210","message":"Hi","timestamp":"2024-01-15 10:00:00"}'

# Say Bye
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"from":"+91-9876543210","message":"Bye","timestamp":"2024-01-15 10:00:00"}'

# Ask for help
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"from":"+91-9876543210","message":"help","timestamp":"2024-01-15 10:00:00"}'
```

---

## Test with Postman

1. Open Postman
2. Method: **POST**
3. URL: `http://localhost:8080/webhook`
4. Body → raw → JSON:
```json
{
  "from": "+91-9876543210",
  "message": "Hi",
  "timestamp": "2024-01-15 10:00:00"
}
```
5. Click **Send**

---

## Run with Docker

```bash
# Build Docker image
docker build -t whatsapp-chatbot .

# Run Docker container
docker run -p 8080:8080 whatsapp-chatbot
```

---

## Deploy on Render

1. Push code to GitHub
2. Go to [render.com](https://render.com) → New → Web Service
3. Connect your GitHub repository
4. Select **Docker** as language
5. Select **Free** instance type
6. Click **Deploy Web Service**

> **Note:** On Render's free tier, the app sleeps after 15 minutes of inactivity. First request after sleep takes 30-60 seconds to wake up.

---

## Message Logging

All messages are logged to the console:

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

---

## Run Tests

```bash
mvn test
```

---

## Frontend UI

The project includes a fully responsive WhatsApp-style frontend UI built with HTML, CSS, and JavaScript.

- Works on **mobile and desktop**
- Connects to the **live Render API** in real time
- Falls back to **local replies** if API is offline
- Shows **typing indicator**, **double blue ticks**, and **timestamps**
- Includes **quick reply buttons** for common messages

---

## Author

**krishan kant** — Java Developer Intern Assignment  
GitHub: [@krishankantgit](https://github.com/krishankantgit)

---

## License

This project is open source and available under the [MIT License](LICENSE).
