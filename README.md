# ♟️ Chess Move Validator

A Java and Spring Boot based Chess Move Validator that validates chess moves and implements important chess rules through a REST API and web interface.

## 🚀 Features

* ♟️ All major chess pieces

  * King
  * Queen
  * Rook
  * Bishop
  * Knight
  * Pawn
* ✅ Legal move validation
* 🚫 Invalid move detection
* 🔄 Turn management
* ⚔️ Piece capturing
* 🛡️ Check detection
* ♟️ Checkmate detection
* 🤝 Stalemate detection
* 🏰 King-side castling
* 🏰 Queen-side castling
* 👑 Pawn promotion

  * Queen
  * Rook
  * Bishop
  * Knight
* 🔒 Game lock after checkmate
* 🔄 Reset game
* 🎯 Legal moves highlighting
* 🌐 Web-based chess board
* 📡 REST API backend

## 🛠️ Technologies Used

* Java
* Spring Boot
* HTML
* CSS
* JavaScript
* Maven
* IntelliJ IDEA

## 📂 Project Structure

```text
chess-move-validator/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── chess_backend/
│       │       ├── chess/
│       │       │   ├── Board.java
│       │       │   ├── ChessPiece.java
│       │       │   ├── King.java
│       │       │   ├── Queen.java
│       │       │   ├── Rook.java
│       │       │   ├── Bishop.java
│       │       │   ├── Knight.java
│       │       │   ├── Pawn.java
│       │       │   ├── MoveValidator.java
│       │       │   ├── CheckDetector.java
│       │       │   ├── CheckmateDetector.java
│       │       │   └── StalemateDetector.java
│       │       │
│       │       ├── controller/
│       │       │   └── ChessController.java
│       │       │
│       │       └── ChessBackendApplication.java
│       │
│       └── resources/
│           └── static/
│               └── index.html
│
├── pom.xml
└── README.md
```

## ▶️ How to Run

### 1. Clone the repository

```bash
git clone <your-repository-url>
```

### 2. Open the project

Open the project in IntelliJ IDEA.

### 3. Run the Spring Boot application

Run:

```text
ChessBackendApplication.java
```

### 4. Open the chess board

Open:

```text
http://localhost:8080/index.html
```

## 🔌 REST API

### Get Board

```text
GET /board
```

### Make Move

```text
POST /move?from=e2&to=e4
```

### Get Legal Moves

```text
GET /legal-moves?position=e2
```

### Check Status

```text
GET /check-status
```

### Game Status

```text
GET /game-status
```

### Reset Game

```text
POST /reset
```

## 🧠 Chess Rules Implemented

The project currently handles:

```text
Normal piece movement
Pawn movement
Pawn two-square initial move
Pawn captures
Path blocking
Turn validation
Check
Checkmate
Stalemate
King-side castling
Queen-side castling
Pawn promotion
Game-over protection
```

## 🎯 Project Goal

This project was created to understand and implement:

* Object-Oriented Programming
* Java interfaces and inheritance
* Chess game logic
* Move validation
* REST APIs
* Spring Boot
* Frontend-backend communication

## 👨‍💻 Author

**Rohan Tyagi**

Computer Science & Engineering Student

### GitHub

https://github.com/rohantyagi14


