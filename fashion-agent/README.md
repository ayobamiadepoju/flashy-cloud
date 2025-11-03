# 👗 Fashion AI Agent – Telex.im Integration

This project is a multi-agent AI system that provides weather-based outfit suggestions and generates visual representations of fashion looks. It is built using [Google ADK](https://github.com/google/adk), powered by Gemini models, and deployed as a Spring Boot service with webhook support for [Telex.im](https://telex.im).

---

## 🚀 Features

- 🌤️ Weather-aware outfit recommendations
- 👗 Fashion prompt generation
- 💬 Friendly chat responses (e.g., “Hi”, “Hello”)
- 🔌 Telex.im integration via Webhook A2A protocol

---

- `fashion_agent`: Orchestrates workflow: gets weather → suggests outfit
- `weather_agent`: Fetches current weather for a given location
- `outfit_agent` – Generates outfit suggestions based on temperature & condition

---

## 🛠️ Technologies

- Java 21+
- Spring Boot
- Google ADK (Agent Development Kit)
- Gemini 2.0 Flash (via Google Generative AI SDK)
- Telex.im (MCP)

---

## 📦 Project Structure
```
src/main/java/hng/backend/fashion_agent/
├── config
│   ├── AgentConfig.java
│   └── MCPAgent*nitializer.java
├── controller
│   └── MCPController.java
├── model
│   └── MCPRequest.java
├── tools
│   ├── AgentTools.java
│   ├── WeatherService.java
│   └── OutfitService.java
├── FashionAgentApplication.java
├── application.properties
├── README.md
└── pom.xml

```


---

## 🧪 Local Testing

Run the app locally:

```bash
Then test via console or send POST requests to:
http://localhost:8080/telex/webhook

Sample payload:
{
  "text": "What should I wear today?"
}
```

## 🌐 Deployment
Deploy to a public HTTPS endpoint using:
- Render
- Railway
- Google Cloud Run
  Ensure your webhook is reachable at:

```
https://your-app.com/telex/webhook
```

## 🔗 Telex.im Integration
1. Invite Yourself
   Run this in Telex chat:
```
/telex-invite your-email@example.com
```

2. Register Workflow
   Create a Mastra workflow JSON like:
```
{
  "name": "fashion_agent",
  "nodes": [
    {
      "id": "fashion_node",
      "name": "Fashion Agent",
      "type": "a2a/mastra-a2a-node",
      "typeVersion": 1,
      "url": "https://your-app.com/telex/webhook",
      "position": [100, 200]
    }
  ],
  "description": "Suggests outfits based on weather",
  "category": "lifestyle",
  "active": true
}
```

Upload it to Mastra or register via API.

📜 Logs
View agent logs:
```
https://api.telex.im/agent-logs/{channel-id}.txt
```

Replace {channel-id} with your Telex channel UUID.

## ✅ Future Improvements
- Auto-detect user location via IP
- Personalize outfit suggestions based on user preferences
- Add seasonal styling tips
- Store user history for smarter recommendations

## 👤 Author
**Name:** AYOBAMI ADEPOJU  
**Email:** ayobamiadepoju263@gmail.com  
**Stack:** Java / Spring Boot  
**GitHub:** [@ayobamiadepoju](https://github.com/ayobamiadepoju)
Task: HNG Stage 3 – Backend (AI Agents)

Built with ❤️ using Google ADK, Gemini, and Spring Boot