# BoardGames

This project contains simple implementations of classic board games: Tic-Tac-Toe and Reversi, built with Java Swing.

## Project Structure

The project follows a standard Java package structure for better organization and maintainability.

```
.
└── src/
    └── com/
        └── boardgames/
            ├── common/
            │   └── AIPlayer.java
            ├── reversi/
            │   ├── ReversiAIPlayer.java
            │   ├── ReversiGame.java
            │   └── ReversiGUI.java
            ├── tictactoe/
            │   ├── TicTacToeGame.java
            │   └── TicTacToeGUI.java
            ├── GameSelectionGUI.java
            └── Main.java
```

-   `src/com/boardgames/Main.java`: The main entry point of the application, responsible for launching the game selection.
-   `src/com/boardgames/GameSelectionGUI.java`: Provides a graphical interface for selecting which game to play (Tic-Tac-Toe or Reversi).
-   `src/com/boardgames/common/AIPlayer.java`: Contains a generic AI player implementation (currently used by Tic-Tac-Toe).
-   `src/com/boardgames/reversi/`: Contains all classes related to the Reversi game.
    -   `ReversiGame.java`: Implements the game logic for Reversi.
    -   `ReversiGUI.java`: Provides the graphical user interface for Reversi.
    -   `ReversiAIPlayer.java`: Implements the AI player logic specifically for Reversi.
-   `src/com/boardgames/tictactoe/`: Contains all classes related to the Tic-Tac-Toe game.
    -   `TicTacToeGame.java`: Implements the game logic for Tic-Tac-Toe.
    -   `TicTacToeGUI.java`: Provides the graphical user interface for Tic-Tac-Toe.

## How to Run

1.  **Compile the Java files:**
    Open a terminal in the project root directory (`BoardGames/`) and run:
    ```bash
    javac -d bin src/com/boardgames/**/*.java
    ```
    This command compiles all Java files and places the compiled `.class` files into a `bin` directory.

2.  **Run the application:**
    From the project root directory, run:
    ```bash
    java -cp bin com.boardgames.Main
    ```
    This will launch the game selection window.

## Features

-   **Game Selection:** Choose between Tic-Tac-Toe and Reversi.
-   **Tic-Tac-Toe:** Play against another player or a simple AI.
-   **Reversi:** Play against another player or an AI.
-   **Graphical User Interface (GUI):** Built with Java Swing, featuring a modern and simple color palette.
