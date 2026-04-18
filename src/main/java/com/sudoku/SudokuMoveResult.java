package com.sudoku;

public class SudokuMoveResult {
    private final boolean success;
    private final String message;

    private SudokuMoveResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static SudokuMoveResult success(String message) {
        return new SudokuMoveResult(true, message);
    }

    public static SudokuMoveResult failure(String message) {
        return new SudokuMoveResult(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
