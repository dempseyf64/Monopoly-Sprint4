---
# Monopoly-Project Sprint 4
Monopoly Project for CSCI 234. 

## CI Status
[![Maven](https://github.com/dempseyf64/Monopoly-Sprint4/actions/workflows/maven-checklist.yml/badge.svg)](https://github.com/dempseyf64/Monopoly-Sprint4/actions/workflows/maven-checklist.yml)

## Authors
- **Finn Dempsey**
- **Kristian Wright**
- **Collin Cabral-Castro**
- **Rachele Grigoli**

[**BurnDown Chart**](https://moravian0-my.sharepoint.com/:x:/g/personal/wrightk_moravian_edu/Ec9vbNkXKkBAhebGWkPVn3gBCRMl1tALIvEDojNvpZFdHg?e=RiQ86v)

## About the Project
This project is a digital implementation of the classic Monopoly board game, developed as part of the CSCI 234 course. It features:
- A fully functional game board
- Player management
- Property handling
- A graphical user interface (GUI) for user interaction.

## Features
- **Game Board**: A dynamic game board with support for multiple players and properties.
- **Player Management**: Tracks player tokens, turns, and game progress.
- **Bank System**: Manages in-game currency and property ownership.
- **Property System**: Handles property details such as name, price, color, and ownership.
- **Dice Rolling**: Simulates dice rolls and tracks doubles.
- **Graphical User Interface (GUI)**: Provides an interactive interface for gameplay.
- **JUnit Testing**: Comprehensive test coverage for core game logic and GUI components.

## Project Structure
- **Model**: Core game logic, including classes for `Bank`, `Player`, `Property`, `GameBoard`, and `Dice`.
- **View**: Graphical representation of the game, including `GameBoardView` and `DiceView`.
- **ViewTests**: JUnit test cases for both GUI and game logic.

## How to Run
1. Clone the repository to your local machine:
   ```bash
   git clone <repository-url>
   ```
2. Ensure you have:
   - Java
   - Maven
   - SDK 23.0.2 installed
3. Navigate to the project directory:
   ```bash
   cd Monopoly-Sprint4
   ```
4. Use the `run.sh` script to build and run the project:
   ```bash
   sh run.sh
   ```

## UML Diagram
![UML Diagram](https://github.com/grigolir/CMonopolyProject-Sprint-3/blob/main/UML%20Sprint%203.png)

---

### Improvements Made:
1. Organized the sections with clear headings and bullet points for better readability.
2. Improved grammar and clarity in the descriptions.
3. Provided a cleaner and consistent structure.
4. Added inline code formatting for commands and filenames to make them stand out.

Let me know if you'd like additional changes!
