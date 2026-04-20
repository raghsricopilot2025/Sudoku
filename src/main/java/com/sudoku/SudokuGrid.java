package com.sudoku;

import java.util.ArrayList;
import java.util.List;

public class SudokuGrid {
    private final int[][] solution;
    private final int[][] initial;
    private final int[][] current;

    public SudokuGrid(int[][] initialPuzzle, int[][] solution) {
        if (initialPuzzle.length != 9 || solution.length != 9) {
            throw new IllegalArgumentException("Puzzle and solution must be 9x9.");
        }
        this.solution = copy(solution);
        this.initial = copy(initialPuzzle);
        this.current = copy(initialPuzzle);
    }

    public boolean isCellPrefilled(int row, int col) {
        return initial[row][col] != 0;
    }

    public boolean canPlaceValue(int row, int col, int value) {
        if (value < 1 || value > 9) {
            return false;
        }
        if (current[row][col] == value) {
            return true;
        }
        for (int i = 0; i < 9; i++) {
            if (current[row][i] == value) {
                return false;
            }
            if (current[i][col] == value) {
                return false;
            }
        }
        int boxRowStart = (row / 3) * 3;
        int boxColStart = (col / 3) * 3;
        for (int r = boxRowStart; r < boxRowStart + 3; r++) {
            for (int c = boxColStart; c < boxColStart + 3; c++) {
                if (current[r][c] == value) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean placeValue(int row, int col, int value) {
        if (isCellPrefilled(row, col) || value < 1 || value > 9) {
            return false;
        }
        current[row][col] = value;
        return true;
    }

    public boolean clearCell(int row, int col) {
        if (isCellPrefilled(row, col)) {
            return false;
        }
        current[row][col] = 0;
        return true;
    }

    public String validate() {
        List<String> issues = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            boolean[] seen = new boolean[10];
            for (int col = 0; col < 9; col++) {
                int value = current[row][col];
                if (value == 0) continue;
                if (seen[value]) {
                    issues.add("Number " + value + " already exists in Row " + (char) ('A' + row) + ".");
                    break;
                }
                seen[value] = true;
            }
        }
        for (int col = 0; col < 9; col++) {
            boolean[] seen = new boolean[10];
            for (int row = 0; row < 9; row++) {
                int value = current[row][col];
                if (value == 0) continue;
                if (seen[value]) {
                    issues.add("Number " + value + " already exists in Column " + (col + 1) + ".");
                    break;
                }
                seen[value] = true;
            }
        }
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                boolean[] seen = new boolean[10];
                for (int row = boxRow * 3; row < boxRow * 3 + 3; row++) {
                    for (int col = boxCol * 3; col < boxCol * 3 + 3; col++) {
                        int value = current[row][col];
                        if (value == 0) continue;
                        if (seen[value]) {
                            issues.add("Number " + value + " already exists in subgrid starting at " + (char) ('A' + boxRow * 3) + (boxCol * 3 + 1) + ".");
                            break;
                        }
                        seen[value] = true;
                    }
                }
            }
        }
        return issues.isEmpty() ? null : String.join("\n", issues);
    }

    public String hint() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (current[row][col] == 0) {
                    char rowChar = (char) ('A' + row);
                    return "Hint: Cell " + rowChar + (col + 1) + " = " + solution[row][col];
                }
            }
        }
        return "No hints available. The grid is already complete.";
    }

    public boolean isComplete() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (current[row][col] == 0 || current[row][col] != solution[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    public String display() {
        StringBuilder builder = new StringBuilder();
        builder.append("    1 2 3 4 5 6 7 8 9\n");
        for (int row = 0; row < 9; row++) {
            builder.append("  ").append((char) ('A' + row)).append(' ');
            for (int col = 0; col < 9; col++) {
                int value = current[row][col];
                builder.append(value == 0 ? 'E' : value);
                if (col < 8) {
                    builder.append(' ');
                }
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    private static int[][] copy(int[][] source) {
        int[][] copy = new int[9][9];
        for (int row = 0; row < 9; row++) {
            System.arraycopy(source[row], 0, copy[row], 0, 9);
        }
        return copy;
    }
}
