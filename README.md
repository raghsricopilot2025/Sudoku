# Sudoku CLI Java Application

This project implements a command-line Sudoku game in Java.

## Features
- Generates a valid Sudoku puzzle with 30 pre-filled cells.
- Supports user commands: `A3 4`, `C5 clear`, `hint`, `check`, `quit`.
- Validates moves against Sudoku rules.
- Reveals a hint for a single empty cell.
- Displays the board in a readable grid format.

## Requirements
- Java 18 or later.

## Build and Test
Open PowerShell in the project root and run:

```powershell
mvn clean test
```

If Maven is not installed, the project can still be built using `build.ps1`.

## Run
Use Maven to start the application from the project root:

```powershell
mvn exec:java -Dexec.mainClass=com.sudoku.SudokuApp
```

Alternatively, after building, run the compiled classes directly:

```powershell
java -cp target/classes com.sudoku.SudokuApp
```

## Notes
- This project uses the Java standard library plus JUnit for test execution.
- Maven is configured in `pom.xml`.
