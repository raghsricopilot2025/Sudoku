package com.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {
    private static final Random RANDOM = new Random();

    public static int[][] generateSolution() {
        int[][] grid = new int[9][9];
        fillGrid(grid, 0, 0);
        return grid;
    }

    public static int[][] generatePuzzle(int[][] solution, int prefilledCount) {
        int[][] puzzle = copy(solution);
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions, RANDOM);
        int cellsToRemove = 81 - prefilledCount;
        for (int i = 0; i < cellsToRemove; i++) {
            int position = positions.get(i);
            puzzle[position / 9][position % 9] = 0;
        }
        return puzzle;
    }

    private static boolean fillGrid(int[][] grid, int row, int col) {
        if (row == 9) {
            return true;
        }
        int nextRow = col == 8 ? row + 1 : row;
        int nextCol = col == 8 ? 0 : col + 1;
        List<Integer> values = valueList();
        for (int value : values) {
            if (isSafe(grid, row, col, value)) {
                grid[row][col] = value;
                if (fillGrid(grid, nextRow, nextCol)) {
                    return true;
                }
                grid[row][col] = 0;
            }
        }
        return false;
    }

    private static boolean isSafe(int[][] grid, int row, int col, int value) {
        for (int i = 0; i < 9; i++) {
            if (grid[row][i] == value || grid[i][col] == value) {
                return false;
            }
        }
        int boxRowStart = (row / 3) * 3;
        int boxColStart = (col / 3) * 3;
        for (int r = boxRowStart; r < boxRowStart + 3; r++) {
            for (int c = boxColStart; c < boxColStart + 3; c++) {
                if (grid[r][c] == value) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<Integer> valueList() {
        List<Integer> values = new ArrayList<>();
        for (int value = 1; value <= 9; value++) {
            values.add(value);
        }
        Collections.shuffle(values, RANDOM);
        return values;
    }

    private static int[][] copy(int[][] source) {
        int[][] copy = new int[9][9];
        for (int row = 0; row < 9; row++) {
            System.arraycopy(source[row], 0, copy[row], 0, 9);
        }
        return copy;
    }
}
