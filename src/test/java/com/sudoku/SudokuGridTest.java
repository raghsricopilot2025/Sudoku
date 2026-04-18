package com.sudoku;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuGridTest {

    @Test
    void generatedSolutionIsValid() {
        int[][] solution = SudokuGenerator.generateSolution();
        SudokuGrid grid = new SudokuGrid(SudokuGenerator.generatePuzzle(solution, 81), solution);
        assertNull(grid.validate(), "Generated solution should be valid.");
    }

    @Test
    void puzzleHasThirtyPrefilledCells() {
        int[][] solution = SudokuGenerator.generateSolution();
        int[][] puzzle = SudokuGenerator.generatePuzzle(solution, 30);
        int filled = 0;
        for (int[] row : puzzle) {
            for (int value : row) {
                if (value != 0) {
                    filled++;
                }
            }
        }
        assertEquals(30, filled, "Puzzle should have 30 prefilled cells.");
    }

    @Test
    void placeValueAndClearWorks() {
        int[][] solution = SudokuGenerator.generateSolution();
        int[][] puzzle = SudokuGenerator.generatePuzzle(solution, 30);
        SudokuGrid grid = new SudokuGrid(puzzle, solution);

        outer:
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (!grid.isCellPrefilled(row, col)) {
                    int value = solution[row][col];
                    assertTrue(grid.placeValue(row, col, value));
                    assertNull(grid.validate());
                    assertTrue(grid.clearCell(row, col));
                    assertNull(grid.validate());
                    break outer;
                }
            }
        }
    }

    @Test
    void validationDetectsDuplicates() {
        int[][] solution = SudokuGenerator.generateSolution();
        int[][] puzzle = SudokuGenerator.generatePuzzle(solution, 30);
        SudokuGrid grid = new SudokuGrid(puzzle, solution);

        for (int row = 0; row < 9; row++) {
            int duplicateValue = -1;
            int targetCol = -1;
            for (int col = 0; col < 9; col++) {
                if (grid.isCellPrefilled(row, col) && duplicateValue < 0) {
                    duplicateValue = puzzle[row][col];
                }
                if (!grid.isCellPrefilled(row, col) && targetCol < 0) {
                    targetCol = col;
                }
            }
            if (duplicateValue > 0 && targetCol >= 0) {
                assertTrue(grid.placeValue(row, targetCol, duplicateValue));
                assertNotNull(grid.validate(), "Validation should detect a duplicate move in row.");
                return;
            }
        }
        fail("Could not find a valid duplicate test scenario.");
    }
}
