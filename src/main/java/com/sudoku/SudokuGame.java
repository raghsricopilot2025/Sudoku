package com.sudoku;

import java.util.Locale;
import java.util.Scanner;

public class SudokuGame {
    private SudokuGrid gameGrid;

    public SudokuGame() {
        restartGame();
    }

    public void play() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Welcome to Sudoku!\n");
            boolean quitGame = false;
            
            while (!quitGame) {
                System.out.println("This is your puzzle to solve:");
                System.out.println(gameGrid.display());
                System.out.println("Enter command (e.g., A3 4, C5 clear, hint, check, quit):");
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                String command = line.toLowerCase(Locale.ROOT).trim();
                if (command.equals("quit")) {
                    quitGame = true;
                    System.out.println("Thanks for playing. Goodbye!");
                    continue;
                }
                
                if (command.equals("hint")) {
                    String hint = gameGrid.hint();
                    System.out.println(hint);
                    continue;
                }
                
                if (command.equals("check")) {
                    String validation = gameGrid.validate();
                    if (validation == null) {
                        System.out.println("No rule violations detected.");
                    } else {
                        System.out.println(validation);
                    }
                    continue;
                }
                
                SudokuMoveResult result = processMove(command);
                System.out.println(result.getMessage());
                if (gameGrid.isComplete() && result.isSuccess()) {
                    System.out.println("\nYou have successfully completed the Sudoku puzzle!");
                    System.out.println("Press Enter to play again or type quit to exit.");
                    String replay = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
                    if (replay.equals("quit")) {
                        quitGame = true;
                    } else {
                        restartGame();
                    }
                }
            }
        }
    }

    private SudokuMoveResult processMove(String command) {
        String[] tokens = command.split("\\s+");
        if (tokens.length < 2 || tokens.length > 3) {
            return SudokuMoveResult.failure("Invalid command. Use A3 4, C5 clear, hint, check, or quit.");
        }

        String cell = tokens[0].toUpperCase(Locale.ROOT);
        if (cell.length() != 2) {
            return SudokuMoveResult.failure("Invalid cell reference. Use a letter A-I followed by a number 1-9.");
        }

        char rowChar = cell.charAt(0);
        char colChar = cell.charAt(1);
        int row = rowChar - 'A';
        int col = colChar - '1';

        if (row < 0 || row >= 9 || col < 0 || col >= 9) {
            return SudokuMoveResult.failure("Invalid cell reference. Use a letter A-I and a column 1-9.");
        }

        String action = tokens[1].toLowerCase(Locale.ROOT);
        if (action.equals("clear")) {
            if (gameGrid.isCellPrefilled(row, col)) {
                return SudokuMoveResult.failure("Invalid move. " + cell + " is pre-filled.");
            }
            boolean cleared = gameGrid.clearCell(row, col);
            return cleared
                    ? SudokuMoveResult.success("Move accepted.\n\nCurrent grid:\n" + gameGrid.display())
                    : SudokuMoveResult.failure("Unable to clear cell " + cell + ".");
        }

        if (tokens.length != 2 && tokens.length != 3) {
            return SudokuMoveResult.failure("Invalid command format.");
        }

        String valueToken = tokens.length == 3 ? tokens[2] : tokens[1];
        int value;
        try {
            value = Integer.parseInt(valueToken);
        } catch (NumberFormatException ex) {
            return SudokuMoveResult.failure("Invalid number. Please enter a value between 1 and 9.");
        }

        if (value < 1 || value > 9) {
            return SudokuMoveResult.failure("Invalid number. Please enter a value between 1 and 9.");
        }

        if (gameGrid.isCellPrefilled(row, col)) {
            return SudokuMoveResult.failure("Invalid move. " + cell + " is pre-filled.");
        }

        if (!gameGrid.canPlaceValue(row, col, value)) {
            return SudokuMoveResult.failure("Invalid move. " + value + " cannot be placed at " + cell + " because it violates Sudoku rules.");
        }

        boolean placed = gameGrid.placeValue(row, col, value);
        return placed
                ? SudokuMoveResult.success("Move accepted.\n\nCurrent grid:\n" + gameGrid.display())
                : SudokuMoveResult.failure("Unable to place value at " + cell + ".");
    }

    private void restartGame() {
        int[][] solution = SudokuGenerator.generateSolution();
        int[][] puzzle = SudokuGenerator.generatePuzzle(solution, 30);
        this.gameGrid = new SudokuGrid(puzzle, solution);
    }
}
