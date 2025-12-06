# Poker Simulation API

Eine Spring Boot REST API zur Simulation von Texas Hold'em Poker-Händen mit vollständiger Handauswertung und Wahrscheinlichkeitsberechnung.

## Features

- ✅ Vollständige Texas Hold'em Handauswertung (alle 10 Handränge)
- ✅ Monte-Carlo-Simulation für Gewinnwahrscheinlichkeiten
- ✅ Unterstützung für beliebig viele Spieler
- ✅ Flexible Community Cards (0-5 Karten)
- ✅ Korrekte Tie-Behandlung
- ✅ Best Hand Rank bei vollständigen Board (5 Community Cards)
- ✅ Umfassende Unit Tests
- ✅ REST API mit Validierung

## Handränge (von niedrig zu hoch)

1. High Card
2. One Pair
3. Two Pair
4. Three of a Kind
5. Straight
6. Flush
7. Full House
8. Four of a Kind
9. Straight Flush
10. Royal Flush

## API Endpunkt

### POST /api/poker/simulate

Simuliert Poker-Hände und gibt Gewinnwahrscheinlichkeiten zurück.

#### Request Body

```json
{
  "players": [
    {
      "name": "Alice",
      "holeCards": ["AS", "KS"]
    },
    {
      "name": "Bob",
      "holeCards": ["QH", "QD"]
    }
  ],
  "communityCards": ["JS", "10S", "9S"],
  "numberOfSimulations": 10000
}
```

#### Request-Parameter

- **players** (required): Liste der Spieler, mindestens 2
  - **name**: Spielername
  - **holeCards**: Genau 2 Karten im Format `[Rank][Suit]`
    - Ranks: `2-10, J, Q, K, A`
    - Suits: `H` (Hearts), `D` (Diamonds), `C` (Clubs), `S` (Spades)
- **communityCards** (optional): 0-5 Community Cards
- **numberOfSimulations** (required): Anzahl der Simulationen (1-1,000,000)

#### Response

```json
{
  "playerResults": [
    {
      "name": "Alice",
      "winPercentage": 65.42,
      "bestHandRank": "Straight Flush"
    },
    {
      "name": "Bob",
      "winPercentage": 30.15,
      "bestHandRank": "One Pair"
    }
  ],
  "tiePercentage": 4.43,
  "totalSimulations": 10000
}
```

**Hinweis:** `bestHandRank` wird nur ausgegeben, wenn alle 5 Community Cards angegeben wurden.

## Beispiele

### Beispiel 1: Vollständiges Board (5 Community Cards)

```bash
curl -X POST http://localhost:8080/api/poker/simulate \
  -H "Content-Type: application/json" \
  -d '{
    "players": [
      {"name": "Alice", "holeCards": ["AS", "KS"]},
      {"name": "Bob", "holeCards": ["2H", "3H"]}
    ],
    "communityCards": ["QS", "JS", "10S", "9S", "8S"],
    "numberOfSimulations": 100
  }'
```

**Erwartetes Ergebnis:** Alice gewinnt mit Straight Flush (100% Gewinnchance)

### Beispiel 2: Teilweise Community Cards

```bash
curl -X POST http://localhost:8080/api/poker/simulate \
  -H "Content-Type: application/json" \
  -d '{
    "players": [
      {"name": "Alice", "holeCards": ["AS", "AH"]},
      {"name": "Bob", "holeCards": ["KD", "KC"]}
    ],
    "communityCards": ["AD", "AC"],
    "numberOfSimulations": 5000
  }'
```

**Erwartetes Ergebnis:** Alice gewinnt sehr oft mit 4 Assen

### Beispiel 3: Keine Community Cards (Pre-Flop Simulation)

```bash
curl -X POST http://localhost:8080/api/poker/simulate \
  -H "Content-Type: application/json" \
  -d '{
    "players": [
      {"name": "Alice", "holeCards": ["AS", "AH"]},
      {"name": "Bob", "holeCards": ["2D", "3C"]},
      {"name": "Charlie", "holeCards": ["KS", "KH"]}
    ],
    "numberOfSimulations": 10000
  }'
```

**Erwartetes Ergebnis:** Alice (Pocket Aces) hat die höchste Gewinnwahrscheinlichkeit

### Beispiel 4: Tie-Szenario

```bash
curl -X POST http://localhost:8080/api/poker/simulate \
  -H "Content-Type: application/json" \
  -d '{
    "players": [
      {"name": "Alice", "holeCards": ["2H", "3H"]},
      {"name": "Bob", "holeCards": ["2D", "3D"]}
    ],
    "communityCards": ["AS", "KS", "QS", "JS", "10S"],
    "numberOfSimulations": 100
  }'
```

**Erwartetes Ergebnis:** 100% Tie, da beide den Royal Flush auf dem Board spielen

## Installation & Start

### Voraussetzungen

- Java 17 oder höher
- Maven

### Anwendung starten

```bash
# Mit Maven Wrapper
./mvnw spring-boot:run

# Oder mit installiertem Maven
mvn spring-boot:run
```

Die Anwendung startet auf `http://localhost:8080`

## Tests ausführen

```bash
# Alle Tests ausführen
./mvnw test

# Mit Coverage-Report
./mvnw test jacoco:report
```

## Projektstruktur

```
src/
├── main/
│   └── java/org/pokerapp/pokersimulationcopilotagent/
│       ├── controller/
│       │   └── PokerSimulationController.java
│       ├── dto/
│       │   ├── PlayerRequest.java
│       │   ├── PlayerResult.java
│       │   ├── SimulationRequest.java
│       │   └── SimulationResponse.java
│       ├── model/
│       │   ├── Card.java
│       │   ├── HandEvaluation.java
│       │   ├── HandRank.java
│       │   ├── Rank.java
│       │   └── Suit.java
│       ├── service/
│       │   ├── HandEvaluatorService.java
│       │   └── PokerSimulationService.java
│       └── PokerSimulationCopilotAgentApplication.java
└── test/
    └── java/org/pokerapp/pokersimulationcopilotagent/
        ├── controller/
        │   └── PokerSimulationControllerTest.java
        └── service/
            ├── HandEvaluatorServiceTest.java
            └── PokerSimulationServiceTest.java
```

## Validierung

Die API validiert folgende Bedingungen:

- Mindestens 2 Spieler erforderlich
- Jeder Spieler muss genau 2 Hole Cards haben
- Maximal 5 Community Cards erlaubt
- Keine doppelten Karten
- Anzahl der Simulationen muss zwischen 1 und 1,000,000 liegen
- Gültige Kartenformate (z.B. "AS", "10H", "2D")

## Error Handling

Bei Validierungsfehlern gibt die API einen 400 Bad Request mit einer Fehlermeldung zurück:

```json
{
  "error": "Duplicate card found: AS"
}
```

## Technologie-Stack

- Spring Boot 3.5.7
- Java 17
- Lombok
- JUnit 5
- Maven

## Lizenz

