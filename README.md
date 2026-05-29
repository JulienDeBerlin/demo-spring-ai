# Demo Spring AI – Dating Profile Moderation

A minimal Spring Boot demo exploring **Spring AI** for AI-powered content moderation.

## Features

- REST API `POST /profiles` to create dating profiles
- AI-based bio moderation (rejects inappropriate content)
- Supports **two AI providers**: OpenAI and Ollama
- Provider selection via environment variable
- Clean Architecture (Ports & Adapters)
- Simple HTML frontend

---

## Quick Start

### Option 1: Run with Docker Compose (Ollama – no API key needed)

```bash
# Start the app + Ollama
docker compose up -d

# Pull the Ollama model (first time only)
docker compose exec ollama ollama pull llama3.2

# Open the app
open http://localhost:8080
```

To use a different model:
```bash
OLLAMA_MODEL=mistral docker compose up -d
docker compose exec ollama ollama pull mistral
```

### Option 2: Run with OpenAI

```bash
AI_PROVIDER=openai OPENAI_API_KEY=sk-proj-... docker compose up -d
```

Or run locally without Docker:
```bash
export AI_PROVIDER=openai
export OPENAI_API_KEY=sk-proj-...
./mvnw spring-boot:run
```

### Option 3: Run locally with Ollama installed on your machine

```bash
# Make sure Ollama is running on localhost:11434
ollama pull llama3.2
export AI_PROVIDER=ollama
./mvnw spring-boot:run
```

---

## Configuration

All settings are configurable via environment variables:

| Variable          | Default                   | Description                      |
|-------------------|---------------------------|----------------------------------|
| `AI_PROVIDER`     | `ollama`                  | `openai` or `ollama`             |
| `OPENAI_API_KEY`  | –                         | Your OpenAI API key              |
| `OPENAI_MODEL`    | `gpt-4o-mini`             | OpenAI model to use              |
| `OLLAMA_BASE_URL` | `http://localhost:11434`  | Ollama server URL                |
| `OLLAMA_MODEL`    | `llama3.2`                | Ollama model to use              |

---

## API

### Create Profile

```
POST /profiles
Content-Type: application/json

{
  "name": "Julien",
  "age": 35,
  "bio": "I love dancing forró and gardening."
}
```

**Success** → `201 Created`
```json
{
  "id": "uuid",
  "name": "Julien",
  "age": 35,
  "bio": "I love dancing forró and gardening."
}
```

**Rejected by moderation** → `400 Bad Request`
```json
{
  "message": "Content contains off-platform contact attempt.",
  "categories": ["OFF_PLATFORM_CONTACT"]
}
```

---

## Project Structure (Clean Architecture)

```
src/main/java/de/gematik/demospringai/
├── domain/model/          # Profile, ModerationResult, ModerationCategory
├── domain/exception/      # ProfileRejectedException
├── application/port/in/   # CreateProfileUseCase
├── application/port/out/  # ModerationPort, ProfileRepository
├── application/service/   # ProfileService
├── adapter/in/web/        # Controller, ExceptionHandler, DTOs
├── adapter/out/ai/        # OpenAiModerationAdapter, OllamaModerationAdapter
└── adapter/out/persistence/ # InMemoryProfileRepository
```

---

## Running Tests

```bash
./mvnw test
```

Integration tests mock the `ModerationPort` – no AI provider needed.

---

## Tech Stack

- Java 21
- Spring Boot 4.0
- Spring AI 2.0
- Spring Web + Validation
- OpenAI / Ollama (configurable)
- Docker & Docker Compose

